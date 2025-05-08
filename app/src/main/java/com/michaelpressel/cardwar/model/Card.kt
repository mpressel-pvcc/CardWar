package com.michaelpressel.cardwar.model

import androidx.compose.ui.tooling.preview.Preview
//Card class to hold the rank and suit
//and image name retrieval function
class Card(val rank: Int, val suit: String) {
    //return the rank
   fun getValue(): Int{
       return rank
   }//end getValue function

    //get the card name based on rank
    fun getCardName(): String {
        val cardName = when (rank) {
            11 -> "Jack"
            12 -> "Queen"
            13 -> "King"
            14 -> "Ace"
            else -> rank.toString()
        }//end when
        return "$cardName of $suit"
    }//end getCardName function

    //get the image name based on rank and suit
    fun getImageResName(): String {
        val rankName = when (rank) {
            11 -> "jack"
            12 -> "queen"
            13 -> "king"
            14 -> "ace"
            else -> rank.toString()
        }//end when
        if (rank in 11..13) {
        return "card_${rankName}_${suit.lowercase()}2"
            } else {
            return "card_${rankName}_${suit.lowercase()}"
        }//end else
    }//end getImageResName function
}//end Card class


