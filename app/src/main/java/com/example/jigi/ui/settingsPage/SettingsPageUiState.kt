package com.example.jigi.ui.settingsPage

data class SettingsPageUiState(
    val statusMessage: String = "",
    val loadingCurrentSize: Int = 0,
    val loadingTotalSize: Int = 0,
    val isLoadingDictionary: Boolean = false,
    val isImportError: Boolean = false,
)
