package com.example.rickandmortywiki.components.character

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.KTorClient
import com.example.network.models.local.Character
import com.example.network.models.local.Episode
import com.example.rickandmortywiki.components.common.CharacterImage
import com.example.rickandmortywiki.components.common.CharacterNameComponent
import com.example.rickandmortywiki.components.common.DataPoint
import com.example.rickandmortywiki.components.common.DataPointComponent
import com.example.rickandmortywiki.components.common.LoadingState
import com.example.rickandmortywiki.components.episode.EpisodeRowComponent
import com.example.rickandmortywiki.ui.theme.RickPrimary
import com.example.rickandmortywiki.ui.theme.RickTextPrimary
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeScreen(characterId: Int, kTorClient: KTorClient) {
    var characterState by remember { mutableStateOf<Character?>(null) }
    var episodeState by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(Unit) {
        kTorClient.getCharacter(characterId).onSuccess { it ->
            characterState = it
//            Log.d("CharacterEpisodeScreen", "Episodes: ${it.episodeIds}")
            launch { kTorClient.getEpisodes(it.episodeIds).onSuccess { episodes ->
                Log.d("CharacterEpisodeScreen", "Episodes: ${episodes.size}")
                episodeState = episodes
            }.onFailure {
                Log.e("CharacterEpisodeScreen", "Error fetching episodes: ${it.message}")
                // TODO
            }
            }
        }.onFailure {
            // #TODO
        }
    }

    characterState?.let { character ->
        MainScreen(character = character, episodeState)
    } ?: LoadingState()
}

// The reason for creating this composable is so that we don't have to worry about nullable
@Composable
fun MainScreen(character: Character, episodes: List<Episode>) {
    val episodeBySeasonMap =episodes.groupBy { it.seasonNumber }

    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()) {
        item { CharacterNameComponent(character.name) }
        item { Spacer(Modifier.height(8.dp)) }

        // Horizontal scroll of season headers
        item {
            LazyRow {
                episodeBySeasonMap.forEach {
                    val title = "Season ${it.key}"
                    val description = "${it.value.size} ep"
                    item {
                        DataPointComponent( DataPoint(
                            title, description
                        ))
                        Spacer(Modifier.width(32.dp))
                    }
                }
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
        item { CharacterImage(character.imageUrl) }
        item { Spacer(Modifier.height(24.dp)) }

        episodeBySeasonMap.forEach { ent ->
            // header Component
            stickyHeader { SeasonHeader(ent.key) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(ent.value) { epi ->
                EpisodeRowComponent(epi)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SeasonHeader(seasonNumber: Int) {
    Text(
        text = "Season $seasonNumber",
        fontSize = 32.sp,
        color = RickTextPrimary,
        lineHeight = 32.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
            .background(RickPrimary)
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = RickTextPrimary,
                shape = RoundedCornerShape(8.dp)
        ).padding(vertical = 8.dp)
    )
}