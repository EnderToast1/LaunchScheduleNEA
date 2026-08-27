package com.niaw.launchnea.networking

import com.niaw.launchnea.utils.LandingType
import com.niaw.launchnea.utils.LeaderType
import com.niaw.launchnea.utils.Location
import com.niaw.launchnea.utils.ProgramType
import com.niaw.launchnea.utils.Reusable
import com.niaw.launchnea.utils.ReuseType
import com.niaw.launchnea.utils.Status
import com.niaw.launchnea.utils.StatusReuse
import com.niaw.launchnea.utils.TimelineEvent
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.MetaData
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.utils.NotificationScheduler
import com.niaw.launchnea.utils.enableLaunchReminder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.collections.List
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit



suspend fun updateLaunchData(scheduleDao: ScheduleDao){

    //finds the last time the database updated and then updates it so it set to now()
    var databaseLastUpdated= scheduleDao.getMetaData("lastUpdated")//Gets lastUpdates time
    println("Last Updated: $databaseLastUpdated")//LOG


    //Only for if DB is reset
    if (databaseLastUpdated == null) {//if it doesn't exist set to some time SHOULD BE TEMP AS WHEN IN PRODUCTION WILL ALWAYS EXIST
        databaseLastUpdated=(LocalDateTime.of(2026,8,25,0,0)).toString()  //some one-time variable will only be used when Database is reset in TESTing
        scheduleDao.upsertMetadata(MetaData("lastUpdated",databaseLastUpdated))
    }

    // Overlap by 1 minute so updates at the boundary aren't missed
    databaseLastUpdated = LocalDateTime.parse(databaseLastUpdated)
        .minusMinutes(1)
        .toString()

    val callApiUrl="https://ll.thespacedevs.com/2.3.0/launches/?mode=detailed&limit=100&ordering=-last_updated&last_updated__gte=$databaseLastUpdated"


    //Updates previous launches
    println("\n\nCalled $callApiUrl")
    var dbUpdated=true
    var callResponse=APIFetchRequest(callApiUrl) //passes the url as a string with the last updated dateTime as part of it in the API filter system


    if(callResponse.errorType==null){
        var i=1
        println("NEXT RESPONSE: #${i}")
        responseToDatabase(scheduleDao,callResponse)

        while (callResponse.next!=null){
            i++
            println("NEXT RESPONSE: #${i}")
            callResponse=APIFetchRequest(callResponse.next)
            callResponse.count-=(i*100)
            if(callResponse.errorType==null){
                responseToDatabase(scheduleDao,callResponse)
            }else{
                if (callResponse.errorType == NetworkError.NO_DATA){
                    println("\nNO DATA\n")
                }else if(callResponse.errorType!=NetworkError.TOO_MANY_REQUESTS){
                    println("\nTOO_MANY_REQUESTS\n")
                    dbUpdated=false
                    requestNumber()

                }else{
                    println(callResponse.errorType)
                    dbUpdated=false
                    requestNumber()
                }
            }
        }
    }else{
        println("Error: "+callResponse.errorType)
        if (callResponse.errorType == NetworkError.NO_DATA) {
            println("\nNO DATA\n")
        }else if (callResponse.errorType == NetworkError.NO_INTERNET){
            dbUpdated=false
        }else{
            dbUpdated=false
            requestNumber()
        }
    }


    if (dbUpdated){
        scheduleDao.upsertMetadata(MetaData("lastUpdated",LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString() ))  //updates LastUpdated to now
        println("LastUpdated Updated to ${scheduleDao.getMetaData("lastUpdated")}")
    }
    println("\nDone updateLaunchData")
    requestNumber()
}

//parses the response and adds each launch to the database (also calls for a function to check for other tables that need adding to)
suspend fun responseToDatabase(scheduleDao: ScheduleDao, callResponse: ResponseData) {

    //checks if there was an error reported when getting the response
    if (callResponse.errorType == null) {
        println("Count: ${callResponse.count}\n")//LOG

        //if there was no error it sets it to a JsonArray and calls a function to parse each launch into an object then adds to db
        val launchesJsonArray = callResponse.data
        launchesJsonArray?.forEachIndexed {i, launchJson ->              //goes though each launch in the return from the API

            try {
                val launch = parseLaunch(launchJson)         //first it creates a launch object for the launch
                println("\n\nLaunch number ${i + 1}: \n$launch")//LOG
                if (launch != null) {
                    if (scheduleDao.isStarred(launch.launchID)) {
                        NotificationScheduler.cancelNotification(launch.launchID)
                        enableLaunchReminder(launch)
                    }

                    checkForOtherEntities(launchJson, launch, scheduleDao)
                    scheduleDao.upsertLaunch(launch) //adds to database if not null
                    checkForReuse(launchJson, launch, scheduleDao)
                }
            }catch (e:Exception){
                println("Error: ${e.message}")
            }



            }
        println("\n\nNext: ${callResponse.next}\n")
    }else {//if there is an error it logs it
        println("ERROR: ${callResponse.errorType}")//LOG
    }
    }

