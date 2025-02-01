package com.example.jigi.ui.settingsPage

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jigi.R
import com.example.jigi.ui.theme.onPrimaryContainerLight
import com.example.jigi.ui.theme.primaryContainerLight
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.room.Delete
import com.example.jigi.ui.searchPage.Kanji
import com.example.jigi.viewprovider.AppViewModelProvider
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingsPage(
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiState = settingsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { contentUri ->
        contentUri?.let {
            coroutineScope.launch {
                settingsViewModel.uploadDictionary(context = context, contentUri = contentUri)
            }
        }
    }



    Column {
        if (uiState.value.isLoadingDictionary) {
            Column {
                Text(uiState.value.statusMessage)
                Text("${uiState.value.loadingCurrentSize}/${uiState.value.loadingTotalSize}")
            }

        }
        ImportDictionaryButton(importDictionaryOnClick = {
            filePickerLauncher.launch("*/*")
        })
        DeleteAllDictionaries(deleteAlLDictionaries = {
            coroutineScope.launch {
                settingsViewModel.purgeAllDictionaries()
            }
        })
    }

}

@Composable
fun ImportDictionaryButton(importDictionaryOnClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = importDictionaryOnClick,
        containerColor = primaryContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier
    ) {
        Icon(painterResource(id = R.drawable.upload2), "Import Dictionary Button.")
    }
}
@Composable
fun DeleteAllDictionaries(deleteAlLDictionaries: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = deleteAlLDictionaries,
        containerColor = primaryContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier
    ) {

        Icon(Icons.Rounded.Delete, "Remove All Dictionaries Button")
    }
}
