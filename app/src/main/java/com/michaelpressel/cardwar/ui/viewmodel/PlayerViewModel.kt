package com.michaelpressel.cardwar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelpressel.cardwar.data.PlayerScore
import com.michaelpressel.cardwar.data.PlayerScoreDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//ViewModel to handle player data
//parameters are PlayerScoreDao
//returns ViewModel
class PlayerViewModel(private val playerScoreDao: PlayerScoreDao ) : ViewModel() {
    //MutableStateFlow to hold the current player
    private val _currentPlayer = MutableStateFlow<PlayerScore?>(null)
    //StateFlow to expose the current player
    val currentPlayer: StateFlow<PlayerScore?> = _currentPlayer.asStateFlow()

    //function to set the player name
    fun setPlayerName(name: String) {
        //launch a coroutine to insert or update the player
        viewModelScope.launch {
            //check if the player already exists
            val existing = playerScoreDao.getPlayerByName(name)
            if ( existing != null) {
                _currentPlayer.value = existing
                //update the player if they already exist
            } else {
                //insert a new player if they don't exist
                val newPlayer = PlayerScore(playerName = name, winCount = 0, roundsToWin = 0, gamesPlayed = 0, timeStamp = System.currentTimeMillis())
                playerScoreDao.insertScore(newPlayer)
                _currentPlayer.value = newPlayer
            }//end else
        }//end launch
    }//end setPlayerName function

    //function to record a win at end of game
    fun recordWin() {
        //check if the current player is not null
        _currentPlayer.value?.let { player ->
            //update the player with the new win count and game count
            val updated = player.copy(
                winCount = player.winCount + 1,
                gamesPlayed = player.gamesPlayed + 1,
                timeStamp = System.currentTimeMillis()
            )//end updated
            //update the player in the database and update the current player
            viewModelScope.launch {
                playerScoreDao.updateScore(updated)
                _currentPlayer.value = updated
            }//end launch
        }//end let
    }//end recordWin function
}//end PlayerViewModel class