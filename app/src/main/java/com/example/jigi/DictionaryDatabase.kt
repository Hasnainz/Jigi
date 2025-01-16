package com.example.jigi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DictionaryEntry::class], version = 1, exportSchema = false)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun dictionaryEntryDAO() : DictionaryEntryDAO

    companion object {
        @Volatile
        private var Instance: DictionaryDatabase? = null

        fun getDatabase(context: Context) : DictionaryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DictionaryDatabase::class.java, "dictionary_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }



}