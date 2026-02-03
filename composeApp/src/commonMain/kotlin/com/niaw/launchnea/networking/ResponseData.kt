package com.niaw.launchnea.networking

import kotlinx.serialization.json.JsonArray

data class ResponseData(
    val data: JsonArray?=null,
    val errorType: NetworkError?= null,
    var count: Int=0,
    val next: String?=null
)
