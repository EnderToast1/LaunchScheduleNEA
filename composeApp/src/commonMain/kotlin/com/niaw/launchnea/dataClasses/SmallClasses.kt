package com.niaw.launchnea.DataClasses

//a set of short classes that are needed
enum class ResultsState {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILURE,
    UNKNOWN
    }

enum class LeaderType(val displayName: String) {
    CEO("CEO"),
    Admin("Admin"),
    UnknownLeaderType("Unknown leader type");
}

enum class ReuseType{
    BOOSTER,
    SHIP,
    CAPSULE,
    UNKNOWN
}


data class Location(
    val latitude: Double,
    val longitude: Double
)


data class Reusable(
    var isReusable: Boolean,
    var reuseVehicleID: String,
    var landingLocationID: String,
    var status: ResultsState
)
