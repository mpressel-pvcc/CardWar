package com.michaelpressel.cardwar.model

//Deck class to hold the deck of cards
class Deck {
    //attributes include the mutable list of Card objects
    private val cards = mutableListOf<Card>()

    //initialize a single deck

    init {
        val suits = listOf("Hearts", "Diamonds", "Clubs", "Spades")

        for (suit in suits) {
            for(i in 2..14) {
                cards.add(Card(i, suit))
            }//end nested rank loop
        }//end outer suit loop
    }//end initialization

    //methods

    //shuffle the deck when called
    fun shuffle() {
        cards.shuffle()
    }//end shuffle

    //function to deal top card off of the deck
    //return the top card
    fun dealCard(): Card {
        return cards.removeAt(0)
    }//end deal

    //determine if the deck is empty (signals the end of the deal phase)
    fun isDeckEmpty(): Boolean {
        if (cards.size == 0) {
            return true
        } else
            return false
    }//end isDeckEmpty

}//end class