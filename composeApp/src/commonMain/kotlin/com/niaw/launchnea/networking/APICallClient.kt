package com.niaw.launchnea.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.http.HttpConnectTimeoutException
import java.net.http.HttpTimeoutException
import java.net.UnknownHostException

//Creates an object to connect to the internet and perform the requests
val httpClient = HttpClient(OkHttp.create()) {
    install(HttpTimeout) {            //Extends Time to time out as the API is sometime slow
        requestTimeoutMillis = 60*1000 // 60 seconds
        connectTimeoutMillis = 15*1000  //15 seconds
        socketTimeoutMillis = 60*1000   //60 seconds
    }
    install(ContentNegotiation){        //adds JSON functionality to the object
        json(
            json= Json{
                prettyPrint = true
                ignoreUnknownKeys = true
            })
    }
}

    suspend fun APIFetchRequest(urlString: String?): ResponseData{
        if (urlString == null) {
            return ResponseData()//sends out all null so aborts
        }
        val response = try {
            httpClient.get(urlString)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return when (e) {
                is HttpRequestTimeoutException -> ResponseData(null, NetworkError.REQUEST_TIMEOUT)
                is ConnectTimeoutException -> ResponseData(null, NetworkError.CONNECTION_TIMEOUT)
                is SocketTimeoutException -> ResponseData(null, NetworkError.SOCKET_TIMEOUT)
                is SerializationException -> ResponseData(null, NetworkError.SERIALIZATION)
                is UnknownHostException -> ResponseData(null, NetworkError.NO_INTERNET)


                else -> {
                    println("Type: ${e::class.qualifiedName}")
                    println("Cause: ${e.cause}")
                    e.printStackTrace()
                    ResponseData(null, NetworkError.UNKNOWN)
                }
            }
        }
        println("API request finished")
        return when(response.status.value) {
            in 200..299 -> {                           
                val jsonElement = Json.parseToJsonElement(response.bodyAsText())         //If the HTTP code is a 2xx code which means success then it returns the response body

                // Safely attempt to get the "results" array
                val results = jsonElement.jsonObject["results"]?.jsonArray 
                if (results.isNullOrEmpty()){

                        ResponseData(null, NetworkError.NO_DATA)
                }else {
                    val count =jsonElement.jsonObject["count"]?.jsonPrimitive?.int ?:0
                    if (count <=100) {
                        ResponseData(results, null,count)
                    }else{
                        ResponseData(results, null,count,jsonElement.jsonObject["next"]?.jsonPrimitive?.contentOrNull)
                    }
                }
            }
            409 -> ResponseData(null,NetworkError.CONFLICT)             //If conflict 
            408 -> ResponseData(null,NetworkError.REQUEST_TIMEOUT)      //If it connects but returns timeout
            429 -> ResponseData(null,NetworkError.TOO_MANY_REQUESTS)    //If it goes over the call limit
            in 500..599 -> ResponseData(null,NetworkError.SERVER_ERROR) //If the server errors
            else -> ResponseData(null,NetworkError.UNKNOWN)
        }
    }
