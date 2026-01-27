package com.example.rickandmortywiki.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.KTorClient
import com.example.network.models.local.Character
import com.example.network.models.local.CharacterStatus
import com.example.rickandmortywiki.components.character.CharacterStatusComponent
import com.example.rickandmortywiki.components.common.CharacterImage
import com.example.rickandmortywiki.components.common.DataPoint
import com.example.rickandmortywiki.components.common.DataPointComponent
import com.example.rickandmortywiki.components.common.LoadingState
import com.example.rickandmortywiki.ui.theme.RickAction

sealed interface CharacterDetailsViewState {
    object loading: CharacterDetailsViewState
    data class Error(val message: String): CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ): CharacterDetailsViewState
}

@Composable
fun CharacterDetailsScreen(
    kTorClient: KTorClient,
    characterId: Int,
    onEpisodeClick: (Int) -> Unit // Callback for navigation with id as argument.
) {
    /*Main Composable component for the character details screen*/

    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints: List<DataPoint> by remember {
        // Using derivedStateOf reduces recomposing for each change in character.
        // Only where this variable is explicity change would it trigger or recompute this buildList
        derivedStateOf {
            buildList {
                character?.let { character ->
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    // Take IF the type exists for this char otherwise ignore
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode Count", character.episodeUrls.size.toString()))
                }
            }
        }
    }

//    Data points fetching
    LaunchedEffect(key1 = Unit, block = {
        kTorClient.getCharacter(characterId)
            .onSuccess { character = it }
            .onFailure { exception ->
                // todo handle error
            }
    })

//    THe whole character details is a list view i.e. recycler view in traditional sense
    LazyColumn(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(16.dp)
    ) {
        if (character == null) {
            item { LoadingState() }
            return@LazyColumn
        }

        // Name plate
        item {
            CharacterDetailNamePlateComponent(
                name = character!!.name,
                status = character!!.status
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Image
        item {
            // SubCompose instead of Just AsyncImage so we get loading, error callbacks in declaration itself wo extra state variables.
            CharacterImage(character!!.imageUrl)
        }


        // Data points
        items(characterDataPoints) {
            Spacer(modifier = Modifier.height(16.dp))
            DataPointComponent(it)
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        // Button
        item {
            Text(
                text = "View all episodes",
                color = RickAction,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .border(
                        width = 1.dp,
                        color = RickAction,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onEpisodeClick(characterId)
                    }
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }

        // Indicating end
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun CharacterDetailNamePlateComponent(name: String, status: CharacterStatus) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CharacterStatusComponent(status)
        Text(text = name,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = RickAction
        )
    }
}

