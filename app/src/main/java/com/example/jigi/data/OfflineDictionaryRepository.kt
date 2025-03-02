package com.example.jigi.data

import kotlinx.coroutines.flow.Flow

class OfflineDictionaryRepository(private val dictEntryDAO: DictionaryEntryDAO) :
    DictionaryRepository {
    override suspend fun insert(entry: DictionaryEntry) = dictEntryDAO.insert(entry)

    override suspend fun update(entry: DictionaryEntry) = dictEntryDAO.update(entry)

    override suspend fun delete(entry: DictionaryEntry) = dictEntryDAO.delete(entry)

    override suspend fun nukeTable() = dictEntryDAO.nukeTable()

    override suspend fun removeDictionary(dictionary: String) = dictEntryDAO.removeDictionary(dictionary)

    override fun getExactWord(word: String, dictionary: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getExactWord(word, dictionary)

    override fun getContainsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getContainsWord(word, dictionary)

    override fun getForwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getForwardsWord(word, dictionary)

    override fun getBackwardsWord(word: String, dictionary: String): Flow<List<DictionaryEntry>> = dictEntryDAO.getBackwardsWord(word, dictionary)

    override fun getUniqueDictionaries(): Flow<List<String>> = dictEntryDAO.getUniqueDictionaries()

}