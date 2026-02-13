package com.example.rickandmortywiki.repositories

import com.example.network.ApiOperation
import com.example.network.KTorClient
import com.example.network.models.local.Character
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val kTorClient: KTorClient) {
    /*This is the main middle man from which the viewmodel get's the data.
    All data related to DB/network should be asked from this repository.
    This is where we should implement functions that make API calls or DB calls etc.
    This is also where we do web socket, pooling SSE etc and ofc caching too.
    */

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return kTorClient.getCharacter(characterId)
    }

}