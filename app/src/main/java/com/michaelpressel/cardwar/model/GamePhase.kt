package com.michaelpressel.cardwar.model

//enumeration class to hold the game phases. Helps deliniate the game structure and allows for future expansion
enum class GamePhase {
    Pregame,
    RoundStarted,
    WarDeclared,
    RoundResolved,
    WarResolved,
    GameOver
}