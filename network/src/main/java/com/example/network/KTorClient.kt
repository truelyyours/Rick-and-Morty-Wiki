package com.example.network

import com.example.network.models.local.Character
import com.example.network.models.remote.RemoteCharacter
import com.example.network.models.remote.toLocalCharacter
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
        return client.get("character/$id")
            .body<RemoteCharacter>() // Tell the body to parse the response to serializable class RemoteCharacter
            .toLocalCharacter()
    }
}