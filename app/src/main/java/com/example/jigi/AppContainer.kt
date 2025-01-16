package com.example.jigi

import android.content.Context

interface AppContainer {
    val dictionaryRepository: DictionaryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val dictionaryRepository : DictionaryRepository by lazy {
        OfflineDictionaryRepository(DictionaryDatabase.getDatabase(context).dictionaryEntryDAO())
    }
}