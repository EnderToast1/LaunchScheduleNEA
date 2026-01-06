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
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

//Creates an object to connect to the internet and perform the requests
val httpClient = HttpClient(OkHttp.create()) {
    install(HttpTimeout) {            //Extends Time to time out as the API is sometime slow
        requestTimeoutMillis = 30000 // 30 seconds
        connectTimeoutMillis = 15000  //1.5 seconds
        socketTimeoutMillis = 30000   //30 seconds
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
            httpClient.get(urlString)                                 //Tries to get request the data from the api
        } catch (e: TimeoutCancellationException) {
            return ResponseData(null, NetworkError.REQUEST_TIMEOUT)          //If the Request timeout it catches it and returns the error
        }catch(e: UnresolvedAddressException){
            return ResponseData(null,NetworkError.NO_INTERNET)                //If the address is invalid for some reason it catches it and returns the error
        }catch(e: SerializationException){
            return ResponseData(null,NetworkError.SERIALIZATION)       //If there is a json error it catches it and returns the error
        }
        return when(response.status.value) {
            in 200..299 -> {                           
                val jsonElement = Json.parseToJsonElement(response.bodyAsText())         //If the HTTP code is a 2xx code which means success then it returns the response body

                // Safely attempt to get the "results" array
                val results = jsonElement.jsonObject["results"]?.jsonArray 
                if (results.isNullOrEmpty()){
                    ResponseData(null,NetworkError.No_DATA)
                }else {
                    ResponseData(results, null)
                }
            }
            409 -> ResponseData(null,NetworkError.CONFLICT)             //If conflict 
            408 -> ResponseData(null,NetworkError.REQUEST_TIMEOUT)      //If it connects but returns timeout
            429 -> ResponseData(null,NetworkError.TOO_MANY_REQUESTS)    //If it goes over the call limit
            in 500..599 -> ResponseData(null,NetworkError.SERVER_ERROR) //If the server errors
            else -> ResponseData(null,NetworkError.UNKNOWN)
        }
    }
