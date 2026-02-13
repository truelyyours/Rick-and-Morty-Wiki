package com.example.rickandmortywiki.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun fetchInitialPage() = viewModelScope.launch {
        val initialPage = characterRepository.fetchCharacterPage(1)
        initialPage.onSuccess { characterPage ->
            _internalViewState.update {
                return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters)
            }

        }.onFailure {
            // TODO
            // Handle error
        }
    }

    fun fetchNextPage() {

    }

}