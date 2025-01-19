package com.example.jigi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dictionary")
data class DictionaryEntry(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val dictionary : String,
    val word : String,
    val reading : String,
    val type : String,
    val definition : String
)
