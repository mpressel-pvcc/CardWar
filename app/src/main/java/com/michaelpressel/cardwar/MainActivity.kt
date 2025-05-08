package com.michaelpressel.cardwar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelpressel.cardwar.ui.components.SplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michaelpressel.cardwar.data.AppDatabase
import com.michaelpressel.cardwar.ui.components.WarGameScreen
import com.michaelpressel.cardwar.ui.navigation.Screen
import com.michaelpressel.cardwar.ui.theme.CardWarTheme
import com.michaelpressel.cardwar.ui.viewmodel.PlayerViewModel
import com.michaelpressel.cardwar.ui.viewmodel.PlayerViewModelFactory

//Main Activity to handle navigation
//returns ComponentActivity
class MainActivity : ComponentActivity() {
    //onCreate function to set the content and navigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set the content to the CardWarTheme
        setContent {
            CardWarTheme {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.playerScoreDao()
                val playerViewModel: PlayerViewModel = viewModel(
                    factory = PlayerViewModelFactory(dao)
                )//end playerViewModel
                //navController to handle navigation
                val navController = rememberNavController()

                //NavHost to define the navigation graph
                NavHost(
                    //set the navController
                    navController = navController,
                    //set the start destination to the SplashScreen
                    startDestination = Screen.Splash.route
                ) {//end NavHost
                    //composable for the SplashScreen
                    composable(Screen.Splash.route) {
                        //call the SplashScreen composable
                        SplashScreen(
                            //on start click, navigate to the GameScreen
                            onStartClick = { name ->
                                playerViewModel.setPlayerName(name)
                                navController.navigate(Screen.Game.route)
                            }//end on start click
                        )//end SplashScreen
                    }//end composable for SplashScreen
                    composable(Screen.Game.route) {
                        WarGameScreen(playerViewModel = playerViewModel)
                    }//end composable for GameScreen
                }//end NavHost
            }//end CardWarTheme
        }//end setContent
    }//end onCreate function
}//end MainActivity class


