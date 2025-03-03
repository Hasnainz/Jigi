package com.example.jigi.ui.dictionaryResultsPage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jigi.data.DictionaryEntry
import com.example.jigi.data.DictionaryRepository
import com.example.jigi.ui.searchPage.SearchOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DictionaryResultsViewModel(private val dictionaryRepository: DictionaryRepository) :
    ViewModel() {

    var dictionaryUiState by mutableStateOf(DictionaryResultsUiState())
        private set


    fun search(query: String, option: SearchOption, searchDictionaries: Map<String, Boolean>) {
        if (query == "") {
            return
        }
        val dictionariesToSearch = searchDictionaries.filter { (_, value) -> value }
        val resultingList: MutableList<DictionaryEntry> = mutableListOf()
        viewModelScope.launch {
            dictionariesToSearch.keys.forEach { dict ->
                resultingList.addAll(
                    when (option) {
                        SearchOption.Contains -> dictionaryRepository.getContainsWord(query, dict)
                            .filterNotNull()
                            .first()

                        SearchOption.Exact -> dictionaryRepository.getExactWord(query, dict)
                            .filterNotNull()
                            .first()

                        SearchOption.Forwards -> dictionaryRepository.getForwardsWord(query, dict)
                                .filterNotNull()
                                .first()
                    }
                )
            }
            dictionaryUiState = DictionaryResultsUiState(
                resultingList
            )
        }
    }


}
