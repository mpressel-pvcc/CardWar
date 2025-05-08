package com.michaelpressel.cardwar.ui.navigation

//Sealed Class to handle route navigation, can add more screens as needed in the future leaderboard, player profile, etc.
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Game : Screen("game")
    object Leaderboard : Screen("leaderboard")
}//end sealed class Screen