package com.example.rickandmortywiki.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.network.models.local.Character
import com.example.rickandmortywiki.components.character.CharacterGridItem
import com.example.rickandmortywiki.components.common.LoadingState
import com.example.rickandmortywiki.viewmodels.HomeScreenViewModel

sealed interface HomeScreenViewState {
    object Loading: HomeScreenViewState
    data class GridDisplay(val characters: List<Character>): HomeScreenViewState
//    data class ListDisplay(val characters: List<Character>): HomeScreenViewState
}

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()

    // If key1 is viewModel, then eveyr time that viewModel is destroyed the launched effect is killed.
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.fetchInitialPage()
    })


    when (val state = viewState) {
        HomeScreenViewState.Loading -> {
            LoadingState()
        }

        is HomeScreenViewState.GridDisplay -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.characters,
                    key = { it.id }
                ) { character ->
                    CharacterGridItem(character = character) {
                        onCharacterClick(character.id)
                    }
                }
            }
        }
    }

}