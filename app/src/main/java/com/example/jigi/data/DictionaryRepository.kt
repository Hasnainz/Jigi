package com.example.jigi.data

import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun insert(entry: DictionaryEntry)

    suspend fun update(entry: DictionaryEntry)

    suspend fun delete(entry: DictionaryEntry)

    fun getExactWord(word: String): Flow<List<DictionaryEntry>>

    fun getContainsWord(word: String): Flow<List<DictionaryEntry>>

    fun getForwardsWord(word: String): Flow<List<DictionaryEntry>>

    fun getBackwardsWord(word: String): Flow<List<DictionaryEntry>>

}