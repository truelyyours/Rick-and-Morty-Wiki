package com.example.rickandmortywiki.components.character

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.models.local.CharacterStatus

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.border(1.dp, color = Color(characterStatus.color))) {
        Text("Status: ", fontSize = 20.sp)
    }
}