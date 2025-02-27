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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jigi.ui.theme.backgroundDark
import com.example.jigi.ui.theme.onBackgroundDark
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

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        ImportDictionaryBar(

            showProgress = uiState.value.isLoadingDictionary || uiState.value.isImportError,
            progressMessage = uiState.value.importStatusMessage,
            progressPercentage = uiState.value.loadingCurrentSize.toFloat() / uiState.value.loadingTotalSize.toFloat(),
            importDictionaryOnClick = { filePickerLauncher.launch("*/*") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        DeleteAllDictionariesBar(
            isDeleted = uiState.value.isDeleted,
            deletedStatusMessage = uiState.value.deleteStatusMessage,
            deleteAlLDictionaries = {
                coroutineScope.launch {
                    settingsViewModel.purgeAllDictionaries()
                }
            },
        )

    }

}


@Composable
fun DeleteAllDictionariesBar(
    isDeleted: Boolean = false,
    deletedStatusMessage: String = "",
    deleteAlLDictionaries: () -> Unit,
    modifier: Modifier = Modifier

) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(color = primaryContainerLight),
    ) {

        Spacer(modifier = Modifier.width(16.dp))
        if(isDeleted) {
            Text(
                text = deletedStatusMessage,
                color = onPrimaryContainerLight,
                modifier = modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        DeleteAllDictionariesButton(deleteAlLDictionaries)
        Spacer(modifier = Modifier.width(16.dp))
    }

}

@Composable
fun ImportDictionaryBar(
    showProgress: Boolean = false,
    progressMessage: String,
    progressPercentage: Float,
    importDictionaryOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(color = primaryContainerLight),
    ) {

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = progressMessage,
            color = onPrimaryContainerLight,
            modifier = modifier.weight(1f)
        )

        if (showProgress) {
            CircularProgressIndicator(
                progress = { progressPercentage },
                color = onPrimaryContainerLight
            )

        }
        Spacer(modifier = Modifier.width(16.dp))
        ImportDictionaryButton(
            importDictionaryOnClick,
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}


@Composable
fun ImportDictionaryButton(importDictionaryOnClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = importDictionaryOnClick,
        containerColor = backgroundDark,
        contentColor = onBackgroundDark,
        modifier = modifier
    ) {
        Icon(painterResource(id = R.drawable.upload2), "Import Dictionary Button.")
    }
}

@Composable
fun DeleteAllDictionariesButton(deleteAlLDictionaries: () -> Unit, modifier: Modifier = Modifier) {
    val openConfirmation = remember { mutableStateOf(false) }
    if (openConfirmation.value) {
        ConfirmPurge(
            onDismiss = { openConfirmation.value = false },
            onConfirm = { deleteAlLDictionaries() })
    }
    FloatingActionButton(
        onClick = { openConfirmation.value = true },
        containerColor = backgroundDark,
        contentColor = onBackgroundDark,
        modifier = modifier
    ) {

        Icon(Icons.Rounded.Delete, "Remove All Dictionaries Button")
    }
}

@Composable
fun ConfirmPurge(onDismiss: () -> Unit, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        icon = {
            Icon(Icons.Rounded.DeleteForever, contentDescription = "Purge Dictionary")
        },
        title = {
            Text(text = "Delete all dictionaries?")
        },
        text = {
            Text(text = "Once deleted this action cannot be undone and any dictionary files must be uploaded again.")
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

