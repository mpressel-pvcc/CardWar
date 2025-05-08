
//Room Database to hold player information
//PlayerScore is the Entity class for the database
//Version 2 because we added a total games played column
package com.michaelpressel.cardwar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerScore::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //function to return the DAO
    abstract fun playerScoreDao(): PlayerScoreDao
    //companion object to create a singleton instance of the database
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //function to get the database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "card_war_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }//end synchronized
        }//end getDatabase
    }//end companion object
}//end AppDatabase class