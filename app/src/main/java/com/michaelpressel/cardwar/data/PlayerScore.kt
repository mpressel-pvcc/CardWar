package com.michaelpressel.cardwar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_scores")
data class PlayerScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerName: String,
    val roundsToWin: Int,
    val winCount: Int = 1,
    val timeStamp: Long = System.currentTimeMillis()


)