package com.michaelpressel.cardwar.model

import androidx.compose.ui.tooling.preview.Preview

class Card(val rank: Int, val suit: String) {

   fun getValue(): Int{
       return rank
   }

    fun getCardName(): String {
        val cardName = when (rank) {
            11 -> "Jack"
            12 -> "Queen"
            13 -> "King"
            14 -> "Ace"
            else -> rank.toString()
        }
        return "$cardName of $suit"
    }

    fun getImageResName(): String {
        val rankName = when (rank) {
            11 -> "jack"
            12 -> "queen"
            13 -> "king"
            14 -> "ace"
            else -> rank.toString()
        }
        return "card_${rankName}_${suit.lowercase()}"
    }

}


