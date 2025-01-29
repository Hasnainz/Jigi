package com.example.jigi

import android.app.Application
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DictionaryResultsViewModel(dictionaryApplication().container.dictionaryRepository)
        }
    }
}

fun CreationExtras.dictionaryApplication(): DictionaryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DictionaryApplication)