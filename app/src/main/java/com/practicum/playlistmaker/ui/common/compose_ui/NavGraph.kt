package com.practicum.playlistmaker.ui.common.compose_ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import kotlin.jvm.java

@Composable
fun NavGraph(startDestination: String = "search", gson: Gson){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){

        composable(
            route = "audio_player/{trackJson}",
            arguments = listOf(
                navArgument("trackJson") { type = NavType.StringType  }
            )
        )
        {navBackStackEntry ->
            val trackJson = navBackStackEntry.arguments?.getString("trackJson")
            val track = gson.fromJson(trackJson, Track::class.java)
            AudioPlayerFragment.createArgs(track)
            AudioPlayerFragment()
        }
    }
}