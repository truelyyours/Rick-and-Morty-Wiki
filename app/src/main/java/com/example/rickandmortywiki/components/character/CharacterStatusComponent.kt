package com.example.rickandmortywiki.components.character

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.models.local.CharacterStatus
import com.example.rickandmortywiki.ui.theme.RickTextPrimary

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
//    For if character is alive dead etc.
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp,
                color = Color(characterStatus.color),
                shape = RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text("Status: ${characterStatus.displayName}",
            fontSize = 20.sp, color = RickTextPrimary)
    }
}