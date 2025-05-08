//Screen to display the game
//Package and Imports
package com.michaelpressel.cardwar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.michaelpressel.cardwar.R
import com.michaelpressel.cardwar.data.PlayerScore
import com.michaelpressel.cardwar.model.Card
import com.michaelpressel.cardwar.model.GameModel
import com.michaelpressel.cardwar.model.GamePhase
import com.michaelpressel.cardwar.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

//Composable to get the image resource
@Composable
//CardImage has parameters for the card, facedown, and modifier
fun CardImage(
    card: Card? = null,
    facedown: Boolean = false,
    modifier: Modifier = Modifier
    ) {//end params start function
    val context = LocalContext.current
    //get the corresponding card image based on facedown or Card method call
    val resId = remember(card, facedown) {
        val name = if (facedown || card == null) "card_back" else card.getImageResName()
        context.resources.getIdentifier(name, "drawable", context.packageName)
    }//end resId assignment

    //display the card image
    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = if (facedown) "Facedown card" else "Card: ${card?.getCardName()}",
            modifier = modifier
                .height(160.dp)
                .aspectRatio(0.714f)
        )//end Image composable
    } else {
        Text("Card Image Not Found")
    }//end else
}//end CardImage function

//Large composable that handles the entire game screen
@Composable
//WarGameScreen has a PlayerViewModel parameter
fun WarGameScreen(playerViewModel: PlayerViewModel) {
    //get the player from the PlayerViewModel
    val player by playerViewModel.currentPlayer.collectAsState()
    if (player == null) {
        Text("Loading player...")
        return
    }//end if

    //create a GameModel instance and remember it
    val gameModel = remember { GameModel() }
    //state variables
    var message by remember { mutableStateOf("Welcome to War!") }
    var winRecorded by remember { mutableStateOf(false) }
    //get the game phase from the GameModel
    val gamePhase = gameModel.gamePhase
    //initial cards played and last cards played (only in war)
    //first in pair is player, second is villain
    val initial = gameModel.getInitialPlayedCards()
    val warFinal = gameModel.getLastPlayedCards()

    //column composable to hold the game screen
    //fills the entire screen and centers the content
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        //end column parameters
    ) {//start column content
        //display the player name and army strength
        Text("Welcome, ${player?.playerName ?: "Player"}!")
        Spacer(Modifier.height(8.dp))
        Text("${player?.playerName}'s Army Strength: ${gameModel.getPlayer1DeckSize()}")
        Text("Villain's Army Strength: ${gameModel.getPlayer2DeckSize()}")
        Spacer(Modifier.height(12.dp))
        //case structure to determine what is displayed
        when (gamePhase) {
            //when round or war resolved, show initial cards
            GamePhase.RoundResolved, GamePhase.WarResolved -> {
                Text("${player?.playerName}:")
                initial.first?.let { CardImage(card = it) }
                Spacer(Modifier.height(12.dp))
                Text("Villain:")
                initial.second?.let { CardImage(card = it) }

                //if we are resolving a war, get the war cards
                if (gamePhase == GamePhase.WarResolved) {
                    val (playerWarCard, villainWarCard) = warFinal

                    //spacer and text to explain cards
                    Spacer(Modifier.height(24.dp))
                    Text("War Cards:")
                    Spacer(Modifier.height(8.dp))

                    //if both players have a war card, display them in a row
                    if (playerWarCard != null && villainWarCard != null) {
                        Row(//row paramaeters, center vertical and arrange horizontally
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {//start content
                            //column within row that hold player war card
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(player?.playerName ?: "Player")
                                CardImage(card = playerWarCard)
                            }//end player war card column

                            //column within row that holds villain war card
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Villain")
                                CardImage(card = villainWarCard)
                            }//end villain war card column
                        }//end row content
                    } else {
                        Text("War cards not available.")
                    }//end else
                }//end if resolving war
            }//end round or war resolved

            //when round started or war declared, show facedown cards
            GamePhase.RoundStarted, GamePhase.WarDeclared -> {
                Text("${player?.playerName}:")
                CardImage(facedown = true)
                Spacer(Modifier.height(12.dp))
                Text("Villain:")
                CardImage(facedown = true)
            }//end round started or war declared
            else -> {}//end else
        }//end when case structure

        //spacer and text to explain cards
        Spacer(Modifier.height(14.dp))
        Text(message)
        Spacer(Modifier.weight(1f))

        //case structure to determine what button to display
        when (gamePhase) {
            GamePhase.Pregame -> {
                Button(onClick = {
                    gameModel.startGame()
                    message = "Game Started!"
                }) {//end on click
                    Text("Start Game")
                }//end button
            }//end pregame button

            //button to start a round
            GamePhase.RoundStarted -> {
                Button(onClick = {
                    gameModel.playRound()
                    message = "Battle Commencing!"
                }) {//end on click
                    Text("Next Battle")
                }//end button
            }//end round started button

            //button to resolve a war
            GamePhase.WarDeclared -> {
                Button(onClick = {
                    gameModel.resolveWar()
                    message = "War declared!"
                }) {//end on click
                    Text("War is upon us")
                }//end button
            }//end war declared button

            //button to resolve a round
            GamePhase.RoundResolved, GamePhase.WarResolved -> {
                Button(onClick = {//on click resolve round
                    gameModel.resolveRound()
                    val winner = gameModel.getLastRoundWinner()  // Now it has updated state

                    //determine message based on round/war winner
                    message = when (winner) {
                        1 -> "${player?.playerName} wins the round!"
                        2 -> "Villain wins the round!"
                        else -> "It's a tie!"
                    }//end when for message
                }) {//end on click
                    Text("Next Battle")
                }//end button
            }//end round resolved button

            //button to end the game
            GamePhase.GameOver -> {
                //record the win if it hasn't already been recorded
                if (!winRecorded) {
                    //determine who won and record the win
                    if (gameModel.getPlayer1DeckSize() > gameModel.getPlayer2DeckSize()) {
                        playerViewModel.recordWin()
                    }//end if
                    winRecorded = true
                }//end if
                Text("Game Over!")
                //case structure to announce who won
                when (gameModel.getGameWinner()) {
                    1 -> Text("${player?.playerName ?: "Player"} wins the game!")
                    2 -> Text("Villain wins the game!")
                    else -> Text("It's a tie!")
                }//end when case structure

                val currentPlayer = player  // Safely assign to a local val
                //display game stats
                Text("Rounds Played: ${gameModel.getRoundsPlayer()}")
                Text("${player?.playerName ?: "Player"} War Wins: ${gameModel.getPlayer1WarsWon()}")
                Text("Villain War Wins: ${gameModel.getPlayer2WarsWon()}")
                Text("${player?.playerName ?: "Player"} Win Total: ${currentPlayer?.winCount}")
                Spacer(Modifier.height(8.dp))
                Button(onClick = {//on click start new game
                    gameModel.startGame()
                    message = "New game started!"
                    winRecorded = false
                }) {//end on click
                    Text("Play Again")
                }//end button
            }//end game over button
        }//end when case structure

        //auto play button, use coroutine to help with performance/race condition
        val scope = rememberCoroutineScope()
        var autoRunning by remember { mutableStateOf(false) }
        //button to auto play the game
        Button(
            onClick = {
                autoRunning = true
                scope.launch {
                    gameModel.autoPlay()
                    autoRunning = false
                    message = "Auto-resolve complete!"
                }//end coroutine launch
            },//end on click
            //disable button if auto is running or game is over
            enabled = !autoRunning && gamePhase != GamePhase.GameOver
        ) {//end button
            Text("Quick Complete")
        }//end button
    }//end column content
}//end WarGameScreen function

