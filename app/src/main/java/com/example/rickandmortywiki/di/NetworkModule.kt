package com.example.rickandmortywiki.di

import com.example.network.KTorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    // This will always provide the same "object" whenever KTor client is accessed! Noice

    @Provides
    @Singleton
    fun providesKtorClient(): KTorClient {
//        val config = ... // This'll be applied to all calls
        return KTorClient()
    }
}