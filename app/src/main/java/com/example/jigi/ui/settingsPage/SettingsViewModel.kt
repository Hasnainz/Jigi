package com.example.jigi.ui.settingsPage

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.jigi.data.DictionaryEntry
import com.example.jigi.data.DictionaryRepository
import com.example.jigi.utils.ImportDictionary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.zip.ZipInputStream

class SettingsViewModel(private val dictionaryRepository: DictionaryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsPageUiState())
    val uiState: StateFlow<SettingsPageUiState> = _uiState.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun uploadDictionary(context: Context, contentUri: Uri) {


        val fileReader = FileReader(context = context)
        _uiState.update { currentState ->
            currentState.copy(isLoadingDictionary = true, statusMessage = "FileReader Created")
        }
        val fileInfo = fileReader.uriToFileInfo(contentUri)
        _uiState.update { currentState ->
            currentState.copy(statusMessage = "Dictionary file has been read")
        }
        val dictionaryImporter = ImportDictionary()
        _uiState.update { currentState ->
            currentState.copy(statusMessage = "Dictionary Importer Created")
        }
        val dictionary = dictionaryImporter.loadDictionary(fileInfo)
        _uiState.update { currentState ->
            currentState.copy(statusMessage = "Dictionary Loaded")
        }
        insertDictionaryIntoDatabase(dictionary)
        _uiState.update { currentState ->
            currentState.copy(statusMessage = "Dictionary loaded into memory",)
        }


    }

    private suspend fun insertDictionaryIntoDatabase(dictionary: ImportDictionary.Dictionary) {
        _uiState.update { currentState ->
            currentState.copy(loadingTotalSize=dictionary.entries.size)
        }

        for (entry in dictionary.entries) {
            dictionaryRepository.insert(
                DictionaryEntry(
                    dictionary = dictionary.metadata.title,
                    word = entry.word,
                    reading = entry.reading,
                    type = entry.adjective,
                    definition = entry.definition.joinToString("\n")
                )
            )
            _uiState.update { currentState ->
                currentState.copy(loadingCurrentSize = currentState.loadingCurrentSize + 1)
            }

        }
    }

    suspend fun purgeAllDictionaries() {
        Log.d("Doge", "deleting the entire table")
        dictionaryRepository.nukeTable()
    }

}

class FileReader(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun uriToFileInfo(contentUri: Uri): ImportDictionary.ZipFilesWithMetaData {
        return withContext(ioDispatcher) {

            val zipEntries: MutableList<ImportDictionary.ZipEntryWithByteData> = mutableListOf()
            context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    val zipInputStream = ZipInputStream(inputStream)
                    var currentEntry = zipInputStream.nextEntry

                    while (currentEntry != null) {
                        val content = zipInputStream.readAllBytes()
                        zipEntries.add(
                            ImportDictionary.ZipEntryWithByteData(
                                currentEntry,
                                content
                            )
                        )
                        currentEntry = zipInputStream.nextEntry
                    }
                }


            val fileName = UUID.randomUUID().toString()
            val mimeType = context.contentResolver.getType(contentUri) ?: ""

            ImportDictionary.ZipFilesWithMetaData(
                name = fileName,
                mimeType = mimeType,
                zipEntries = zipEntries
            )
        }
    }
}
