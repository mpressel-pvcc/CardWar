package com.michaelpressel.cardwar.model

class GameModel {

    //model attributes include decks of cards for each player, cards in battle, and game over flag
    private val player1Deck = mutableListOf<Card>()
    private val player2Deck = mutableListOf<Card>()
    private val cardsInPlay = mutableListOf<Card>()

    private var isGameOver = false


    //initialize the game
    init {
        val deck = Deck()
        deck.shuffle()

        //deal cards to each player
        while (!deck.isDeckEmpty()) {
            player1Deck.add(deck.dealCard())
            player2Deck.add(deck.dealCard())
        }//end while
    }//end init

    fun isGameOver(): Boolean {
        return isGameOver
    }

    fun getPlayer1DeckSize(): Int {
        return player1Deck.size
    }

    fun getPlayer2DeckSize(): Int {
        return player2Deck.size
    }

    private var lastCardsPlayed: Pair<Card?, Card?> = Pair(null,null)
    fun getLastPlayedCards(): Pair<Card?, Card?> {
        return lastCardsPlayed
    }

    fun playRound() {

        //establish cards for each player and add them to the "field"
        val playerOneCard = player1Deck.removeAt(0)
        val playerTwoCard = player2Deck.removeAt(0)
        lastCardsPlayed = Pair(playerOneCard, playerTwoCard)
        cardsInPlay.add(playerOneCard)
        cardsInPlay.add(playerTwoCard)

        //award the higher card to the winning player
        if (playerOneCard.getValue() > playerTwoCard.getValue()) {
            player1Deck.addAll(cardsInPlay)
            cardsInPlay.clear()
        } else if (playerTwoCard.getValue() > playerOneCard.getValue()) {
            player2Deck.addAll(cardsInPlay)
            cardsInPlay.clear()
        } else {//end else if
            war()
            }//end else
        if (player1Deck.isEmpty() || player2Deck.isEmpty()) {
            isGameOver = true
            cardsInPlay.clear()
        }
    }//end play round

    private fun war() {

        val warCardCount = minOf(4, player1Deck.size, player2Deck.size)

        if (warCardCount == 0) {
            isGameOver = true
            return
        }

        val warCardsP1 = mutableListOf<Card>()
        val warCardsP2 = mutableListOf<Card>()


        for (i in 0 until warCardCount) {
            warCardsP1.add(player1Deck.removeAt(0))
            warCardsP2.add(player2Deck.removeAt(0))
        }
        cardsInPlay.addAll(warCardsP1)
        cardsInPlay.addAll(warCardsP2)

        val lastCardP1 = warCardsP1.last()
        val lastCardP2 = warCardsP2.last()
        lastCardsPlayed = Pair(lastCardP1, lastCardP2)

        when {
            lastCardP1.getValue() > lastCardP2.getValue() -> {
                player1Deck.addAll(cardsInPlay)
                cardsInPlay.clear()
            }
            lastCardP2.getValue() > lastCardP1.getValue() -> {
                player2Deck.addAll(cardsInPlay)
                cardsInPlay.clear()
            }
            else -> {
                if (player1Deck.isNotEmpty() && player2Deck.isNotEmpty()){
                    war()
                } else {
                    isGameOver = true
                }
            }

        }

    }
}//end class

