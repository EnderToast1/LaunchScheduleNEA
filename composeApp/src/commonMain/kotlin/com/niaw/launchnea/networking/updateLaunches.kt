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

suspend fun updateLaunchData(scheduleDao: ScheduleDao){


    //finds the last time the database updated and then updates it so it set to now()
    var databaseLastUpdated= scheduleDao.getMetaData("lastUpdated")//Gets lastUpdates time
        println("Last Updated: $databaseLastUpdated")//LOG

    //TODO remove 30mins from databaseLastUpdated
    scheduleDao.upsertMetadata(MetaData("lastUpdated",ZonedDateTime.now(ZoneOffset.UTC).toString()))  //updates LastUpdated to now in UTC
    if (databaseLastUpdated == null) {//if it doesn't exist set to some time SHOULD BE TEMP AS WHEN IN PRODUCTION WILL ALWAYS EXIST
        databaseLastUpdated=(LocalDateTime.of(2025,12,23,0,0)).toString()  //some one-time variable will only be used when Database is reset in TESTing
    }
    val TEMPdatabaseLastUpdated= LocalDateTime.of(2025,12,23,0,0)   //TEMP for TESTing and DEVelopment
    TEMPdatabaseLastUpdated.minusDays(1).toString() //minuses one day and turns it into ISO format TEMP for TESTing and DEVelopment
//TODO change lldev to ll only for production not dev
    println("\n\nPast https://lldev.thespacedevs.com/2.3.0/launches/previous/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated")
    //Updates previous launches
    val pastCallResponse=APIFetchRequest("https://lldev.thespacedevs.com/2.3.0/launches/previous/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated") //passes the url as a string with the last updated dateTime as part of it in the API filter system
    responseToDatabase(scheduleDao,pastCallResponse)        //parses the response and adds each launch to the database (inside the function it checks for other tables that need adding to)
    while ((pastCallResponse.count>=100) and (pastCallResponse.next!=null)) {
        println("NEXT PAST RESPONSE")
        responseToDatabase(scheduleDao,APIFetchRequest(pastCallResponse.next))
    }

    println("\n\nUpcoming https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated")//LOG
    //Update upcoming launches
    var upcomingCallResponse=APIFetchRequest("https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?mode=detailed&limit=100&last_updated__gte=$TEMPdatabaseLastUpdated") //passes the url as a string into the API and also gives it the lastUpdated string filter
    responseToDatabase(scheduleDao,upcomingCallResponse)   //parses the response and adds each launch to the database (inside the function it checks for other tables that need adding to)
    while ((upcomingCallResponse.count>=100) and (upcomingCallResponse.next!=null)) {
        println("NEXT UPCOMING RESPONSE")
        upcomingCallResponse=(APIFetchRequest(upcomingCallResponse.next))
        responseToDatabase(scheduleDao,upcomingCallResponse)
    }
}


//parses the response and adds each launch to the database (also calls for a function to check for other tables that need adding to)
private suspend fun responseToDatabase(scheduleDao: ScheduleDao, callResponse: ResponseData) {
    println("Count: ${callResponse.count}\n Next: ${callResponse.next}\n\n")//LOG
    //checks if there was an error reported when getting the response
    if (callResponse.errorType == null) {

        //if there was no error it sets it to a JsonArray and calls a function to parse each launch into a object then adds to db
        val pastLaunchesJsonArray = callResponse.data
        pastLaunchesJsonArray?.forEachIndexed {i, launchJson ->              //goes though each launch in the return from the API
            val launch = parseLaunch(launchJson)         //first it creates a launch object for the launch
            println("\n\nLaunch number ${i+1}: \n$launch")//LOG
            if (launch != null) {
                checkForOtherEntities(launchJson,launch, scheduleDao)
                scheduleDao.upsertLaunch(launch) //adds to database if not null




            }
        }
    }
    else {//if there is an error it logs it
        println(callResponse.errorType)//LOG
    }
}

