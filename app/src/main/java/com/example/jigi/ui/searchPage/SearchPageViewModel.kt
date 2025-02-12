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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.google.mlkit.vision.digitalink.RecognitionResult
import kotlinx.coroutines.flow.update

class SearchPageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchPageUiState())
    val uiState: StateFlow<SearchPageUiState> = _uiState.asStateFlow()

    val searchOptions: List<SearchOption> = SearchOption.entries.toList()

//    private lateinit var modelIdentifier: DigitalInkRecognitionModelIdentifier
//    private var recognizer: DigitalInkRecognizer
//    private var model: DigitalInkRecognitionModel

    private val remoteModelManager = RemoteModelManager.getInstance()

    private var inkBuilder = Ink.Builder()
    private lateinit var strokeBuilder: Ink.Stroke.Builder


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


//    init {
//        try {
//            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja-JP")!!
//        } catch (e: Exception) {
////            Log.e("Model Exception", e.message.toString())
//        }
//        model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
//        remoteModelManager.download(model, DownloadConditions.Builder().build())
//            .addOnSuccessListener {
////                Log.i("DOGE", "Model downloaded")
//            }
//            .addOnFailureListener { e: Exception ->
////                Log.e("DOGE", "Error while downloading a model: $e")
//            }
//        recognizer = DigitalInkRecognition.getClient(
//            DigitalInkRecognizerOptions.builder(model).build()
//        )
//    }

    fun setHandwritingPadSize(context: Context, size: IntSize) {
        _uiState.update { currentState ->
            currentState.copy(
                canvasSize = size,
            )
        }
        characterRecogniser = CharacterRecognition(_uiState.value.canvasSize)
        characterRecogniser.createInterpreter(context)
    }

//    fun initStrokeBuilder() {
//        strokeBuilder = Ink.Stroke.builder()
////        Log.d("DOGE", strokeBuilder.toString())
//    }
//
//    fun addStrokeToBuilder(x: Float, y: Float) {
////        Log.d("DOGE", "x:${x}, y:${y}")
//        strokeBuilder.addPoint(Ink.Point.create(x, y))
//    }
//
//    fun endStrokeBuilder() {
//        inkBuilder.addStroke(strokeBuilder.build())
//        val ink = inkBuilder.build()
////        Log.d("DOGE", recognizer.toString())
//        recognizer.recognize(ink)
//            .addOnSuccessListener { result: RecognitionResult ->
////                Log.d("DOGE", result.candidates.toString())
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        kanjiList = result.candidates.map { candidate -> candidate.text }
//                            .filter { candidate -> candidate.length == 1 }
//                    )
//                }
//            }
//            .addOnFailureListener { e: Exception ->
////                Log.d("DOGE", "Error during recognition: $e")
//            }
//    }

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
//            val a = linesSize.elementAt(linesSize.lastIndex - 1)
//            val b = linesSize.elementAt(linesSize.lastIndex)
//            Log.d("Lines size ranges", "a=$a, b=$b")
            lines.removeRange(
                linesSize.elementAt(linesSize.lastIndex - 1),
                linesSize.elementAt(linesSize.lastIndex)
            )
            linesSize.removeRange(linesSize.lastIndex - 1, linesSize.lastIndex + 1);
        }
        recognise()
    }


    fun canvasClear() {


        lines.clear()
        linesSize.clear()

//        inkBuilder = Ink.Builder()
//        strokeBuilder = Ink.Stroke.builder()

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