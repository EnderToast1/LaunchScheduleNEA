package com.niaw.launchnea.networking

import com.niaw.launchnea.LandingType
import com.niaw.launchnea.LeaderType
import com.niaw.launchnea.Location
import com.niaw.launchnea.ProgramType
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.ReuseType
import com.niaw.launchnea.Status
import com.niaw.launchnea.StatusReuse
import com.niaw.launchnea.TimelineEvent
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.MetaData
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.collections.List
import java.time.format.DateTimeFormatter



suspend fun updateLaunchData(scheduleDao: ScheduleDao){

    //finds the last time the database updated and then updates it so it set to now()
    var databaseLastUpdated= scheduleDao.getMetaData("lastUpdated")//Gets lastUpdates time
        println("Last Updated: $databaseLastUpdated")//LOG    //TODO remove 30mins from databaseLastUpdated

    scheduleDao.upsertMetadata(MetaData("lastUpdated",ZonedDateTime.now(ZoneOffset.UTC).toString()))  //updates LastUpdated to now in UTC
    if (databaseLastUpdated == null) {//if it doesn't exist set to some time SHOULD BE TEMP AS WHEN IN PRODUCTION WILL ALWAYS EXIST
        databaseLastUpdated=(LocalDateTime.of(2025,12,23,0,0)).toString()  //some one-time variable will only be used when Database is reset in TESTing
    }

    //TODO TEMP
    val TEMPdatabaseLastUpdated= LocalDateTime.of(2025,10,23,0,0)
    TEMPdatabaseLastUpdated.minusDays(1).toString()

    //TODO change lldev to ll only for production not dev
    val pastCallApiUrl="https://lldev.thespacedevs.com/2.3.0/launches/previous/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated"
    val upcomingCallApiUrl="https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated"

    //Updates previous launches
    println("\n\nPast $pastCallApiUrl")
    var pastCallResponse=APIFetchRequest(pastCallApiUrl) //passes the url as a string with the last updated dateTime as part of it in the API filter system
    println("NEXT PAST RESPONSE: 1")
    responseToDatabase(scheduleDao,pastCallResponse)        //parses the response and adds each launch to the database (inside the function it checks for other tables that need adding to)
    var i=0
    while ((pastCallResponse.count>=100) and (pastCallResponse.next!=null)) {
        i++
        println("NEXT PAST RESPONSE: ${i+1}")
        pastCallResponse=APIFetchRequest(pastCallResponse.next)
        pastCallResponse.count-=(i*100)
        println("TEST: ${pastCallResponse.count}")
        responseToDatabase(scheduleDao,pastCallResponse)
    }

    println("\n\nUpcoming $upcomingCallApiUrl")//LOG
    //Update upcoming launches
    var upcomingCallResponse=APIFetchRequest(upcomingCallApiUrl) //passes the url as a string into the API and also gives it the lastUpdated string filter
    responseToDatabase(scheduleDao,upcomingCallResponse)   //parses the response and adds each launch to the database (inside the function it checks for other tables that need adding to)
    i=0
    while ((upcomingCallResponse.count>=100) and (upcomingCallResponse.next!=null)) {
        i++
        println("NEXT UPCOMING RESPONSE")
        upcomingCallResponse=(APIFetchRequest(upcomingCallResponse.next))
        upcomingCallResponse.count-=(i*100)
        responseToDatabase(scheduleDao,upcomingCallResponse)
    }
}


//parses the response and adds each launch to the database (also calls for a function to check for other tables that need adding to)
suspend fun responseToDatabase(scheduleDao: ScheduleDao, callResponse: ResponseData) {

    //checks if there was an error reported when getting the response
    if (callResponse.errorType == null) {
        println("Count: ${callResponse.count}\n")//LOG

        //if there was no error it sets it to a JsonArray and calls a function to parse each launch into an object then adds to db
        val launchesJsonArray = callResponse.data
        launchesJsonArray?.forEachIndexed {i, launchJson ->              //goes though each launch in the return from the API
            val launch = parseLaunch(launchJson)         //first it creates a launch object for the launch
            println("\n\nLaunch number ${i+1}: \n$launch")//LOG
            if (launch != null) {
                checkForOtherEntities(launchJson,launch, scheduleDao)
                scheduleDao.upsertLaunch(launch) //adds to database if not null




            }
        }
        println("\n\nNext: ${callResponse.next}\n")
    }else {//if there is an error it logs it
        println(callResponse.errorType)//LOG
    }
    }

