package com.michaelpressel.cardwar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michaelpressel.cardwar.data.PlayerScoreDao
//ViewModelFactory to create PlayerViewModel
class PlayerViewModelFactory(
    //dao parameter
    private val dao: PlayerScoreDao
    //returns ViewModelProvider.Factory
): ViewModelProvider.Factory {
    //create function to create PlayerViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //check if modelClass is assignable from PlayerViewModel
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            //create and return a new instance of PlayerViewModel
            return PlayerViewModel(dao) as T
        }//end if
        throw IllegalArgumentException("Unknown ViewModel class")
    }//end create function
}//end PlayerViewModelFactory class