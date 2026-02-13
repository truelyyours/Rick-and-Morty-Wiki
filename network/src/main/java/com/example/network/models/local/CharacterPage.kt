package com.example.network.models.local

data class CharacterPage(
    val info: Info,
    val characters: List<Character>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}