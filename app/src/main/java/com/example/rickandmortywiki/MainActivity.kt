package com.example.rickandmortywiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.network.KTorClient
import com.example.rickandmortywiki.screens.CharacterDetailsScreen
import com.example.rickandmortywiki.screens.CharacterEpisodeScreen
import com.example.rickandmortywiki.screens.HomeScreen
import com.example.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.example.rickandmortywiki.ui.theme.RickPrimary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var kTorClient: KTorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            RickAndMortyWikiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavHost(navController = navController, startDestination = "home_screen",
                        modifier = Modifier.background(color = RickPrimary).padding(paddingValues)) {
                        composable(route = "home_screen") {
                            HomeScreen { characterId ->
                                navController.navigate("character_details/$characterId")
                            }
                        }
                        composable("character_details/{characterId}",
                            arguments = listOf(navArgument("characterId") {type =
                                NavType.IntType})) { backStackEntry ->
                            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterDetailsScreen(
                                characterId = characterId
                            ) {
                                navController.navigate("character_episodes/${it}")
                            }
                        }
                        composable("character_episodes/{characterId}",
                            arguments = listOf(navArgument("characterId") {type =
                                NavType.IntType})) { backStackEntry ->
                            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                            // Handle the characterId as needed
                            CharacterEpisodeScreen(characterId, kTorClient)
                        }
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
