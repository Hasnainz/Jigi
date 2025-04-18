package com.example.jigi.ui.searchPage

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.IntSize
import com.example.jigi.utils.CharacterRecognition
import kotlinx.coroutines.flow.update

class SearchPageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchPageUiState())
    val uiState: StateFlow<SearchPageUiState> = _uiState.asStateFlow()

    val searchOptions: List<SearchOption> = SearchOption.entries.toList()


    var lines = mutableStateListOf<Line>()
        private set

    private var linesSize = mutableStateListOf<Int>()

    private lateinit var characterRecogniser: CharacterRecognition


    var query by mutableStateOf(
        TextFieldValue(
            text = "",
            selection = TextRange(index = 0)
        )
    )




    fun setHandwritingPadSize(context: Context, size: IntSize) {
        _uiState.update { currentState ->
            currentState.copy(
                canvasSize = size,
            )
        }
        characterRecogniser = CharacterRecognition(_uiState.value.canvasSize)
        characterRecogniser.createInterpreter(context)
    }



    fun setSearchOption(searchOption: SearchOption) {
        _uiState.update { currentState ->
            currentState.copy(selectedSearchOption = searchOption)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
            )
        }
    }

    fun canvasUndo() {
        if (linesSize.size >= 2) {
            lines.removeRange(
                linesSize.elementAt(linesSize.lastIndex - 1),
                linesSize.elementAt(linesSize.lastIndex)
            )
            linesSize.removeRange(linesSize.lastIndex - 1, linesSize.lastIndex + 1);
            recognise()
        }
        if (linesSize.size == 0) {
            canvasClear()
        }
    }


    fun canvasClear() {
        lines.clear()
        linesSize.clear()


        _uiState.update { currentState ->
            currentState.copy(
                kanjiList = listOf()
            )
        }

    }

    fun recognise() {
        val results = characterRecogniser.recognise(lines)

        _uiState.update { currentState ->
            currentState.copy(
                kanjiList = results
            )
        }
    }

    fun addLineSize() {
        linesSize.add(lines.size)
    }

    fun addLine(line: Line) {
        lines.add(line)

    }

    fun onQueryChanged(modifiedQuery: TextFieldValue) {
        query = modifiedQuery
    }

    fun clearQuery() {
        query = TextFieldValue(
            text = "",
            selection = TextRange(0)
        )
    }

    fun addKanjiToQuery(kanji: String) {
        val queryWithInsertedKanji: String =
            StringBuilder(query.text).insert(query.selection.start, kanji).toString()
        val updatedCursorIndex = TextRange(index = query.selection.start + 1)
        query = TextFieldValue(
            text = queryWithInsertedKanji,
            selection = updatedCursorIndex
        )
    }


}