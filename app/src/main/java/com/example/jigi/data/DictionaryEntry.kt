package com.example.jigi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dictionary", primaryKeys = ["dictionary", "word"])
data class DictionaryEntry(
    val dictionary : String,
    val word : String,
    val reading : String,
    val type : String,
    val definition : String
)
