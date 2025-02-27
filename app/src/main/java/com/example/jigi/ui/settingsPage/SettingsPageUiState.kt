package com.example.jigi.ui.settingsPage

data class SettingsPageUiState(
    val importStatusMessage: String = "Upload your own dictionaries here",
    val deleteStatusMessage: String = "Delete all selected dictionaries here",
    val isDeleted: Boolean = false,
    val loadingCurrentSize: Int = 0,
    val loadingTotalSize: Int = 1,
    val isLoadingDictionary: Boolean = false,
    val isImportError: Boolean = false,
)
