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
import java.nio.charset.Charset
import java.util.UUID
import java.util.zip.ZipInputStream

class SettingsViewModel(private val dictionaryRepository: DictionaryRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun uploadDictionary(context: Context, contentUri: Uri) {
        val fileReader = FileReader(context = context)
        val fileInfo = fileReader.uriToFileInfo(contentUri)
        val dictionaryImporter = ImportDictionary()
        val dictionary = dictionaryImporter.loadDictionary(fileInfo)
        insertDictionaryIntoDatabase(dictionary)
    }
    private suspend fun insertDictionaryIntoDatabase(dictionary: ImportDictionary.Dictionary) {
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
        }
    }
    suspend fun purgeAllDictionaries() {
        dictionaryRepository.nukeTable()
    }

}

class FileReader(
    private val context: Context
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun uriToFileInfo(contentUri: Uri): ImportDictionary.ZipFilesWithMetaData {
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

        return ImportDictionary.ZipFilesWithMetaData(
            name = fileName,
            mimeType = mimeType,
            zipEntries = zipEntries
        )
    }
}


data class ProgressUpdate(
    val bytesSent: Long,
    val totalBytes: Long
)