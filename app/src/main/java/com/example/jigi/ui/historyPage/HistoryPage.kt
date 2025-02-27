package com.example.jigi.ui.historyPage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.jigi.ui.dictionaryResultsPage.DictionaryEntryCard
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsViewModel
import com.example.jigi.ui.searchPage.SearchPageViewModel
import com.example.jigi.viewprovider.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun HistoryPage(
    modifier: Modifier = Modifier
) {
    Column() {
        Text("History Page")
    }


}