//parse each launch element into a launch object
suspend fun parseLaunch(launchJsonEle: JsonElement): Launch? {
    val launchJsonObj=launchJsonEle.jsonObject  //turns the element into an object
    val id= launchJsonObj["id"]?.jsonPrimitive?.content ?: return null  //finds the tag of "id" then gets the content after it is made JsonPrimitive if it is null, does not exist or cannot be made primitive it is set to null
    val url = launchJsonObj["url"]?.jsonPrimitive?.content
    val missionName= (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padName= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val agencyID= launchJsonObj["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketVersionID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content
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
                downrangeDistanceKM = stage.jsonObject["landing"]?.jsonObject?.get("downrange_distance")?.jsonPrimitive?.doubleOrNull,
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
    val imageUrl = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("image_url")?.jsonPrimitive?.content
    val imageCredit = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("credit")?.jsonPrimitive?.content
    val imageLicenseType = (launchJsonObj["image"] as? JsonObject)?.jsonObject?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val weatherProbability = launchJsonObj["probability"]?.jsonPrimitive?.doubleOrNull
    val agencyName = launchJsonObj["launch_service_provider"]?.jsonObject?.get("name")?.jsonPrimitive?.content
    val rocketName = launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val rocketVariantName = launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("full_name")?.jsonPrimitive?.content
    val description = (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("description")?.jsonPrimitive?.content
    val orbit = (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("orbit")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padLat= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("latitude")?.jsonPrimitive?.double
    val padLong= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("longitude")?.jsonPrimitive?.double
    val padCords = Location(padLat,padLong)
    val padLocation = (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("location")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padCountry = (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("country")?.jsonObject?.get("name")?.jsonPrimitive?.content
    val netPrecision = launchJsonObj["net_precision"]?.jsonObject?.get("description")?.jsonPrimitive?.content
    val precisionAbbrev = launchJsonObj["net_precision"]?.jsonObject?.get("abbrev")?.jsonPrimitive?.content
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

//TODO need to add launchLiveStream link
    val liveStreamURL="HOLD TEST"
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
        liveStreamUrl = liveStreamURL,
        weatherConcerns = weatherConcerns,
        timeline=timelineEvents,
        precisionAbbrev = precisionAbbrev,
        programType = programType
    )
     return toReturn

}

//check if agency,rocket,version or reuse need a record added to and if they are new adds them to the db
suspend fun checkForOtherEntities(launchJsonEle: JsonElement,launch: Launch,scheduleDao: ScheduleDao){

    var lastUpdated=scheduleDao.getMetaData("lastUpdated")
    if (lastUpdated == null) {lastUpdated=(LocalDateTime.of(2025,12,23,0,0)).toString() } //some one-time variable will only be used when Database is reset in TESTing

    if (launch.agencyID!=null ) {
        if (!scheduleDao.doesAgencyExists(launch.agencyID) || scheduleDao.doesAgencyNeedUpdating(launch.agencyID, lastUpdated)) {
            println("Agency: ${launch.agencyName} does NOT exist or needs updating")
            scheduleDao.upsertAgency(parseAgency(launchJsonEle,scheduleDao))
        }else{
            println("Agency: ${launch.agencyName} ALREADY exists and is updated")
        }
    }
    if (launch.rocketID!=null){
        if (!scheduleDao.doesRocketExists(launch.rocketID)|| scheduleDao.doesRocketNeedUpdating(launch.rocketID, lastUpdated)) {
            println("Rocket: ${launch.rocketName} does NOT exist or needs updating")
            scheduleDao.upsertRocket(parseRocket(launchJsonEle,scheduleDao))
        }else{
            println("Rocket: ${launch.rocketName} ALREADY exists and is updated")
        }
    }
    if (launch.rocketVersionID!=null){
        if (!scheduleDao.doesRocketVersionExists(launch.rocketVersionID)|| scheduleDao.doesRocketVersionNeedUpdating(launch.rocketVersionID, lastUpdated)) {
            println("Version: ${launch.rocketVariantName} does NOT exist or needs updating")
            scheduleDao.upsertRocketVersion(parseRocketVersion(launchJsonEle,scheduleDao))
        }else{
            println("Version: ${launch.rocketVariantName} ALREADY exists and is updated")
        }
    }
    if (!launch.reusableParts.isEmpty()) {
        for (reusablePart in launch.reusableParts) {
            if (reusablePart.reuseID != null) {
                if (!scheduleDao.doesReuseExists(reusablePart.reuseID!!)|| scheduleDao.doesReuseNeedUpdating(reusablePart.reuseID!!, lastUpdated)) {
                    println("Reusable: ${reusablePart.reuseName} does NOT exist or needs updating")
                    scheduleDao.upsertReuse(parseReuse(launchJsonEle, reusablePart,scheduleDao))
                }else{
                    println("Reusable:${reusablePart.reuseName} ALREADY exists and is updated")
                }
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
        else -> LeaderType.UnknownLeaderType
    }
    val leaderName= administratorPair?.substringAfter(":")

    val toReturn = Agency(
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
        socialsURL =null ,   //TODO seems to be broken
        youtubeURL =null,    //TODO seems to be broken
        lastUpdated=scheduleDao.getMetaData("lastUpdated")?.let{ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)}
    )
    println("      $toReturn")//LOG
    return toReturn
}

suspend fun parseRocket(launchJsonEle: JsonElement,scheduleDao: ScheduleDao): Rocket {
    val rocketJsonObj=launchJsonEle.jsonObject["rocket"]?.jsonObject  //turns the element into an object

    val toReturn = Rocket(
        rocketID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content!!,
        name = rocketJsonObj?.get("configuration")?.jsonObject?.get("name")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        imageURL =(rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }?.get("image_url")?.jsonPrimitive?.content,
        imageCredit =(rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }?.get("credit")?.let { it as? JsonPrimitive }?.contentOrNull,
        licenseType=(rocketJsonObj?.get("configuration") as? JsonObject)?.get("image")?.let { it as? JsonObject }?.get("license")?.let { it as? JsonObject }?.get("name")?.let { it as? JsonPrimitive }?.contentOrNull,
        desc = rocketJsonObj?.get("configuration")?.jsonObject?.get("description")?.jsonPrimitive?.content,
        lastUpdated=scheduleDao.getMetaData("lastUpdated")?.let{ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)}
    )
    println("      $toReturn")//LOG
    return toReturn
}

suspend fun parseRocketVersion(launchJsonEle: JsonElement,scheduleDao: ScheduleDao): RocketVersion {
    val rocketVersionJsonObj=launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject  //turns the element into an object

    val toReturn =RocketVersion(
        rocketVersionID = rocketVersionJsonObj?.get("id")?.jsonPrimitive?.content!!,
        name = rocketVersionJsonObj["full_name"]?.jsonPrimitive?.content,
        active = rocketVersionJsonObj["active"]?.jsonPrimitive?.boolean,
        launches = rocketVersionJsonObj["total_launch_count"]?.jsonPrimitive?.int,
        activeSince = rocketVersionJsonObj["maiden_flight"]?.takeIf { it !is JsonNull }?.jsonPrimitive?.content?.let(LocalDate::parse),
        rocketID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
        diameterMe = rocketVersionJsonObj["diameter"]?.jsonPrimitive?.doubleOrNull,
        lengthMe = rocketVersionJsonObj["length"]?.jsonPrimitive?.doubleOrNull,
        launchCost = rocketVersionJsonObj["launch_cost"]?.jsonPrimitive?.intOrNull,
        launchMass = rocketVersionJsonObj["launch_mass"]?.jsonPrimitive?.doubleOrNull,
        leoCapacity = rocketVersionJsonObj["leo_capacity"]?.jsonPrimitive?.doubleOrNull,
        gtoCapacity = rocketVersionJsonObj["gto_capacity"]?.jsonPrimitive?.doubleOrNull,
        liftoffThrust = rocketVersionJsonObj["to_thrust"]?.jsonPrimitive?.doubleOrNull,
        imageURL = rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("image_url")?.jsonPrimitive?.content,
        imageCredit =rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("credit")?.let { it as? JsonPrimitive }?.contentOrNull,
        licenseType=rocketVersionJsonObj["image"]?.let { it as? JsonObject }?.get("license")?.let { it as? JsonObject }?.get("name")?.let { it as? JsonPrimitive }?.contentOrNull,
        lastUpdated=scheduleDao.getMetaData("lastUpdated")?.let{ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)}
    )
    println("      $toReturn")//LOG
    return toReturn
}

suspend fun parseReuse(launchJsonEle: JsonElement, reusablePart: Reusable, scheduleDao: ScheduleDao): Reuse {
    val status: StatusReuse
    when (reusablePart.reuseType) {
        ReuseType.BOOSTER -> {val statusID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("status")?.jsonObject?.get("id")?.jsonPrimitive?.content
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
        }
        ReuseType.SHIP_CAPSULE -> {val statusID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("spacecraft_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("spacecraft")?.jsonObject?.get("status")?.jsonObject?.get("id")?.jsonPrimitive?.content
            status = when (statusID) {
                "1" -> StatusReuse.ACTIVE
                "2" -> StatusReuse.RETIRED
                "3" -> StatusReuse.DESTROYED
                "4" -> StatusReuse.EXPENDED
                else -> StatusReuse.UNKNOWN
            }
        }
        else -> {
            status = StatusReuse.UNKNOWN
        }
    }


    val toReturn=Reuse(
        reuseID = reusablePart.reuseID!!,
        name = reusablePart.reuseName,
        activeSince = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("launch_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("first_launch_date")?.jsonPrimitive?.content?.let { LocalDate.parse(it) },
        versionID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content!!,
        rocketID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
        reuseType = reusablePart.reuseType,
        status =status,
        lastUpdated=scheduleDao.getMetaData("lastUpdated")?.let{ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)}
    )
    println("      $toReturn")//LOG
    return toReturn
}
