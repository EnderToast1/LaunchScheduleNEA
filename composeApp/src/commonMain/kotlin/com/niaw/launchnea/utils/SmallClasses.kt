package com.niaw.launchnea.utils

import kotlinx.serialization.Serializable

//a set of short classes that are needed
@Serializable
enum class Status {
    GO_FOR_LAUNCH,  //T-0 is confirmed
    TBD,    //Current data is placeholder
    SUCCESS,  //The Launch Vehicle  successfully inserted its payload(s) into the target orbit(s)
    FAILURE,   //Either the launch vehicle did not reach orbit, or the payload(s) failed to separate
    ON_HOLD,  //The countdown has been paused, but the launch can still happen within the launch window
    IN_FLIGHT,  //The launch vehicle has lifted off from the launchpad
    PARTIAL_SUCCESS, //Either the launch vehicle reached orbit but did not deliver its payload in the targeted orbit, or an exceptional event made it impossible to consider the mission a success
    TBC,   //Awaiting official confirmation - current date is known with some certainty
    PAYLOAD_DEPLOYED,  //Deployment of the payload(s) has been confirmed
    STATUS_MISSING,  //Status ID is null so is missing
    UNKNOWN    //Default state
    }
@Serializable
enum class StatusReuse {
    RETIRED,
    EXPENDED,
    ACTIVE,
    LOST,
    SCRAPPED,
    CONVERTED,
    DESTROYED,
    UNKNOWN
}

enum class LeaderType {
    CEO,
    ADMINISTRATOR,
    CHAIRMAN,
    PRESIDENT,
    CHAIRMAN_PRESIDENT,
    UnknownLeaderType
}
@Serializable
enum class ReuseType{
    BOOSTER,
    SHIP_CAPSULE,
    UNKNOWN
}

@Serializable
enum class LandingType{
    ASDS,          //autonomous spaceport drone ship
    RTLS,         //return to launch side
    OCEAN,        //No recovery attempt/splashdown
    PCL,          // parachute landing
    HC,           //Helicopter catch
    HL,          //Horizontal Landing
    ATM,        //Atmospheric burn up
    EXP,        //expended 
    VL,        //vertical landing
    PFL,       //Unpowered landing using parafoil(s)
    
    UNKNOWN
}

@Serializable
data class Location(
    val latitude: Double?=0.0,
    val longitude: Double?=0.0
)

@Serializable
data class Reusable(
    var reuseID: String?=null,
    var reuseName: String?=null,
    var flightNumber: Int?=null,
    var landingLocationAbbr: String?=null,
    var landingLocationName: String?=null,
    var downrangeDistanceKM: Double?=null,
    var landingType: LandingType?=null,
    var reuseType: ReuseType,
    var status: Boolean?= false
)


@Serializable
enum class ProgramType{
    UNKNOWN,
    HUMAN_SPACEFLIGHT,
    COMMUNICATION_CONSTELLATION,
    NAVIGATION_CONSTELLATION,
    EXPLORATION,
    EDUCATION,
    TECHNOLOGY,
    EARTH_OBSERVATION,


}

@Serializable
data class TimelineEvent(
    val abbrev: String,
    val relativeTime: String
)