package com.example.jigi.ui.dictionaryResultsPage

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import com.example.jigi.R
import com.example.jigi.ui.theme.onPrimaryContainerDark
import com.example.jigi.ui.theme.primaryContainerDark


@Composable
fun DictionaryResultsPage(
    dictionaryResultsViewModel: DictionaryResultsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState = dictionaryResultsViewModel.uiState.collectAsState()
    Text("Query: \"${uiState.value.query}\", Option: ${uiState.value.selectedSearchOption}")
}