//parse each launch element into a launch object
suspend fun parseLaunch(launchJsonEle: JsonElement,): Launch? {
    val launchJsonObj=launchJsonEle.jsonObject  //turns the element into an object
    val id= launchJsonObj["id"]?.jsonPrimitive?.content ?: return null  //finds the tag of "id" then gets the content after it is made JsonPrimitive if it is null, does not exist or canot be made primitive it is set to null  
    val url = launchJsonObj["url"]?.jsonPrimitive?.content
    val missionName= (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padName= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val agencyID= launchJsonObj["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketVersionID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content
    val launchTimeUTC: ZonedDateTime? =launchJsonObj["net"]?.jsonPrimitive?.content?.let { runCatching { ZonedDateTime.parse(it) }.getOrNull() }
    val padTimeZoneID = (launchJsonObj["pad"] as? JsonObject)?.get("location")?.jsonObject?.get("timezone_name")?.jsonPrimitive?.content?.takeIf { it.isNotBlank() } // <- ignore empty strings
    val launchTimePad: ZonedDateTime? = if (launchTimeUTC != null && padTimeZoneID != null) {runCatching { launchTimeUTC.withZoneSameInstant(ZoneId.of(padTimeZoneID)) }.getOrNull()//gets the pad launch timezone id and the UTC launch time and the applys it if one or both is null or the timezone id is invalid it returns the UTC time
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

// need to add launchLiveStream link  TODO
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


    //TODO currently will not update only make, need to add update but ideally without parsing each-one  TODO Need to add last called as a prameter for each object and add a check for if less then lastupdated() in the of selection
    if (launch.agencyID!=null ) {
        if (!scheduleDao.doesAgencyExists(launch.agencyID)) {
            println("Agency: ${launch.agencyName} does not exist")
            scheduleDao.upsertAgency(parseAgency(launchJsonEle))
        }else{
            println("Agency:${launch.agencyName} already exists")
        }
    }
    if (launch.rocketID!=null){
        if (!scheduleDao.doesRocketExists(launch.rocketID)) {
            println("Rocket: ${launch.rocketName}, ${launch.rocketID} does not exist")
            scheduleDao.upsertRocket(parseRocket(launchJsonEle))
        }else{
            println("Rocket: ${launch.rocketName} already exists")
        }
    }
    if (launch.rocketVersionID!=null){
        if (!scheduleDao.doesRocketVersionExists(launch.rocketVersionID)) {
            println("Version: ${launch.rocketVariantName} does not exist")
            scheduleDao.upsertRocketVersion(parseRocketVersion(launchJsonEle))
        }else{
            println("Version: ${launch.rocketVariantName} already exists")
        }
    }
    if (!launch.reusableParts.isEmpty()) {
        for (reusablePart in launch.reusableParts) {
            if (reusablePart.reuseID != null) {
                if (!scheduleDao.doesReuseExists(reusablePart.reuseID!!)) {
                    println("Reusable: ${reusablePart.reuseName} does not exist")
                    scheduleDao.upsertReuse(parseReuse(launchJsonEle, reusablePart))
                }else{
                    println("Reusable:${reusablePart.reuseName} already exists")
                }
            }
        }
    }

}

fun parseAgency(launchJsonEle: JsonElement): Agency {
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
    logoURL = agencyJsonObj["logo"]?.jsonObject?.get("image_url")?.jsonPrimitive?.content,
        websiteURL = agencyJsonObj["info_url"]?.jsonPrimitive?.content,
        wikiURL =  agencyJsonObj["wiki_url"]?.jsonPrimitive?.content,
        socialsURL =null ,   //TODO seems to be broken
        youtubeURL =null    //TODO seems to be broken
    )
    println(toReturn)//LOG
    return toReturn
}

fun parseRocket(launchJsonEle: JsonElement): Rocket {
    val rocketJsonObj=launchJsonEle.jsonObject["rocket"]?.jsonObject  //turns the element into an object

    val toReturn = Rocket(
        rocketID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content!!,
        name = rocketJsonObj?.get("configuration")?.jsonObject?.get("name")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        imageURL = rocketJsonObj?.get("configuration")?.jsonObject?.get("image")?.jsonObject?.get("image_url")?.jsonPrimitive?.content,
        imageCredit= rocketJsonObj?.get("configuration")?.jsonObject?.get("image")?.jsonObject?.get("credit")?.jsonPrimitive?.content,
        licenseType= rocketJsonObj?.get("configuration")?.jsonObject?.get("image")?.jsonObject?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content,
        desc = rocketJsonObj?.get("configuration")?.jsonObject?.get("description")?.jsonPrimitive?.content
    )
    println(toReturn)//LOG
    return toReturn
}

fun parseRocketVersion(launchJsonEle: JsonElement): RocketVersion {
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
        imageURL = rocketVersionJsonObj["image"]?.jsonObject?.get("image_url")?.jsonPrimitive?.content,
        imageCredit= rocketVersionJsonObj["image"]?.jsonObject?.get("credit")?.jsonPrimitive?.content,
        licenseType= rocketVersionJsonObj["image"]?.jsonObject?.get("license")?.jsonObject?.get("name")?.jsonPrimitive?.content
    )
    println(toReturn)//LOG
    return toReturn
}

fun parseReuse(launchJsonEle: JsonElement, reusablePart: Reusable): Reuse {
    val status: StatusReuse
    if (reusablePart.reuseType== ReuseType.BOOSTER){
        val statusID= launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("launcher_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("status")?.jsonObject?.get("id")?.jsonPrimitive?.content
        status=when(statusID){
            "1"-> StatusReuse.RETIRED
            "2"-> StatusReuse.EXPENDED
            "3"-> StatusReuse.ACTIVE
            "4"-> StatusReuse.LOST
            "5"-> StatusReuse.SCRAPPED
            "6"-> StatusReuse.CONVERTED
            "7"-> StatusReuse.DESTROYED
            else -> StatusReuse.UNKNOWN
        }
    }else if (reusablePart.reuseType== ReuseType.SHIP_CAPSULE){
        val statusID= launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("spacecraft_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("spacecraft")?.jsonObject?.get("status")?.jsonObject?.get("id")?.jsonPrimitive?.content
        status=when (statusID) {
            "1"-> StatusReuse.ACTIVE
            "2"-> StatusReuse.RETIRED
            "3"-> StatusReuse.DESTROYED
            "4"-> StatusReuse.EXPENDED
            else -> StatusReuse.UNKNOWN
        }
    }else{
        status= StatusReuse.UNKNOWN
    }


    val toReturn=Reuse(
        reuseID = reusablePart.reuseID!!,
        name = reusablePart.reuseName,
        activeSince = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("launch_stage")?.jsonArray?.firstOrNull()?.jsonObject?.get("launcher")?.jsonObject?.get("first_launch_date")?.jsonPrimitive?.content?.let { LocalDate.parse(it) },
        versionID = launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content!!,
        rocketID =launchJsonEle.jsonObject["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("families")?.jsonArray?.lastOrNull()?.jsonObject?.get("id")?.jsonPrimitive?.content,
        agencyID = launchJsonEle.jsonObject["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content,
        reuseType = reusablePart.reuseType,
        status =status
    )
    println(toReturn)//LOG
    return toReturn
}
