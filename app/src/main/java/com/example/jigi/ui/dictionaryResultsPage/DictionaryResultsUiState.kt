package com.example.jigi.ui.dictionaryResultsPage

import com.example.jigi.ui.searchPage.SearchOption

data class DictionaryResultsUiState(
    val selectedSearchOption: SearchOption = SearchOption.Contains,
    val query: String = ""
)