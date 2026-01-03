package com.niaw.launchnea.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

//Creates an object to connect to the internet and perform the requests
val httpClient = HttpClient(OkHttp.create()) {
    install(HttpTimeout) {
        requestTimeoutMillis = 30000 // 30 seconds
        connectTimeoutMillis = 15000
        socketTimeoutMillis = 30000
    }
    install(ContentNegotiation){        //adds JSON functionality to the object
        json(
            json= Json{
                prettyPrint = true
                ignoreUnknownKeys = true
            })
    }
}

    suspend fun APIFetchRequest(urlString: String): ResponseData{
        val response = try {
            httpClient.get(urlString)
        } catch (e: SocketTimeoutException) {
            return ResponseData(null, NetworkError.REQUEST_TIMEOUT)
        }catch(e: UnresolvedAddressException){
            return ResponseData(null,NetworkError.NO_INTERNET)
        }catch(e: SerializationException){
            return ResponseData(null,NetworkError.SERIALIZATION)
        }
        return when(response.status.value) {
            in 200..299 -> {
                val jsonElement = Json.parseToJsonElement(response.bodyAsText())

                // Safely attempt to get the "results" array
                val results = jsonElement.jsonObject["results"]?.jsonArray
                if (results.isNullOrEmpty()){
                    ResponseData(null,NetworkError.No_DATA)
                }else {
                    ResponseData(results, null)
                }
            }
            409 -> ResponseData(null,NetworkError.CONFLICT)
            408 -> ResponseData(null,NetworkError.REQUEST_TIMEOUT)
            429 -> ResponseData(null,NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> ResponseData(null,NetworkError.SERVER_ERROR)
            else -> ResponseData(null,NetworkError.UNKNOWN)
        }
    }
