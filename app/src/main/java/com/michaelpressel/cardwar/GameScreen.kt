package com.michaelpressel.cardwar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun WarGameScreen() {
    val gameModel = remember { GameModel() }
    var message by remember { mutableStateOf("Welcome to War!") }
    var p1Card by remember { mutableStateOf<String?>(null) }
    var p2Card by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Player 1: ${gameModel.getPlayer1DeckSize()} cards")
        Text("Player 2: ${gameModel.getPlayer2DeckSize()} cards")

        Spacer(Modifier.height(24.dp))

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
    }

}

