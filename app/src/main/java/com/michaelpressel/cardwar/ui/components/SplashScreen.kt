package com.michaelpressel.cardwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp


//Splash screen composable to get and set player name in the database
@Composable
//SplashScreen has a onStartClick parameter
fun SplashScreen(onStartClick: (String) -> Unit) {
    //state variables PlayerName
    var playerName by remember { mutableStateOf("") }

    //column composable to hold the splash screen
    Column(//column parameters are modifier(fill max, padding), vertical arrangement, and horizontal alignment
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(32.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {//content start
        //text and text field to get player name
        Text("Welcome to War!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        //text field to get player name
        OutlinedTextField(
            value = playerName,
            //update player name
            onValueChange = { playerName = it },
            label = { Text("Enter your name")}
        )//end OutlinedTextField
        Spacer(modifier = Modifier.height(16.dp))
        //button to start the game
        Button(
            //on click, call onStartClick with player name
            onClick = {onStartClick(playerName)},
            //disable button if player name is blank
            enabled = playerName.isNotBlank()
        ){//end button
            Text("Start Game")
        }//end button
    }//end column content
}//end SplashScreen function