package com.example.jigi.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun insert(entry: DictionaryEntry)

    suspend fun update(entry: DictionaryEntry)

    suspend fun delete(entry: DictionaryEntry)

    suspend fun removeDictionary(dictionary: String)

    suspend fun nukeTable()

    fun getExactWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    fun getContainsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    fun getForwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    fun getBackwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    fun getUniqueDictionaries(): Flow<List<String>>

}