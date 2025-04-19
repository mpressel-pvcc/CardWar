package com.michaelpressel.cardwar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.michaelpressel.cardwar.model.GameModel
import com.michaelpressel.cardwar.model.GamePhase

@Composable
fun WarGameScreen() {
    val gameModel = remember { GameModel() }
    var message by remember { mutableStateOf("Welcome to War!") }
    val gamePhase = gameModel.gamePhase
    val initial = gameModel.getInitialPlayedCards()
    val warFinal = gameModel.getLastPlayedCards()
Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Player 1's Army Strength: ${gameModel.getPlayer1DeckSize()}")
        Text("Player 2's Army Strength: ${gameModel.getPlayer2DeckSize()}")
        Spacer(Modifier.height(12.dp))

        //card displays and messages
        if(gamePhase == GamePhase.RoundResolved || gamePhase == GamePhase.WarResolved) {
            Text("Player 1 played: ${initial.first?.getCardName()}")
            Text("Player 2 played: ${initial.second?.getCardName()}")
            Spacer(Modifier.height(12.dp))
        }
        if (gamePhase == GamePhase.WarResolved) {
            Text("I... Declare... War!")
            Text("Player One's Card: ${warFinal.first?.getCardName()}")
            Text("Player Two's Card: ${warFinal.second?.getCardName()}")
            Spacer(Modifier.height(12.dp))
        }

        Text(text = message)
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        when (gameModel.gamePhase) {
            GamePhase.Pregame -> {
                Button(onClick = {
                    gameModel.startGame()
                    message = "Game Started!"
                }) {
                    Text("Start Game")
                }
            }

            GamePhase.RoundStarted -> {
                Button(onClick = {
                    gameModel.playRound()
                    message = "Battle Commencing!"
                }) {
                    Text("Next Battle")
                }
            }

            GamePhase.WarDeclared -> {
                Button(onClick = {
                    gameModel.resolveWar()
                }) {
                    Text("War!")
                }
            }

            GamePhase.RoundResolved -> {
                Button(onClick = {
                    gameModel.resolveRound()
                    message = when (gameModel.getLastRoundWinner()) {
                        1 -> "Battle Complete! Player 1 wins this round!"
                        2 -> "Battle Complete! Player 2 wins this round!"
                        else -> "War!"
                    }
                }) {
                    Text("Next Battle")
                }
            }

            GamePhase.WarResolved -> {
                Button(onClick = {
                    gameModel.resolveRound()
                }) {
                    Text("Next Battle")
                }
            }

            GamePhase.GameOver -> {
                Button(onClick = {
                    gameModel.startGame()
                    message = "Game restarted!"
                }) {
                    Text("Play Again?")
                }
            }
        }
    }



      /*
        Button(onClick = {
            if (!gameModel.isGameOver()) {
                val beforeSizeP1 = gameModel.getPlayer1DeckSize()
                val beforeSizeP2 = gameModel.getPlayer2DeckSize()

                gameModel.playRound()

                val afterSizeP1 = gameModel.getPlayer1DeckSize()
                val afterSizeP2 = gameModel.getPlayer2DeckSize()

                val played = gameModel.getLastPlayedCards()
                p1Card = played.first?.getCardName()
                p2Card = played.second?.getCardName()

                message = when {
                    afterSizeP1 > beforeSizeP1 -> "Player 1 wins this round!"
                    afterSizeP2 > beforeSizeP2 -> "Player 2 wins this round!"
                    else -> "War!"
                }
            } else {
                message = "Game over! Play again?"
            }
        }) {
            Text("Play Round")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(message)

        p1Card?.let {
            Text("P1 played: $it")
        }

        p2Card?.let {
            Text("P2 played: $it")
        }

       */
    }

}

