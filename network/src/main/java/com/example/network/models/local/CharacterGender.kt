package com.example.network.models.local


sealed class CharacterGender(val displayName: String) {
    data object Male: CharacterGender("Male")
    data object Female: CharacterGender("Female")
    data object Genderless: CharacterGender("No Gender")
    data object Unknown: CharacterGender("No Gender Known")
}