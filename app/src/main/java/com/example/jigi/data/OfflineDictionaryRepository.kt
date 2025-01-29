package com.example.jigi.data

import kotlinx.coroutines.flow.Flow

class OfflineDictionaryRepository(private val dictEntryDAO: DictionaryEntryDAO) :
    DictionaryRepository {
    override suspend fun insert(entry: DictionaryEntry) = dictEntryDAO.insert(entry)

    override suspend fun update(entry: DictionaryEntry) = dictEntryDAO.update(entry)

    override suspend fun delete(entry: DictionaryEntry) = dictEntryDAO.delete(entry)

    override fun getExactWord(word: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getExactWord(word)

    override fun getContainsWord(word: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getContainsWord(word)

    override fun getForwardsWord(word: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getForwardsWord(word)

    override fun getBackwardsWord(word: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getBackwardsWord(word)

}