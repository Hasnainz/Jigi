package com.example.jigi.ui.dictionaryResultsPage

import androidx.lifecycle.ViewModel
import com.example.jigi.ui.searchPage.SearchOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DictionaryResultsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DictionaryResultsUiState())
    val uiState: StateFlow<DictionaryResultsUiState> = _uiState.asStateFlow()


    fun setSearchOption(option: SearchOption) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedSearchOption = option,
            )
        }
    }


    fun setSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
            )
        }
    }



}