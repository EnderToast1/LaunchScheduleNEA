package com.niaw.launchnea.networking

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive




suspend fun requestNumber(){
    val urlString=("https://ll.thespacedevs.com/2.3.0/api-throttle/")
    val response = httpClient.get(urlString)
    val jsonElement = Json.parseToJsonElement(response.bodyAsText())

    println("Used calls: ${jsonElement.jsonObject["current_use"]?.jsonPrimitive?.int}")
    println("Next call time: ${jsonElement.jsonObject["next_use_secs"]?.jsonPrimitive?.int}")
    }