package com.example.jigi.ui.searchPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.jigi.ui.theme.onPrimaryContainerLight


data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = onPrimaryContainerLight,
    val strokeWidth: Dp = 4.dp
)

enum class SearchOption{
    Contains,
    Exact,
    Forwards,
}

data class SearchPageUiState(
    val selectedSearchOption: SearchOption = SearchOption.Contains,
    val query: String = "",
    val kanjiList: List<String> = listOf(),
    val canvasSize: IntSize = IntSize.Zero
)