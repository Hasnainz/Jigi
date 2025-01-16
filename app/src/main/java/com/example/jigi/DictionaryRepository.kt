package com.example.jigi

import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun insert(entry: DictionaryEntry)

    suspend fun update(entry: DictionaryEntry)

    suspend fun delete(entry: DictionaryEntry)

    fun getExactWord(word: String): Flow<DictionaryEntry>

    fun getContainsWord(word: String): Flow<DictionaryEntry>

    fun getForwardsWord(word: String): Flow<DictionaryEntry>

    fun getBackwardsWord(word: String): Flow<DictionaryEntry>

}