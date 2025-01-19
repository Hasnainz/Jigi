package com.example.jigi.ui.searchPage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jigi.R
import com.example.jigi.ui.theme.onBackgroundDark
import com.example.jigi.ui.theme.onPrimaryContainerDark
import com.example.jigi.ui.theme.onPrimaryContainerLight
import com.example.jigi.ui.theme.onSecondaryContainerLight
import com.example.jigi.ui.theme.onTertiaryContainerLight
import com.example.jigi.ui.theme.primaryContainerDark
import com.example.jigi.ui.theme.primaryContainerLight
import com.example.jigi.ui.theme.secondaryContainerLight
import com.example.jigi.ui.theme.tertiaryContainerLight


enum class SearchScreen() {

}

@Composable
fun SearchPage(
    searchPageViewModel: SearchPageViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val searchPageUiState by searchPageViewModel.uiState.collectAsState()


    Column(
        modifier.padding(start = 20.dp, end = 20.dp)
    )
    {
        HandwritingPad(
            modifier
                .padding(top = 40.dp, bottom = 16.dp)
                .height(400.dp),
            lines = searchPageViewModel.lines,
            addLineSize = { searchPageViewModel.addLineSize() },
            addLine = { searchPageViewModel.addLine(it) }
        )
        SearchBar(
            query = searchPageViewModel.query,
            onQueryChange = { searchPageViewModel.onQueryChanged(it) },
            clearQuery = { searchPageViewModel.clearQuery() },
            modifier = modifier.padding()
        )

        Row()
        {
            KanjiGrid(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 60.dp),
                kanji = searchPageViewModel.kanjiList,
                addKanjiToQuery = { searchPageViewModel.addKanjiToQuery(it) },
            )
            ExtraButtonsGrid(
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 16.dp),
                onUndoClicked = { searchPageViewModel.canvasUndo() },
                onCanvasClear = { searchPageViewModel.canvasClear() },
                searchOptions = searchPageViewModel.searchOptions,
                selectedSearchOption = searchPageUiState.selectedSearchOption,
                selectSearchOption = { searchPageViewModel.selectSearchOption(it) },
            )
        }

    }
}

