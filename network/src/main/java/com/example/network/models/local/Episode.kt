package com.example.network.models.local

data class Episode(
    val id: Int,
    val name: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val airDate: String,
    val episode: String,
    val characterIdsInEpisode: List<Int>
)