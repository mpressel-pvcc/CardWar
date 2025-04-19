package com.michaelpressel.cardwar.model

class GameModel {
    var gamePhase: GamePhase = GamePhase.Pregame
    fun getGamePhase(): GamePhase {
        return gamePhase
    }

    //model attributes include decks of cards for each player, cards in battle, and game over flag
    private val p1Deck = mutableListOf<Card>()
    private val p2Deck = mutableListOf<Card>()
    private val cardsInPlay = mutableListOf<Card>()

    private var isGameOver = false


    //initialize the game
    fun startGame() {
        //initialize the deck
        val deck = Deck()
        deck.shuffle()

        //ensure the decks are empty to start the game
        p1Deck.clear()
        p2Deck.clear()
        cardsInPlay.clear()

        //deal cards to each player as long as the deck is not empty
        while (!deck.isDeckEmpty()) {
            p1Deck.add(deck.dealCard())
            p2Deck.add(deck.dealCard())
        }//end while

        gamePhase = GamePhase.RoundStarted
    }//end startGame()


    fun getPlayer1DeckSize(): Int {
        return p1Deck.size
    }

    fun getPlayer2DeckSize(): Int {
        return p2Deck.size
    }

    private var initialCardsPlayed: Pair<Card?, Card?> = Pair(null,null)
    fun getInitialPlayedCards(): Pair<Card?, Card?> {
        return initialCardsPlayed
    }
    private var lastCardsPlayed: Pair<Card?, Card?> = Pair(null,null)
    fun getLastCardsPlayed(): Pair<Card?, Card?> {
        return lastCardsPlayed
    }

    fun playRound() {
        //do not enter this phase unless gamePhase is RoundStarted
        if (gamePhase != GamePhase.RoundStarted) {
            return
        }//end gamePhase check

        //establish cards for each player and add them to the "field"
        val p1Card = p1Deck.removeAt(0)
        val p2Card = p2Deck.removeAt(0)
        initialCardsPlayed = Pair(p1Card, p2Card)
        cardsInPlay.add(p1Card)
        cardsInPlay.add(p2Card)

        //determine next phase. if cards are equal go to war, otherwise we go to roundResolve
        gamePhase = if (p1Card.getValue() == p2Card.getValue()) {
            GamePhase.WarDeclared
        } else {
            GamePhase.RoundResolved
        }//end gamePhase assignment
    }//end playRoundPhase()

    fun resolveRound() {

        //ensure we are in the correct game phase
        if(gamePhase != GamePhase.RoundResolved) {
            return
        }//end game phase check

        //set the value of the cards played
        val (p1Card, p2Card) = initialCardsPlayed

        //determine a winner and add cards to the winner's deck
        //use case structure to determine winner
        val winner = when {
            p1Card == null || p2Card == null -> return
            p1Card.getValue() > p2Card.getValue() -> 1
            else -> 2
        }//end winner assignment

        //add cards to the winner's deck
        if (winner == 1) {
            p1Deck.addAll(cardsInPlay)
        } else {
            p2Deck.addAll(cardsInPlay)
        }//end resolve card distribution

        //clear the field for next phase
        cardsInPlay.clear()

        //determine the next game phase
        gamePhase = if (p1Deck.isEmpty() || p2Deck.isEmpty()) {
            GamePhase.GameOver
        } else {
            GamePhase.RoundStarted
        }//end game phase assignment
    }//end resolveRound

    fun resolveWar() {
        //determine that we are in the correct game phase
        if (gamePhase != GamePhase.WarDeclared) {
            return
        }//end game phase check

        val (initialP1Card, initialP2Card) = initialCardsPlayed
        if (initialP1Card == null || initialP2Card == null) {
            return
        }

        //determine the "length of war" based on if players have less than 4 cards
        val warCardCount = minOf(4, p1Deck.size, p2Deck.size)
        if (warCardCount == 0) {
            gamePhase = GamePhase.GameOver
            return
        }//end war card check

        val warCardsP1 = mutableListOf<Card>()
        val warCardsP2 = mutableListOf<Card>()

        //deal out the cards for war and stash the values in each players "army"
        for (i in 0 until warCardCount) {
            warCardsP1.add(p1Deck.removeAt(0))
            warCardsP2.add(p2Deck.removeAt(0))
        }//end war card deal

        //add the cards to the "field" to be won
        cardsInPlay.addAll(warCardsP1)
        cardsInPlay.addAll(warCardsP2)

        val lastCardP1 = warCardsP1.last()
        val lastCardP2 = warCardsP2.last()
        lastCardsPlayed = Pair(lastCardP1, lastCardP2)

        //determine the winner of the war using case structure
        when {
            lastCardP1.getValue() > lastCardP2.getValue() -> {
                p1Deck.addAll(cardsInPlay)
                gamePhase = GamePhase.WarResolved
            }

            lastCardP2.getValue() > lastCardP1.getValue() -> {
                p2Deck.addAll(cardsInPlay)
                gamePhase = GamePhase.WarResolved
            }

            else -> {
                gamePhase = GamePhase.WarDeclared
                resolveWar()
                return
            }
        }
        cardsInPlay.clear()

        if (p1Deck.isEmpty() || p2Deck.isEmpty()) {
            gamePhase = GamePhase.GameOver
        }
    }

}//end class

