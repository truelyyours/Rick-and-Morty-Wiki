package com.example.rickandmortywiki.components.character

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.network.models.local.Character
import com.example.rickandmortywiki.components.common.CharacterImage
import com.example.rickandmortywiki.components.common.DataPoint
import com.example.rickandmortywiki.components.common.DataPointComponent
import com.example.rickandmortywiki.ui.theme.RickAction

@Composable
fun CharacterListItem (
    modifier: Modifier = Modifier,
    character: Character,
    characterDataPoints: List<DataPoint>,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .height(140.dp)
            .border(width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color.Transparent, RickAction)),
                shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box {
            CharacterImage(character.imageUrl, modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
            )
            CharacterStatusCircle(character.status, modifier = Modifier.padding(start = 8.dp, top = 8.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
//            contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = listOf(DataPoint(title = "Name", description = character.name)) + characterDataPoints,
                key = { it.hashCode() },
            ) { dataPoint ->
                Column(verticalArrangement = Arrangement.Center) {
                    DataPointComponent(dataPoint = sanitizeDataPoint(dataPoint))
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))

    }
}

private fun sanitizeDataPoint(dataPoint: DataPoint): DataPoint {
    if (dataPoint.description.length > 12) {
        return DataPoint(dataPoint.title, dataPoint.description.take(12) + "...")
    }
    return dataPoint
}