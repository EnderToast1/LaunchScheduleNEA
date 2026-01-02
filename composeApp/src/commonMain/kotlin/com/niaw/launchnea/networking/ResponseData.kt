package com.niaw.launchnea.networking

import kotlinx.serialization.json.JsonArray

data class ResponseData(
    val data: JsonArray?=null,
    val errorType: NetworkError?= null,
)
