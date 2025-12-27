package com.niaw.launchnea.Data_Classes

import kotlinx.serialization.Serializable

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


data class Reusables(
    var isReusable: Boolean,
    var reuseVehicleID: String,
    var landingLocationID: String,
    var status: ResultsState
)
