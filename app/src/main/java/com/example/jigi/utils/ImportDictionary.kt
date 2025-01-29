package com.example.jigi.utils



import java.util.zip.ZipFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.io.File
import java.nio.charset.Charset

class ImportDictionary {

    data class Dictionary(
        var entries: MutableList<DictionaryEntry> = mutableListOf(),
        var metadata: DictionaryMetaData = DictionaryMetaData(),
    )

    @Serializable
    data class DictionaryEntry(
        val word: String = "",
        val reading: String = "",
        val empty: String = "",
        val adjective: String = "",
        val blank2: Int = 0,
        val definition: List<String> = listOf(),
        val index: Int = 0,
        val blank3: String = "",
    )

    @Serializable
    data class DictionaryMetaData(
        val title: String = "",
        val format: Int = 0,
        val revision: String = "",
        val sequenced: Boolean = false,
    )


    fun getDictionaries(): List<Dictionary> {
        val pathnames: List<String> = listOf(
            "./src/main/resources/dictionaries/新明解国語辞典第五版v3.zip",
            "./src/main/resources/dictionaries/明鏡国語辞典.zip"
        )
        val dictionaries: List<Dictionary> = loadDictionaries(pathnames)
        for (dictionary in dictionaries) {
            println(dictionary.metadata.title)
        }
        return dictionaries
    }


    private fun loadDictionaries(pathnames: List<String>): List<Dictionary> {
        val dictionaries: MutableList<Dictionary> = mutableListOf()
        for (pathname in pathnames) {

            val dictionary = Dictionary()

            val dictionaryLocation = File(pathname)
            val zippedDictionary = ZipFile(dictionaryLocation)

            for (file in zippedDictionary.entries()) {
                val inputStream = zippedDictionary.getInputStream(file)
                val inputString = inputStream.readBytes().toString(Charset.defaultCharset())
                if (file.name != "index.json") {
                    dictionary.entries.addAll(loadDictionaryFromString(inputString))
                } else {
                    dictionary.metadata = loadMetadataFromString(inputString)
                }
                inputStream.close()
            }
            dictionaries.add(dictionary)
        }
        return dictionaries
    }

    private fun loadDictionaryFromString(inputString: String): List<DictionaryEntry> {
        val entries: MutableList<DictionaryEntry> = mutableListOf()

        val myJson: List<JsonArray> = Json.decodeFromString(inputString)

        for (l in myJson) {
            val dictEntry = stringEntryToDictionaryEntry(l)
            entries += dictEntry
        }
        return entries
    }

    private fun loadMetadataFromString(inputString: String): DictionaryMetaData {
        val metadata: DictionaryMetaData = Json.decodeFromString(inputString)
        return metadata
    }

    private fun stringEntryToDictionaryEntry(entry: JsonArray): DictionaryEntry {
        return DictionaryEntry(
            word = Json.decodeFromJsonElement(entry[0]),
            reading = Json.decodeFromJsonElement(entry[1]),
            empty = Json.decodeFromJsonElement(entry[2]),
            adjective = Json.decodeFromJsonElement(entry[3]),
            blank2 = Json.decodeFromJsonElement(entry[4]),
            definition = Json.decodeFromJsonElement(entry[5]),
            index = Json.decodeFromJsonElement(entry[6]),
            blank3 = Json.decodeFromJsonElement(entry[7]),
        )
    }


}