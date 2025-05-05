package com.michaelpressel.cardwar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerScoreDao {
    @Insert
    suspend fun insertScore(score: PlayerScore)

    @Update
    suspend fun updateScore(score: PlayerScore)

    @Query("SELECT * FROM player_scores ORDER BY roundsToWin ASC LIMIT 1")
    fun getBestScore(): Flow<PlayerScore?>

    @Query("SELECT * FROM player_scores WHERE playerName = :name LIMIT 1")
    suspend fun getScoreByName(name: String): PlayerScore?
}