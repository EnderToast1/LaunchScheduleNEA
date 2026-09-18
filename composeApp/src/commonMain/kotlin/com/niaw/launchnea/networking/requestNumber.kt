package com.niaw.launchnea.networking

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive




suspend fun requestNumber() {
    try {
        val urlString = ("https://ll.thespacedevs.com/2.3.0/api-throttle/")
        val response = httpClient.get(urlString)
        val jsonElement = Json.parseToJsonElement(response.bodyAsText())

        println("Used calls: ${jsonElement.jsonObject["current_use"]?.jsonPrimitive?.intOrNull}")
        println("Next call time: ${(jsonElement.jsonObject["next_use_secs"]?.jsonPrimitive?.intOrNull)?.div(60)} minutes")
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("Request numbers failed")
    }
}