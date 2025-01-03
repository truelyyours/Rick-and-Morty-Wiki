package com.example.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class KTorClient {
    private val baseUrl = "https://rickandmortyapi.com/api/"

    private val client = HttpClient(OkHttp) {
        defaultRequest { url(baseUrl) }

        install(Logging) {
            logger = Logger.SIMPLE
        }

//        Response are in JSON hence...
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCharacter(id: Int):Character {
//        .body to leverage contentNegotiation
        return client.get("character/$id").body()
    }
}

@Serializable
data class Character (
    val id: Int,
    val name: String,
    val origin: Origin,
    val status: String,
    val species: String,

) {
    @Serializable
    data class Origin (
        val name: String,
//    val url: String
    )
}