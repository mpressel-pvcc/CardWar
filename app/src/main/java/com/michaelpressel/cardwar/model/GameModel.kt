package com.michaelpressel.cardwar.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

//GameModel to hold all game logic
class GameModel {
    //get the game phase and initialize it to pregame
    var gamePhase by mutableStateOf(GamePhase.Pregame)
        private set
    //model attributes include decks of cards for each player, cards in battle, and game over flag
    private val p1Deck = mutableListOf<Card>()
    private val p2Deck = mutableListOf<Card>()
    private val cardsInPlay = mutableListOf<Card>()
    private var lastRoundWinner: Int? = null
    fun getLastRoundWinner(): Int? = lastRoundWinner
    private var lastWarWinner: Int? = null
    fun getLastWarWinner(): Int? = lastWarWinner
    private var roundsPlayed = 0
    fun getRoundsPlayer(): Int = roundsPlayed
    private var player1WarsWon = 0
    private var player2WarsWon = 0
    fun getPlayer1WarsWon(): Int = player1WarsWon
    fun getPlayer2WarsWon(): Int = player2WarsWon
    private var isGameOver = false
    private var gameWinner: Int? = null
    fun getGameWinner(): Int? = gameWinner



    //initialize the game
    fun startGame() {
        //initialize the deck
        val deck = Deck()
        deck.shuffle()

        //clear all states
        p1Deck.clear()
        p2Deck.clear()
        cardsInPlay.clear()
        initialCardsPlayed = Pair(null, null)
        lastCardsPlayed = Pair(null, null)
        lastRoundWinner = null
        lastWarWinner = null
        gameWinner = null
        roundsPlayed = 0
        player1WarsWon = 0
        player2WarsWon = 0
        isGameOver = false

        //deal cards to each player as long as the deck is not empty
        while (!deck.isDeckEmpty()) {
            p1Deck.add(deck.dealCard())
            p2Deck.add(deck.dealCard())
        }//end while

        gamePhase = GamePhase.RoundStarted
    }//end startGame()


    //methods to get deck sizes
    fun getPlayer1DeckSize(): Int {
        return p1Deck.size
    }//end getPlayer1DeckSize
    fun getPlayer2DeckSize(): Int {
        return p2Deck.size
    }//end getPlayer2DeckSize

    //methods to get initial and last played cards
    private var initialCardsPlayed: Pair<Card?, Card?> = Pair(null,null)
    fun getInitialPlayedCards(): Pair<Card?, Card?> {
        return initialCardsPlayed
    }//end getInitialPlayedCards
    private var lastCardsPlayed: Pair<Card?, Card?> = Pair(null,null)
    fun getLastPlayedCards(): Pair<Card?, Card?> {
        return lastCardsPlayed
    }//end getLastPlayedCards

    //play a round in the game, triggered by a button press in game screen
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
        //increment rounds played to be displayed at end of game
        roundsPlayed++
        //determine next phase. if cards are equal go to war, otherwise we go to roundResolve
        gamePhase = if (p1Card.getValue() == p2Card.getValue()) {
            GamePhase.WarDeclared
        } else {//end if
            GamePhase.RoundResolved
        }//end gamePhase assignment
    }//end playRoundPhase()

    //Resolve the round after battle. Triggered by a button press in game screen
    fun resolveRound() {

        //ensure we are in the correct game phase
        if (gamePhase != GamePhase.RoundResolved && gamePhase != GamePhase.WarResolved){
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
        lastRoundWinner = winner

        //add cards to the winner's deck
        if (winner == 1) {
            p1Deck.addAll(cardsInPlay)
        } else {
            p2Deck.addAll(cardsInPlay)
        }//end resolve card distribution

        //clear the field for next phase
        cardsInPlay.clear()

        //determine the next game phase
        if (p1Deck.isEmpty() || p2Deck.isEmpty()) {
            gameWinner = if (p1Deck.isEmpty()) 2 else 1
            gamePhase = GamePhase.GameOver
        } else {
            gamePhase = GamePhase.RoundStarted
        }//end game phase assignment
    }//end resolveRound

    //resolve a war in the game. Triggered by a button press in game screen
    fun resolveWar() {
        // Ensure we are in the correct game phase
        if (gamePhase != GamePhase.WarDeclared) return

        // Skip if no initial cards
        val (initialP1Card, initialP2Card) = initialCardsPlayed
        if (initialP1Card == null || initialP2Card == null) return

        // Determine how many cards each player can commit (either 4 or smallest deck total)
        val warCardCount = minOf(4, p1Deck.size, p2Deck.size)
        if (warCardCount == 0) {
            gameWinner = if (p1Deck.isEmpty()) 2 else 1
            gamePhase = GamePhase.GameOver
            return
        }//end warCardCount check

        //define the wars to determine the war
        val warCardsP1 = mutableListOf<Card>()
        val warCardsP2 = mutableListOf<Card>()

        //draw as many cards as the warCardCount
        repeat(warCardCount) {
            warCardsP1.add(p1Deck.removeAt(0))
            warCardsP2.add(p2Deck.removeAt(0))
        }//end war card draw loop

        //add the cards to the field
        cardsInPlay.addAll(warCardsP1 + warCardsP2)

        // Store the last played war cards for display
        val lastCardP1 = warCardsP1.last()
        val lastCardP2 = warCardsP2.last()
        lastCardsPlayed = Pair(lastCardP1, lastCardP2)

        //case structure to determine winner of war
        when {
            //case for player 1 win
            lastCardP1.getValue() > lastCardP2.getValue() -> {
                p1Deck.addAll(cardsInPlay)
                lastRoundWinner = 1
                player1WarsWon++
            }//end case player1 wins
            //case for player 2 win
            lastCardP2.getValue() > lastCardP1.getValue() -> {
                p2Deck.addAll(cardsInPlay)
                lastRoundWinner = 2
                player2WarsWon++
            }//end case player2 wins
            //if cards are equal, go to war again
            else -> {
                // Recursively resolve another war
                gamePhase = GamePhase.WarDeclared
                resolveWar()
                return
            }//end else
        }//end when (case structure)

        //clear the field for next phase
        cardsInPlay.clear()
        //determine if game is over
        if (p1Deck.isEmpty() || p2Deck.isEmpty()) {
            gameWinner = if (p1Deck.isEmpty()) 2 else 1
            gamePhase = GamePhase.GameOver
        } else {//end if
            gamePhase = GamePhase.WarResolved
        }//end else and game over check
    }//end resolveWar

    //auto play the game
    fun autoPlay() {
        // Start the game if it hasn't started yet
        if (gamePhase == GamePhase.Pregame) {
            startGame()
        }//end if

        // Loop until the game is over
        while (gamePhase != GamePhase.GameOver) {
            when (gamePhase) {
                GamePhase.RoundStarted -> playRound()
                GamePhase.RoundResolved, GamePhase.WarResolved -> resolveRound()
                GamePhase.WarDeclared -> resolveWar()
                else -> break
            }//end when case structure to determine next phase
        }//end while
    }//end autoPlay
}//end class