//parse each launch element into a launch object
suspend fun parseLaunch(launchJsonEle: JsonElement): Launch? {
    val launchJsonObj=launchJsonEle.jsonObject  //turns the element into an object
    val id= launchJsonObj["id"]?.jsonPrimitive?.content ?: return null  //finds the tag of "id" then gets the content after it is made JsonPrimitive if it is null, does not exist or cannot be made primitive it is set to null
    val url = launchJsonObj["url"]?.jsonPrimitive?.content
    var missionName= (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    if (missionName==null){
        missionName=launchJsonObj["name"]?.jsonPrimitive?.content
    }
    val padName= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val agencyID= launchJsonObj["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketVersionID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketID=if ( launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject!=null){
        launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content
    }else{
        "NoFamily$rocketVersionID"
    }


    val launchTimeUTC: ZonedDateTime? =launchJsonObj["net"]?.jsonPrimitive?.content?.let { runCatching { ZonedDateTime.parse(it) }.getOrNull() }
    val padTimeZoneID = (launchJsonObj["pad"] as? JsonObject)?.get("location")?.jsonObject?.get("timezone_name")?.jsonPrimitive?.content?.takeIf { it.isNotBlank() } // <- ignore empty strings
    val launchTimePad: ZonedDateTime? = if (launchTimeUTC != null && padTimeZoneID != null) {runCatching { launchTimeUTC.withZoneSameInstant(ZoneId.of(padTimeZoneID)) }.getOrNull()//gets the pad launch timezone id and the UTC launch time and the applies it if one or both is null or the timezone id is invalid it returns the UTC time
    } else {
        launchTimeUTC//Fallback
    }
    val launcherStages = launchJsonObj["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray//create an array of all the booster/launcher reuse parts
    val boosterLandings: List<Reusable> =launcherStages?.mapNotNull { stage -> //goes through all booster reuses and places them in a reuses object
                val landingLocation =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull
                val landingAbbrev   =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
                val landingType     =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("type")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
                val landingTypeInForm= when (landingType) {//gets the landing type as a string then turns it into a LandingType
                    "ASDS" -> LandingType.ASDS
                    "RTLS" -> LandingType.RTLS
                    "PCL" -> LandingType.PCL
                    "Ocean" -> LandingType.OCEAN
                    "HC" -> LandingType.HC
                    "HL" -> LandingType.HL
                    "ATM" -> LandingType.ATM
                    "EXP" -> LandingType.EXP
                    "VL" -> LandingType.VL
                    "PFL" -> LandingType.PFL
                    else -> LandingType.UNKNOWN
                }
                val statusReuse = (stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("success")?.jsonPrimitive?.booleanOrNull)

                val flightNumber= stage.jsonObject["launcher"]?.takeIf { it != JsonNull }?.jsonObject?.get("flights")?.jsonPrimitive?.intOrNull
                val reuseID = stage.jsonObject["launcher"]?.takeIf { it != JsonNull }?.jsonObject?.get("id")?.jsonPrimitive?.contentOrNull
                val reuseName = stage.jsonObject["launcher"]?.takeIf { it != JsonNull }?.jsonObject?.get("serial_number")?.jsonPrimitive?.contentOrNull


                    Reusable(
                        reuseID = reuseID,
                        reuseName = reuseName,
                        flightNumber = flightNumber,
                        landingLocationAbbr = landingAbbrev,
                        landingLocationName = landingLocation,
                        landingType = landingTypeInForm,
                        status = statusReuse,
                        downrangeDistanceKM =  stage.jsonObject["landing"]?.takeIf { it is JsonObject }?.jsonObject?.get("downrange_distance")?.jsonPrimitive?.doubleOrNull,reuseType = ReuseType.BOOSTER
                    )
            }
            ?: emptyList()

    val spacecraftStages = launchJsonObj["rocket"]?.jsonObject?.get("spacecraft_stage")?.jsonArray//create an array of all reusable spacecraft items
    val capsulesLandings:List<Reusable> =spacecraftStages?.mapNotNull { stage ->//goes through each one and creates a reusable object of it 
            val capLandingLocation =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull
            val capLandingAbbrev   =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
            val capLandingType     =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("type")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
            val capLandingTypeInForm= when (capLandingType) {//gets the landing type as a string then turns it into a LandingType
                    "ASDS" -> LandingType.ASDS
                    "RTLS" -> LandingType.RTLS
                    "PCL" -> LandingType.PCL
                    "Ocean" -> LandingType.OCEAN
                    "HC" -> LandingType.HC
                    "HL" -> LandingType.HL
                    "ATM" -> LandingType.ATM
                    "EXP" -> LandingType.EXP
                    "VL" -> LandingType.VL
                    "PFL" -> LandingType.PFL
                    else -> LandingType.UNKNOWN
            }
            val capStatusReuse = (stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("success")?.jsonPrimitive?.booleanOrNull)

            val flightNumber= stage.jsonObject["spacecraft"]?.takeIf { it != JsonNull }?.jsonObject?.get("flights_count")?.jsonPrimitive?.intOrNull
            val reuseID = stage.jsonObject["spacecraft"]?.takeIf { it != JsonNull }?.jsonObject?.get("id")?.jsonPrimitive?.contentOrNull
            val reuseName = stage.jsonObject["spacecraft"]?.takeIf { it != JsonNull }?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull


            Reusable(
                reuseID = reuseID,
                reuseName= reuseName,
                flightNumber = flightNumber,
                landingLocationAbbr = capLandingAbbrev,
                landingLocationName = capLandingLocation,
                landingType = capLandingTypeInForm,
                status =capStatusReuse,
                downrangeDistanceKM = (stage.jsonObject["landing"] as? JsonObject)?.jsonObject?.get("downrange_distance")?.jsonPrimitive?.doubleOrNull,
                reuseType = ReuseType.SHIP_CAPSULE
            )
        }
            ?: emptyList()

    val stageLandings=boosterLandings+capsulesLandings

    val success= when(launchJsonObj["status"]?.jsonObject?.get("id")?.jsonPrimitive?.int) {
        1 -> Status.GO_FOR_LAUNCH
        2 -> Status.TBD
        3 -> Status.SUCCESS
        4 -> Status.FAILURE
        5 -> Status.ON_HOLD
        6 -> Status.IN_FLIGHT
        7 -> Status.PARTIAL_SUCCESS
        8 -> Status.TBC
        9 -> Status.PAYLOAD_DEPLOYED
        null -> Status.STATUS_MISSING
        else -> Status.UNKNOWN
    }
    val windowOpenUTC: ZonedDateTime? =launchJsonObj["window_start"]?.jsonPrimitive?.content?.let { runCatching { ZonedDateTime.parse(it) }.getOrNull() }
    val windowCloseUTC: ZonedDateTime? =launchJsonObj["window_end"]?.jsonPrimitive?.content?.let { runCatching { ZonedDateTime.parse(it) }.getOrNull() }
    val imageUrl = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("thumbnail_url")?.jsonPrimitive?.content
    val imageCredit = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("credit")?.jsonPrimitive?.content
    val imageLicenseType = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val weatherProbability = launchJsonObj["probability"]?.jsonPrimitive?.doubleOrNull
    val agencyName = launchJsonObj["launch_service_provider"]?.jsonObject?.get("name")?.jsonPrimitive?.content
    val rocketName = launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val rocketVariantName = launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("full_name")?.jsonPrimitive?.content
    val description = (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("description")?.jsonPrimitive?.content
    val orbit = ((launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("orbit") as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padLat= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("latitude")?.jsonPrimitive?.double
    val padLong= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("longitude")?.jsonPrimitive?.double
    val padCords = Location(padLat,padLong)
    val padLocation = ((launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("location") as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padCountry = ((launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("country") as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val netPrecision = (launchJsonObj["net_precision"] as? JsonObject)?.jsonObject ?.get("description")?.jsonPrimitive?.content
    val precisionAbbrev = (launchJsonObj["net_precision"] as? JsonObject)?.jsonObject?.get("abbrev")?.jsonPrimitive?.content
    val weatherConcerns= launchJsonObj["weather_concerns"]?.jsonPrimitive?.content
 //creates a list of timelineEvents which can be nullable
    val timelineEvents: List<TimelineEvent> =launchJsonObj["timeline"]?.jsonArray?.mapNotNull { event ->
            val eventObj = event.jsonObject
            val abbrev = eventObj["type"]?.jsonObject?.get("abbrev")?.jsonPrimitive?.content
            val relativeTime = eventObj["relative_time"]?.jsonPrimitive?.content

                if (abbrev != null && relativeTime != null) {
                    TimelineEvent(abbrev, relativeTime)
                } else {
                    null
                }
            }
            ?: emptyList()

    val programType=when (launchJsonObj["program"]?.jsonArray?.firstOrNull()?.jsonObject?.get("type")?.jsonObject?.get("id")?.jsonPrimitive?.int) {
        1-> ProgramType.UNKNOWN
        2-> ProgramType.HUMAN_SPACEFLIGHT
        3-> ProgramType.COMMUNICATION_CONSTELLATION
        4-> ProgramType.NAVIGATION_CONSTELLATION
        5-> ProgramType.EXPLORATION
        6-> ProgramType.EDUCATION
        7-> ProgramType.TECHNOLOGY
        8 -> ProgramType.EARTH_OBSERVATION
        else -> ProgramType.UNKNOWN
    }

    val officialStreamUrl = launchJsonObj["vid_urls"]
        ?.jsonArray
        ?.lastOrNull()
        ?.jsonObject
        ?.takeIf { video ->
            video["type"]
                ?.jsonObject
                ?.get("id")
                ?.jsonPrimitive
                ?.intOrNull == 1
        }
        ?.get("url")
        ?.jsonPrimitive
        ?.contentOrNull
//adds all the variables into an object to return
    val toReturn=Launch(
        launchID = id,
        launchURL=url,
        missionName = missionName,
        padName =padName ,
        agencyID = agencyID,
        rocketID = rocketID,
        rocketVersionID = rocketVersionID,
        launchTimeUTC = launchTimeUTC,
        launchTimePadLocal = launchTimePad,
        reusableParts = stageLandings,
        success = success,
        windowOpen = windowOpenUTC,
        windowClose = windowCloseUTC ,
        imageUrl =  imageUrl,
        imageCredit =  imageCredit ,
        licenseType =  imageLicenseType,
        weatherProbability =  weatherProbability,
        agencyName = agencyName ,
        rocketName =  rocketName ,
        rocketVariantName =  rocketVariantName,
        description =  description,
        orbit =  orbit,
        padCords =  padCords,
        padLocation =  padLocation,
        padCountry =  padCountry,
        padTimeZone =padTimeZoneID,
        netPrecision = netPrecision,
        liveStreamUrl = officialStreamUrl,
        weatherConcerns = weatherConcerns,
        timeline=timelineEvents,
        precisionAbbrev = precisionAbbrev,
        programType = programType
    )
     return toReturn

}

//check if agency,Rocket,version need a record added to and if they are new adds them to the db
suspend fun checkForOtherEntities(launchJsonEle: JsonElement,launch: Launch,scheduleDao: ScheduleDao){

    var lastUpdated=scheduleDao.getMetaData("lastUpdated")
    println("In Comparison to $lastUpdated")
    if (lastUpdated == null) {lastUpdated=(LocalDateTime.of(2025,12,23,0,0)).toString() } //A Set date for if the Db is reset

    if (launch.agencyID!=null ) {
        if (!scheduleDao.doesAgencyExist(launch.agencyID) || scheduleDao.doesAgencyNeedUpdating(launch.agencyID, lastUpdated)) {
            println("Agency: ${launch.agencyName} does NOT exist or needs updating ")
            scheduleDao.upsertAgency(parseAgency(launchJsonEle,scheduleDao))

        } else {
            println("Agency: ${launch.agencyName} ALREADY exists and is updated")
        }
    }
    if (launch.rocketID!=null){
        if (!scheduleDao.doesRocketExist(launch.rocketID)|| scheduleDao.doesRocketNeedUpdating(launch.rocketID, lastUpdated)) {
            println("Rocket: ${launch.rocketName} does NOT exist or needs updating")
            scheduleDao.upsertRocket(parseRocket(launchJsonEle,scheduleDao))
        }else{
            println("Rocket: ${launch.rocketName} ALREADY exists and is updated")
        }
    }

    if (launch.rocketVersionID!=null){
        if (!scheduleDao.doesRocketVersionExist(launch.rocketVersionID)|| scheduleDao.doesRocketVersionNeedUpdating(launch.rocketVersionID, lastUpdated)) {
            println("Version: ${launch.rocketVariantName} does NOT exist or needs updating")
            scheduleDao.upsertRocketVersion(parseRocketVersion(launchJsonEle,scheduleDao))
        }else{
            println("Version: ${launch.rocketVariantName} ALREADY exists and is updated")
        }
    }


}

suspend fun checkForReuse(launchJsonEle: JsonElement,launch: Launch,scheduleDao: ScheduleDao){
    var lastUpdated=scheduleDao.getMetaData("lastUpdated")
    if (lastUpdated == null) {lastUpdated=(LocalDateTime.of(2025,12,23,0,0)).toString() } //some one-time variable will only be used when Database is reset in TESTing

    if (!launch.reusableParts.isEmpty()) {
        for (reusablePart in launch.reusableParts) {
            if (reusablePart.reuseID != null) {
                if (!scheduleDao.doesReuseExist(reusablePart.reuseID!!)|| scheduleDao.doesReuseNeedUpdating(reusablePart.reuseID!!, lastUpdated)) {
                    println("Reusable: ${reusablePart.reuseName} does NOT exist or needs updating")
                    scheduleDao.upsertReuse(parseReuse(launchJsonEle, reusablePart,scheduleDao))


                }else{
                    println("Reusable:${reusablePart.reuseName} ALREADY exists and is updated")
                }
                val crossRef = LaunchReuseCrossRef(
                    launchID = launch.launchID,
                    reuseID = reusablePart.reuseID!!
                )
                scheduleDao.upsertLaunchReuseCrossRef(crossRef)
            }
        }
    }

}

suspend fun parseAgency(launchJsonEle: JsonElement,scheduleDao: ScheduleDao): Agency {
    val agencyJsonObj=launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject  //turns the element into an object
    val countries= agencyJsonObj?.get("country")?.jsonArray
    val countriesList : List<String> = countries?.mapNotNull { country ->
       country.jsonObject["name"]?.jsonPrimitive?.content
    }?: emptyList()
    val administratorPair= agencyJsonObj?.get("administrator")?.jsonPrimitive?.content
    val leaderType=when(administratorPair?.substringBefore(":")){
        "CEO" -> LeaderType.CEO
        "Administrator" -> LeaderType.ADMINISTRATOR
        "Chairman" -> LeaderType.CHAIRMAN
        "President" -> LeaderType.PRESIDENT
        "Chairman & President" -> LeaderType.CHAIRMAN_PRESIDENT
        "Director General" -> LeaderType.DIRECTOR_GENERAL
        "Founder"-> LeaderType.FOUNDER
        else -> LeaderType.UnknownLeaderType
    }
    val leaderName= administratorPair?.substringAfter(":")
    val socialUrls=agencyJsonObj?.get("social_media_links")?.jsonArray?.toList()
    val id428 = socialUrls
        ?.firstOrNull { it.jsonObject["id"]?.jsonPrimitive?.int == 428 }
        ?.jsonObject
        ?.get("url")?.jsonPrimitive?.content

    val id429 = socialUrls
        ?.firstOrNull { it.jsonObject["id"]?.jsonPrimitive?.int == 429 }
        ?.jsonObject
        ?.get("url")?.jsonPrimitive?.content

    val toReturn = Agency(//Gets all parts for the Object from the JSON
        agencyID = agencyJsonObj?.get("id")?.jsonPrimitive?.content!!,
        name =   agencyJsonObj["name"]?.jsonPrimitive?.content,
        launchNumber = agencyJsonObj["total_launch_count"]?.jsonPrimitive?.intOrNull ?: 0,
        countries =  countriesList,
        description = agencyJsonObj["description"]?.jsonPrimitive?.content,
        leader = leaderName,
        leaderType = leaderType,
        founded = agencyJsonObj["founding_year"]?.takeIf { it !is JsonNull }?.jsonPrimitive?.intOrNull?: 0,
        logoURL = (agencyJsonObj["logo"] as? JsonObject)?.get("image_url")?.let { it as? JsonPrimitive }?.contentOrNull,
        websiteURL = agencyJsonObj["info_url"]?.jsonPrimitive?.content,
        wikiURL =  agencyJsonObj["wiki_url"]?.jsonPrimitive?.content,
        socialsURL = id428,
        youtubeURL = id429,
        lastUpdated= scheduleDao.getMetaData("lastUpdated")?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    )
    println("      $toReturn")//LOG
    return toReturn
}

suspend fun parseRocket(launchJsonEle: JsonElement,scheduleDao: ScheduleDao): Rocket {
    val rocketJsonObj=launchJsonEle.jsonObject["rocket"]?.jsonObject  //turns the element into an object
    if ( rocketJsonObj?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject!=null) {


        val toReturn = Rocket(//Gets all parts for the Object from the JSON
            rocketID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get(
                "id"
            )?.jsonPrimitive?.content!!,
            name = rocketJsonObj.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get(
                "name"
            )?.jsonPrimitive?.content,
            agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
            agencyName = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("name")?.jsonPrimitive?.content,
            imageURL = (rocketJsonObj["configuration"] as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("image_url")?.jsonPrimitive?.content,
            imageThumbURL = (rocketJsonObj["configuration"] as? JsonObject)?.get("image")
                ?.let { it as? JsonObject }?.get("thumbnail_url")?.jsonPrimitive?.content,
            imageCredit = (rocketJsonObj["configuration"] as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("credit")?.let { it as? JsonPrimitive }?.contentOrNull,
            licenseType = (rocketJsonObj["configuration"] as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("license")?.let { it as? JsonObject }?.get("name")?.let { it as? JsonPrimitive }?.contentOrNull,
            desc = rocketJsonObj["configuration"]?.jsonObject?.get("description")?.jsonPrimitive?.content,
            lastUpdated = scheduleDao.getMetaData("lastUpdated")
                ?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
        )
        println("      $toReturn")//LOG
        return toReturn
    }else{
        val toReturn = Rocket(
            rocketID = "NoFamily${rocketJsonObj?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content}",
            name = rocketJsonObj?.get("configuration")?.jsonObject?.get("full_name")?.jsonPrimitive?.content,
            agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
            agencyName = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("name")?.jsonPrimitive?.content,
            imageURL = (rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("image_url")?.jsonPrimitive?.content,
            imageThumbURL = (rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")
                ?.let { it as? JsonObject }?.get("thumbnail_url")?.jsonPrimitive?.content,
            imageCredit = (rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("credit")?.let { it as? JsonPrimitive }?.contentOrNull,
            licenseType = (rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }
                ?.get("license")?.let { it as? JsonObject }?.get("name")?.let { it as? JsonPrimitive }?.contentOrNull,
            desc = rocketJsonObj?.get("configuration")?.jsonObject?.get("description")?.jsonPrimitive?.content,
            lastUpdated = scheduleDao.getMetaData("lastUpdated")
                ?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
        )

        println("      $toReturn")//LOG
        return toReturn

    }
}

suspend fun parseRocketVersion(launchJsonEle: JsonElement,scheduleDao: ScheduleDao): RocketVersion {
    val rocketVersionJsonObj=launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject  //turns the element into an object

    val rocketID=if ( rocketVersionJsonObj?.get("families")?.jsonArray?.lastOrNull()?.jsonObject!=null){
        rocketVersionJsonObj["families"]?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content
    }else{
        "NoFamily${rocketVersionJsonObj?.get("id")?.jsonPrimitive?.content!!}"
    }


    val toReturn =RocketVersion(//Gets all parts for the Object from the JSON
        rocketVersionID = rocketVersionJsonObj?.get("id")?.jsonPrimitive?.content!!,
        name = rocketVersionJsonObj["full_name"]?.jsonPrimitive?.content,
        active = rocketVersionJsonObj["active"]?.jsonPrimitive?.boolean,
        launches = rocketVersionJsonObj["total_launch_count"]?.jsonPrimitive?.int,
        activeSince = rocketVersionJsonObj["maiden_flight"]?.takeIf { it !is JsonNull }?.jsonPrimitive?.content?.let(LocalDate::parse),
        rocketID = rocketID,
        agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
        diameterMe = rocketVersionJsonObj["diameter"]?.jsonPrimitive?.doubleOrNull,
        lengthMe = rocketVersionJsonObj["length"]?.jsonPrimitive?.doubleOrNull,
        launchCost = rocketVersionJsonObj["launch_cost"]?.jsonPrimitive?.intOrNull,
        launchMass = rocketVersionJsonObj["launch_mass"]?.jsonPrimitive?.doubleOrNull,
        stages = rocketVersionJsonObj["max_stage"]?.jsonPrimitive?.intOrNull,
        leoCapacity = rocketVersionJsonObj["leo_capacity"]?.jsonPrimitive?.doubleOrNull,
        gtoCapacity = rocketVersionJsonObj["gto_capacity"]?.jsonPrimitive?.doubleOrNull,
        liftoffThrust = rocketVersionJsonObj["to_thrust"]?.jsonPrimitive?.doubleOrNull,
        imageURL = rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("image_url")?.jsonPrimitive?.content,
        imageThumbURL =rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("thumbnail_url")?.jsonPrimitive?.content,
        imageCredit =rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("credit")?.let { it as? JsonPrimitive }?.contentOrNull,
        licenseType=rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("license")?.let { it as? JsonObject }?.get("name")?.let { it as? JsonPrimitive }?.contentOrNull,
        lastUpdated=scheduleDao.getMetaData("lastUpdated")?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    )
    println("      $toReturn")//LOG
    return toReturn
}

suspend fun parseReuse(launchJsonEle: JsonElement, reusablePart: Reusable, scheduleDao: ScheduleDao): Reuse {
    val status: StatusReuse
    var imageUrl:String?=null
    var thumbnailUrl:String?=null
    var imageCredit:String?=null
    var imageLicence:String?=null

    when (reusablePart.reuseType) {//parses the json to get the data from the launches or the spacecraft depending on the reuse type
        ReuseType.BOOSTER -> {val statusID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("status")
                                ?.jsonObject?.get("id")?.jsonPrimitive?.content
            status = when (statusID) {
                "1" -> StatusReuse.RETIRED
                "2" -> StatusReuse.EXPENDED
                "3" -> StatusReuse.ACTIVE
                "4" -> StatusReuse.LOST
                "5" -> StatusReuse.SCRAPPED
                "6" -> StatusReuse.CONVERTED
                "7" -> StatusReuse.DESTROYED
                else -> StatusReuse.UNKNOWN
            }
            val image =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("image") as? JsonObject

             imageUrl = image?.get("image_url")?.jsonPrimitive?.content
             thumbnailUrl = image?.get("thumbnail_url")?.jsonPrimitive?.content
             imageCredit = image?.get("credit")?.jsonPrimitive?.content
             imageLicence = image?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content
        }
        ReuseType.SHIP_CAPSULE -> {val statusID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("spacecraft_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("spacecraft")?.jsonObject?.get("status")
                                    ?.jsonObject?.get("id")?.jsonPrimitive?.content
            status = when (statusID) {
                "1" -> StatusReuse.ACTIVE
                "2" -> StatusReuse.RETIRED
                "3" -> StatusReuse.DESTROYED
                "4" -> StatusReuse.EXPENDED
                else -> StatusReuse.UNKNOWN
            }
            val image =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("spacecraft_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("image") as? JsonObject

            imageUrl = image?.get("image_url")?.jsonPrimitive?.content
            thumbnailUrl = image?.get("thumbnail_url")?.jsonPrimitive?.content
            imageCredit = image?.get("credit")?.jsonPrimitive?.content
            imageLicence = image?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content

        }
        else -> {
            status = StatusReuse.UNKNOWN

        }


    }


    val toReturn=Reuse(//Gets all parts for the Object from the JSON
        reuseID = reusablePart.reuseID!!,
        name = reusablePart.reuseName,
        activeSince = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get(
            "launcher"
        )?.jsonObject?.get("first_launch_date")?.jsonPrimitive?.content?.takeIf { it != "null" }
            ?.let { Instant.parse(it).atZone(ZoneOffset.UTC).toLocalDate() },
        versionID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content!!,
        rocketID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        rocketName = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("name")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
        agencyName = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("name")?.jsonPrimitive?.content,

        reuseType = reusablePart.reuseType,
        status = status,
        lastUpdated = scheduleDao.getMetaData("lastUpdated")
            ?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
        imageURL = imageUrl,
        imageThumbURL =thumbnailUrl ,
        imageCredit = imageCredit,
        licenseType =imageLicence
    )
    println("      $toReturn")//LOG
    return toReturn
}


