package com.example.network.models.local

import android.graphics.Color

sealed class CharacterStatus(val displayName: String, val color: Int) {
    data object Alive: CharacterStatus("Alive", Color.GREEN)
    data object Dead: CharacterStatus("Dead", Color.RED)
    data object Unknown: CharacterStatus("Unknown", Color.YELLOW)
}