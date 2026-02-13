package com.example.network

import com.example.network.models.local.Character
import com.example.network.models.local.CharacterPage
import com.example.network.models.local.Episode
import com.example.network.models.remote.RemoteCharacter
import com.example.network.models.remote.RemoteCharacterPage
import com.example.network.models.remote.RemoteEpisode
import com.example.network.models.remote.toLocalCharacter
import com.example.network.models.remote.toLocalCharacterPage
import com.example.network.models.remote.toLocalEpisode
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
    /*One of the major issue is that this is not singleton so we should always have DI and it's own module*/
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

    private var characterCache = mutableMapOf<Int, Character>()

    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
//        .body to leverage contentNegotiation
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>() // Tell the body to parse the response to serializable class RemoteCharacter
                .toLocalCharacter()
                .also { characterCache[id] = it }
        }
    }

    suspend fun getCharacterByPage(pageNumber: Int) : ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>()
                .toLocalCharacterPage()
        }
    }

    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return safeApiCall {
            client.get("episode/$episodeId")
                .body<RemoteEpisode>()
                .toLocalEpisode()
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return if (episodeIds.size == 1) {
            getEpisode(episodeIds[0]).mapSuccess { listOf(it) }
        } else {
            val idsCommaSeparated = episodeIds.joinToString(separator = ",")
            return safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<RemoteEpisode>>() // Tell the body to parse the response to serializable class RemoteCharacter
                    .map { it.toLocalEpisode() }
            }
        }
    }

    // Generica API call function
    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        // Maybe a bit expensive for wrapping each api call in try catch?
        // Maybe yes. Maybe then we cna handle failure explicitly by creating different viewmodel class?
        return try {
            ApiOperation.Success(apiCall())
        } catch (e: Exception) {
            // Any error is caught as API FAILURE and processed accordingly!
            ApiOperation.Failure(e)
        }
    }
}

// Generic for handling success, fail, error for api calls
sealed interface ApiOperation<T> {
    data class Success<T> (val data: T): ApiOperation<T>
    data class Failure<T> (val exception: Exception): ApiOperation<T>

    // Basically run the transform function on T and return R.
    // Functional programming best
    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(this.data))
            is Failure -> Failure(this.exception)
        }
    }

    // BELOW FUNCTIONS ARE BASICALLY CALLBACKS
    fun onSuccess(block: (T) -> Unit): ApiOperation<T> {
        // if "this" i.e. api operation is of type "Success" process the data.
        if (this is Success) {
            block(this.data)
        }
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) {
            block(this.exception)
        }
        return this
    }
}