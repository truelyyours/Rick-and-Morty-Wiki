package com.example.rickandmortywiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.network.KTorClient
import com.example.network.models.local.Character
import com.example.network.models.local.CharacterStatus
import com.example.network.models.remote.toLocalCharacter
import com.example.rickandmortywiki.components.character.CharacterDetailsScreen
import com.example.rickandmortywiki.components.character.CharacterStatusComponent
import com.example.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.example.rickandmortywiki.ui.theme.RickPrimary
import java.io.Console

class MainActivity : ComponentActivity() {

    private val kTorClient = KTorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyWikiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Column(modifier = Modifier.background(color = RickPrimary)
                        .padding(paddingValues)) {
                        CharacterDetailsScreen(
                            kTorClient = kTorClient,
                            characterId = 22
                        )
                    }
                }
//                Surface (modifier = Modifier.fillMaxSize(),
//                    color = RickPrimary
//                ) {
//                    CharacterDetailsScreen(
//                        kTorClient = kTorClient,
//                        characterId = 22
//                    )
//                }
            }
        }
    }
}
