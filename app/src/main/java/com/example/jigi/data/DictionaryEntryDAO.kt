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

    @Query("DELETE FROM Dictionary")
    suspend fun nukeTable()

    @Query("SELECT * FROM Dictionary WHERE word = :word")
    fun getExactWord(word: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE '%' || :word || '%'")
    fun getContainsWord(word: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE :word || '%'")
    fun getForwardsWord(word: String): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM Dictionary WHERE word LIKE '%' || :word")
    fun getBackwardsWord(word: String): Flow<List<DictionaryEntry>>
}