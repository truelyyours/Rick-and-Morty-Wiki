package com.example.network.models.local

import android.graphics.Color

/*Sealed class is like ENUM but more restrictive as when used with `when` we have to
* take into account ALL the subclasses compulsarily.
* We can also have data class which can store many values or data or other objects as
* as well as have helper functions that can manipulate the data which can readily
* be called within the `when` clause */
sealed class CharacterStatus(val displayName: String, val color: Int) {
    data object Alive: CharacterStatus("Alive", Color.GREEN)
    data object Dead: CharacterStatus("Dead", Color.RED)
    data object Unknown: CharacterStatus("Unknown", Color.YELLOW)
}