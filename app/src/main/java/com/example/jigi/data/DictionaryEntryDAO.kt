package com.example.jigi.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryEntryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DictionaryEntry)

    @Update
    suspend fun update(entry: DictionaryEntry)

    @Delete
    suspend fun delete(entry: DictionaryEntry)

    @Query("DELETE FROM Dictionary WHERE dictionary = :dictionary")
    suspend fun removeDictionary(dictionary: String)

    @Query("DELETE FROM Dictionary")
    suspend fun nukeTable()

    @Query("SELECT * FROM Dictionary WHERE word = :word AND dictionary = :dictionary")
    fun getExactWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE '%' || :word || '%' AND dictionary = :dictionary")
    fun getContainsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE :word || '%' AND dictionary = :dictionary")
    fun getForwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE '%' || :word AND dictionary = :dictionary")
    fun getBackwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>>

    @Query("SELECT DISTINCT dictionary FROM Dictionary")
    fun getUniqueDictionaries(): Flow<List<String>>
}