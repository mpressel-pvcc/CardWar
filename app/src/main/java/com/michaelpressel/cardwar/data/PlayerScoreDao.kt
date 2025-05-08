//Database Access Object (DAO) allows for database operations to be performed
package com.michaelpressel.cardwar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//define the data access object (DAO) for the database
@Dao
//interface defines the methods for accessing the database
interface PlayerScoreDao {
    //define the methods for accessing the database
    //insert a new score into the database
    @Insert
    suspend fun insertScore(score: PlayerScore)
    //update an existing score in the database
    @Update
    suspend fun updateScore(score: PlayerScore)
    //get all scores and sort ascending (for future leaderboard)
    @Query("SELECT * FROM player_scores ORDER BY roundsToWin ASC LIMIT 1")
    fun getBestScore(): Flow<PlayerScore?>
    //get a player by name
    @Query("SELECT * FROM player_scores WHERE playerName = :name LIMIT 1")
    suspend fun getPlayerByName(name: String): PlayerScore?
    //get the number of games played for a player
    @Query("SELECT gamesPlayed FROM player_scores WHERE playerName = :playerName LIMIT 1")
    suspend fun getGamesPlayedForPlayer(playerName: String): Int
}//end PlayerScoreDao interface