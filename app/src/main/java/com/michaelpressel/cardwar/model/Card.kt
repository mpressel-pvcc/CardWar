package com.michaelpressel.cardwar.model

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

}