@Composable
fun HandwritingPad(
    modifier: Modifier = Modifier,
    lines: List<Line> = emptyList(),
    addLineSize: () -> Unit,
    addLine: (Line) -> Unit
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(primaryContainerLight)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { addLineSize() },
                    onDragEnd = { addLineSize() },
                    onDragCancel = { addLineSize() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position
                        )
                        addLine(line)
                    }
                )
            }
    )
    {
        lines.forEach { line ->
            drawLine(
                color = onPrimaryContainerLight,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
fun AboutIcon(modifier: Modifier = Modifier) {
    SmallFloatingActionButton(
        onClick = { },
        containerColor = Color.Transparent,
        contentColor = onBackgroundDark,
        modifier = modifier
    ) {
        Icon(Icons.Rounded.Info, "Settings Button.")
    }
}


@Composable
fun KanjiGrid(
    modifier: Modifier = Modifier,
    kanji: List<String>,
    addKanjiToQuery: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(items = kanji) {
            Kanji(kanji = it, onKanjiSelect = { addKanjiToQuery(it) })
        }
    }
}


@Composable
fun Kanji(modifier: Modifier = Modifier, kanji: String, onKanjiSelect: () -> Unit) {
    TextButton(
        onClick = onKanjiSelect,
        modifier = modifier
    ) {
        Text(
            color = onBackgroundDark,
            text = kanji,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun ExtraButtonsGrid(
    modifier: Modifier = Modifier,
    searchOptions: List<SearchOption> = SearchOption.entries,
    selectedSearchOption: SearchOption,
    selectSearchOption: (SearchOption) -> Unit,
    onUndoClicked: () -> Unit,
    onCanvasClear: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {
        SearchParametersDropDown(
            searchOptions = searchOptions,
            selectedSearchOption = selectedSearchOption,
            selectSearchOption = selectSearchOption,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            SearchHistoryButton(Modifier.padding(end = 8.dp))
            SettingsButton(Modifier.padding())
        }

        Row {
            UndoCanvasButton(Modifier.padding(end = 8.dp), onUndoClicked = onUndoClicked)
            ClearCanvasButton(onCanvasClear = onCanvasClear)
        }
        AboutIcon()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchParametersDropDown(
    selectedSearchOption: SearchOption,
    searchOptions: List<SearchOption> = SearchOption.entries,
    selectSearchOption: (SearchOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        TextField(
            textStyle = MaterialTheme.typography.labelSmall,
            modifier = modifier
                .menuAnchor()
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(20.dp)),
            readOnly = true,
            value = selectedSearchOption.name,
            onValueChange = {},
            trailingIcon = {
                if (expanded) {
                    Icon(
                        Icons.Rounded.ExpandLess,
                        contentDescription = "Expand Less",
                        tint = onPrimaryContainerLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Icon(
                        Icons.Rounded.ExpandMore,
                        contentDescription = "Expand More",
                        tint = onPrimaryContainerLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                    )
                }

            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = secondaryContainerLight,
                focusedContainerColor = secondaryContainerLight,

                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,

                unfocusedTextColor = onSecondaryContainerLight,
                focusedTextColor = onSecondaryContainerLight,
                cursorColor = onSecondaryContainerLight,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier
                .background(secondaryContainerLight)
                .padding(start = 8.dp)
        ) {
            searchOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name, color = onSecondaryContainerLight) },
                    onClick = {
                        selectSearchOption(selectionOption)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun UndoCanvasButton(modifier: Modifier = Modifier, onUndoClicked: () -> Unit) {
    FloatingActionButton(
        onClick = onUndoClicked,
        containerColor = tertiaryContainerLight,
        contentColor = onTertiaryContainerLight,
        modifier = modifier
    ) {
        Icon(painterResource(id = R.drawable.undo), "Undo button.")
    }
}

@Composable
fun ClearCanvasButton(modifier: Modifier = Modifier, onCanvasClear: () -> Unit) {
    FloatingActionButton(
        onClick = onCanvasClear,
        containerColor = tertiaryContainerLight,
        contentColor = onTertiaryContainerLight,
        modifier = modifier
    ) {
        Icon(Icons.Rounded.Clear, "Clear button.")
    }
}

@Composable
fun ToggleConjugationButton(
    modifier: Modifier = Modifier,
    toggleConjugation: () -> Unit,
    conjugate: Boolean
) {
    FloatingActionButton(
        onClick = toggleConjugation,
        containerColor = primaryContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier
    ) {

        Icon(
            if (conjugate) Icons.Rounded.Shuffle else Icons.Rounded.CheckBoxOutlineBlank,
            "Import Dictionary Button."
        )
    }
}

@Composable
fun SearchHistoryButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { },
        containerColor = secondaryContainerLight,
        contentColor = onSecondaryContainerLight,
        modifier = modifier
    ) {
        Icon(Icons.Rounded.History, "Import Dictionary Button.")
    }
}

@Composable
fun ImportDictionaryButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { },
        containerColor = primaryContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier
    ) {
        Icon(painterResource(id = R.drawable.upload2), "Import Dictionary Button.")
    }
}

@Composable
fun SettingsButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { },
        containerColor = secondaryContainerLight,
        contentColor = onSecondaryContainerLight,
        modifier = modifier
    ) {
        Icon(Icons.Rounded.Settings, "Settings Button.")
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    clearQuery: () -> Unit
) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .clip(RoundedCornerShape(20.dp))

    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState()),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            trailingIcon = {
                Icon(
                    Icons.Rounded.Clear,
                    contentDescription = "Clear Query",
                    tint = onPrimaryContainerDark,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { clearQuery() })
            },


            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = primaryContainerDark,
                focusedContainerColor = primaryContainerDark,

                unfocusedIndicatorColor = primaryContainerDark,
                focusedIndicatorColor = primaryContainerDark,

                focusedTextColor = onPrimaryContainerDark,
                cursorColor = onPrimaryContainerDark,
            ),
            shape = RectangleShape
        )
        IconButton(
            onClick = {},
            modifier = Modifier
                .background(color = primaryContainerLight)
                .fillMaxHeight()

        ) {
            Icon(
                Icons.Rounded.Search,
                tint = onPrimaryContainerLight,
                contentDescription = "Search Button"
            )
        }
    }
}
