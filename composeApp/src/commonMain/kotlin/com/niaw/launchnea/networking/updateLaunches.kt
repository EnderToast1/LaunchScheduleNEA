package com.niaw.launchnea.networking

import com.niaw.launchnea.LandingType
import com.niaw.launchnea.Location
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.Status
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
suspend fun updateLaunchData(scheduleDao: ScheduleDao){
    println("Past")
    //Past launches
    val lastCallForPast= LocalDateTime.of(2025,12,23,0,0)   //Need to implement finding last call date for past
    lastCallForPast.minusDays(1).toString() //minuses one day and turns it into ISO format
    val pastCallResponse=APIFetchRequest("https://lldev.thespacedevs.com/2.3.0/launches/previous/?mode=detailed&limit=10&last_updated__lte=$lastCallForPast") //passes the url as a string with the date as part of it in the API filter system
    if (pastCallResponse.errorType == null){
        val pastLaunchesJsonArray=pastCallResponse.data
        pastLaunchesJsonArray?.forEach { launchJson ->              //goes though each launch in the return from the API
            val launch =parseLaunch(launchJson)         //first it creates a launch object for the launch
            if (launch != null) {
               // scheduleDao.upsertLaunch(launch) //adds to database

                println(launch.toString())//TEST
                println("NEXT")//TEST
            }
        }
    }else{
        println(pastCallResponse.errorType)
    }
    //edit lastCallForPast to now

    println("Upcoming")
    //upcoming launches
    val upcomingCallResponse=APIFetchRequest("https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?mode=detailed&limit=150") //passes the url as a string into the API
    if (upcomingCallResponse.errorType == null) {
        val upcomingLaunchesJsonArray = upcomingCallResponse.data
        upcomingLaunchesJsonArray?.forEachIndexed { index, launchJson ->              //goes though each launch in the return from the API
            println("URL " + launchJson.jsonObject["url"]?.jsonPrimitive?.content)
            val launch = parseLaunch(launchJson)         //first it creates a launch object for the launch
            if (launch != null) {
                //scheduleDao.upsertLaunch(launch) //adds to database
                println(launch.toString())//TEST
                println("NEXT $index")//TEST
            }
        }
    }else{
        println(upcomingCallResponse.errorType)
    }
}

fun parseLaunch(launchJsonEle: JsonElement): Launch? {
    val launchJsonObj=launchJsonEle.jsonObject
    val id= launchJsonObj["id"]?.jsonPrimitive?.content ?: return null
    val url = launchJsonObj["url"]?.jsonPrimitive?.content
    val missionName= (launchJsonObj["mission"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val padName= (launchJsonObj["pad"] as? JsonObject)?.jsonObject?.get("name")?.jsonPrimitive?.content
    val agencyID= launchJsonObj["launch_service_provider"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketID= launchJsonObj["rocket"]?.jsonObject?.get("id")?.jsonPrimitive?.content
    val rocketVersionID= launchJsonObj["rocket"]?.jsonObject?.get("configuration")?.jsonObject?.get("id")?.jsonPrimitive?.content
    val launchTimeUTC: ZonedDateTime? =launchJsonObj["net"]?.jsonPrimitive?.content?.let { runCatching { ZonedDateTime.parse(it) }.getOrNull() }
    val padTimeZoneID = (launchJsonObj["pad"] as? JsonObject)?.get("location")?.jsonObject?.get("timezone_name")?.jsonPrimitive?.content?.takeIf { it.isNotBlank() } // <- ignore empty strings
    val launchTimePad: ZonedDateTime? = if (launchTimeUTC != null && padTimeZoneID != null) {
        runCatching { launchTimeUTC.withZoneSameInstant(ZoneId.of(padTimeZoneID)) }.getOrNull()
    } else {
        launchTimeUTC
    }
    val launcherStages = launchJsonObj["rocket"]
        ?.jsonObject?.get("launcher_stage")
        ?.jsonArray
    val boosterLandings: List<Reusable> =
        launcherStages?.mapNotNull { stage ->
                val landingLocation =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull
                val landingAbbrev   =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
                val landingType     =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("type")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
                val landingTypeInForm= when (landingType) {
                    "ASDS" -> LandingType.ASDS
                    "RTLS" -> LandingType.RTLS
                    "PCL" -> LandingType.PCL
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
                        status =statusReuse
                    )
            }
            ?: emptyList()

    val spacecraftStages = launchJsonObj["rocket"]
        ?.jsonObject?.get("spacecraft_stage")
        ?.jsonArray
    val capsulesLandings:List<Reusable> =
        spacecraftStages?.mapNotNull { stage ->
            val capLandingLocation =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull
            val capLandingAbbrev   =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("landing_location")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
            val capLandingType     =stage.jsonObject["landing"]?.takeIf { it != JsonNull }?.jsonObject?.get("type")?.takeIf { it != JsonNull }?.jsonObject?.get("abbrev")?.jsonPrimitive?.contentOrNull
            val capLandingTypeInForm= when (capLandingType) {
                "ASDS" -> LandingType.ASDS
                "RTLS" -> LandingType.RTLS
                "PCL" -> LandingType.PCL
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
                status =capStatusReuse
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

// need to add launchLiveStream link
    val liveStreamURL="HOLD TEST"

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
    )

     return toReturn

}