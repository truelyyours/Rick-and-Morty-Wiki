package com.example.rickandmortywiki.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.models.local.CharacterPage
import com.example.rickandmortywiki.repositories.CharacterRepository
import com.example.rickandmortywiki.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    private val _internalViewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState = _internalViewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = characterRepository.fetchCharacterPage(1)
        initialPage.onSuccess { characterPage ->
            fetchedCharacterPages.clear()
            fetchedCharacterPages.add(characterPage)
            _internalViewState.update {
                return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters)
            }

        }.onFailure {
            // TODO
            // Handle error
        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        characterRepository.fetchCharacterPage(nextPageIndex)
            .onSuccess { characterPage ->
                fetchedCharacterPages.add(characterPage)
                _internalViewState.update { currentState ->
                    val currentCharacters =
                        (currentState as HomeScreenViewState.GridDisplay).characters
                    return@update HomeScreenViewState.GridDisplay(
                        characters = currentCharacters + characterPage.characters
                    )
                }
            }.onFailure {
                // TODO
                // Handle error
            }
    }
}