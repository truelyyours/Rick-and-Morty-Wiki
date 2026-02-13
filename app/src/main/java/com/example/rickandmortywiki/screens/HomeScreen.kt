package com.example.rickandmortywiki.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    // If key1 is viewModel, then every time that viewModel is destroyed the launched effect is killed.
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.fetchInitialPage()
    })

    val scrollState = rememberLazyGridState()
    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentCharacterCount =
                (viewState as? HomeScreenViewState.GridDisplay)?.characters?.size ?: return@derivedStateOf false
            val lastDisplayedIndex =
                scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            // So when the user is within the "Last 10 elements", fetch the next page.
            return@derivedStateOf lastDisplayedIndex >= currentCharacterCount - 10
        }
    }

    LaunchedEffect(key1 = fetchNextPage) {
        if (fetchNextPage) viewModel.fetchNextPage()
    }

    when (val state = viewState) {
        HomeScreenViewState.Loading -> {
            LoadingState()
        }

        is HomeScreenViewState.GridDisplay -> {
            LazyVerticalGrid(
                state = scrollState,
                columns = GridCells.Fixed(2),
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