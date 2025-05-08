//store player information in the database
//player_scores is the entity class for the database
package com.michaelpressel.cardwar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//define the data to be stored in the database
@Entity(tableName = "player_scores")
//data class is a simple class that holds data
data class PlayerScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerName: String,
    val gamesPlayed: Int,
    val roundsToWin: Int,
    val winCount: Int = 1,
    val timeStamp: Long = System.currentTimeMillis()
)//end PlayerScore data class