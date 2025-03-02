package com.example.jigi.ui.settingsPage

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jigi.data.DictionaryEntry
import com.example.jigi.data.DictionaryRepository
import com.example.jigi.utils.ImportDictionary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.zip.ZipInputStream

class SettingsViewModel(private val dictionaryRepository: DictionaryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsPageUiState())
    val uiState: StateFlow<SettingsPageUiState> = _uiState.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun uploadDictionary(context: Context, contentUri: Uri) {

        try {
            val fileReader = FileReader(context = context)
            _uiState.update { currentState ->
                currentState.copy(
                    isLoadingDictionary = true,
                    isImportError = false,
                    importStatusMessage = "Reading the dictionary file."
                )
            }
            val fileInfo = fileReader.uriToFileInfo(contentUri)
            _uiState.update { currentState ->
                currentState.copy(importStatusMessage = "Validating the dictionary file's format.")
            }
            val dictionaryImporter = ImportDictionary()
            _uiState.update { currentState ->
                currentState.copy(importStatusMessage = "Creating the dictionary importer.")
            }
            val dictionary = dictionaryImporter.loadDictionary(fileInfo)
            _uiState.update { currentState ->
                currentState.copy(importStatusMessage = "Importing the dictionary into the database. Warning, this may take a while.")
            }
            insertDictionaryIntoDatabase(dictionary)
            _uiState.update { currentState ->
                currentState.copy(
                    importStatusMessage = "Dictionary has been successfully imported.",
                    loadingCurrentSize = 0,
                    loadingTotalSize = 1,
                    isLoadingDictionary = false,
                    )
            }
            updateExistingDictionaries()
        } catch (e: Exception) {
            _uiState.update { currentState ->
                currentState.copy(
                    importStatusMessage = "Error in loading the dictionary.",
                    isLoadingDictionary = false,
                    isImportError = false,
                    loadingCurrentSize = 0,
                    loadingTotalSize = 1,
                )
            }

        }

    }

    fun toggleSelectedDictionary(dictionary: String) {
        val tmpMap = _uiState.value.isSelected.toMutableMap()
        if (dictionary in tmpMap) {
            tmpMap[dictionary] = !tmpMap[dictionary]!!
        }
        _uiState.update { currentState ->
            currentState.copy(
                isSelected = tmpMap
            )
        }
    }

    fun updateExistingDictionaries() {
        viewModelScope.launch {
            val currentDictionaries = dictionaryRepository.getUniqueDictionaries().filterNotNull().first()
            val tmpMap = _uiState.value.isSelected.toMutableMap()
            currentDictionaries.forEach{
                if (!tmpMap.containsKey(it)) {
                    tmpMap[it] = true
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    existingDictionaries = currentDictionaries,
                    isSelected = tmpMap
                )
            }
        }

    }

    private suspend fun insertDictionaryIntoDatabase(dictionary: ImportDictionary.Dictionary) {
        _uiState.update { currentState ->
            currentState.copy(loadingTotalSize = dictionary.entries.size)
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

    suspend fun removeSelectedDictionaries() {
        val selectedDictionaries = _uiState.value.isSelected.filter { (key, value) -> value }
        selectedDictionaries.keys.forEach { dict ->
            dictionaryRepository.removeDictionary(dict)
        }
        _uiState.update { currentState ->
            currentState.copy(
                isDeleted = true,
                deleteStatusMessage = "Selected dictionaries have been removed."
            )
        }
        updateExistingDictionaries()
    }

    suspend fun purgeAllDictionaries() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleted = true,
                deleteStatusMessage = "Selected dictionaries have been removed."
            )
        }
        dictionaryRepository.nukeTable()
        updateExistingDictionaries()
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
