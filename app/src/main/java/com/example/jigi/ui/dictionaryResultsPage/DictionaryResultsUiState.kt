package com.example.jigi.ui.dictionaryResultsPage

import com.example.jigi.data.DictionaryEntry
import com.example.jigi.ui.searchPage.SearchOption

data class DictionaryResultsUiState(
    val resultList: List<DictionaryEntry> = listOf()
)
