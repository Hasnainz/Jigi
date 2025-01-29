package com.example.jigi.ui.dictionaryResultsPage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsViewModel
import com.example.jigi.ui.theme.onTertiaryContainerLight
import com.example.jigi.ui.theme.tertiaryContainerLight
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import com.example.jigi.AppViewModelProvider
import com.example.jigi.R
import com.example.jigi.ui.searchPage.SearchPageViewModel
import com.example.jigi.ui.theme.onPrimaryContainerDark
import com.example.jigi.ui.theme.primaryContainerDark
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DictionaryResultsPage(
    searchPageViewModel: SearchPageViewModel = viewModel(),
    dictionaryResultsViewModel: DictionaryResultsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val searchPageState = searchPageViewModel.uiState.collectAsState()


    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        dictionaryResultsViewModel.search(searchPageState.value.query, searchPageState.value.selectedSearchOption)
    }

    val queryResult = dictionaryResultsViewModel.dictionaryUiState.resultList
    Column {
        Text("Query: \"${searchPageState.value.query}\", Option: ${searchPageState.value.selectedSearchOption}")
        Text("$queryResult")
    }

}