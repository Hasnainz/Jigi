package com.example.jigi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jigi.ui.theme.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.colorResource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                SearchPage()
            }

        }
    }
}
//TODO :: Search page with dictionary
//TODO :: Settings page for Dictionary
//TODO :: Search Options, Conjugation Settings

@Composable
fun SearchPage(modifier: Modifier = Modifier) {
    val lines = remember { mutableStateListOf<Line>() }
    val linesSize = remember { mutableStateListOf<Int>() }
    val query = remember { mutableStateOf(
        TextFieldValue(
            text = "",
            selection = TextRange(0)
        )
    ) }
    var conjugation by remember { mutableStateOf(false) }
    val kanji = remember { mutableStateListOf("巨", "手", "描", "ホ", "言", "語", "爆", "笑", "間", "週","巨", "手", "描", "ホ", "言", "語", "爆", "笑", "間", "週","巨", "手", "描", "ホ", "言", "語", "爆", "笑", "間", "週","巨", "手", "描", "ホ", "言", "語", "爆", "笑", "間", "週") }

    Column(
        modifier.padding(start = 20.dp, end = 20.dp)
    )
    {
        HandwritingPad(
            modifier
                .padding(top = 40.dp, bottom = 16.dp)
                .height(400.dp),
                lines,
            linesSize)
        SearchBar(modifier.padding(), query)

        Row()
        {
            KanjiGrid(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 60.dp),
                kanji = kanji,
                query = query
                )
            ExtraButtonsGrid(modifier = Modifier
                .weight(2f)
                .padding(top = 16.dp),
                onUndoClicked = {
                    if(linesSize.size >= 2){
                        val a = linesSize.elementAt(linesSize.lastIndex - 1)
                        val b = linesSize.elementAt(linesSize.lastIndex)
                        Log.d("Lines size ranges", "a=$a, b=$b")
                        lines.removeRange(linesSize.elementAt(linesSize.lastIndex-1), linesSize.elementAt(linesSize.lastIndex))
                        linesSize.removeRange(linesSize.lastIndex - 1, linesSize.lastIndex + 1);
                    } },
                onCanvasClear = { lines.clear()
                    linesSize.clear() },
                toggleConjugation = { conjugation = !conjugation },
                conjugate = conjugation,
                )
        }

    }
}

@Composable
fun HandwritingPad(modifier: Modifier = Modifier, lines: SnapshotStateList<Line> = mutableStateListOf(), linesSize: SnapshotStateList<Int> = mutableStateListOf()) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(primaryContainerLight)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { linesSize.add(lines.size) },
                    onDragEnd = { linesSize.add(lines.size) },
                    onDragCancel = { linesSize.add(lines.size) },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position
                        )
                        lines.add(line)
                    }
                )
            }
//            .pointerInput(Unit) {
//                detectDragGestures  { change, dragAmount ->
//                    change.consume()
//                    val line = Line(
//                        start = change.position - dragAmount,
//                        end = change.position
//                    )
//                    lines.add(line)
//                }
//            }
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

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = onPrimaryContainerLight,
    val strokeWidth: Dp = 6.dp
)


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
fun KanjiGrid(modifier: Modifier = Modifier, kanji: List<String>, query: MutableState<TextFieldValue>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(items = kanji) { k ->
            val mutatedQuery = StringBuilder(query.value.text).insert(query.value.selection.start, k).toString()
            Kanji(kanji = k, onKanjiSelect = { query.value = TextFieldValue(text = mutatedQuery, selection = TextRange(query.value.selection.start + 1)) })
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
fun ExtraButtonsGrid(modifier: Modifier = Modifier, onUndoClicked: () -> Unit, onCanvasClear: () -> Unit, toggleConjugation: () -> Unit, conjugate: Boolean) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {
        SearchParametersDropDown(Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            SearchHistoryButton(Modifier.padding(end = 8.dp))
            SettingsButton(Modifier.padding())
        }

        Row() {
            UndoCanvasButton(Modifier.padding(end = 8.dp), onUndoClicked = onUndoClicked)
            ClearCanvasButton(onCanvasClear = onCanvasClear)
        }
        AboutIcon()
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchParametersDropDown(modifier: Modifier = Modifier) {
    val options = listOf("Contains", "Exact", "Forwards")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

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
            value = selectedOptionText,
            onValueChange = {},
            trailingIcon = {
                if (expanded)
                {
                    Icon(Icons.Rounded.ExpandLess,
                        contentDescription = "Expand Less",
                        tint = onPrimaryContainerLight,
                        modifier = Modifier
                        .clip(RoundedCornerShape(20.dp)))
                }
                else
                {
                    Icon(Icons.Rounded.ExpandMore,
                        contentDescription = "Expand More",
                        tint = onPrimaryContainerLight,
                        modifier = Modifier
                        .clip(RoundedCornerShape(20.dp)))
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
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, color = onSecondaryContainerLight) },
                    onClick = {
                        selectedOptionText = selectionOption
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
fun ToggleConjugationButton(modifier: Modifier = Modifier, toggleConjugation: () -> Unit, conjugate: Boolean) {
    FloatingActionButton(
        onClick = toggleConjugation,
        containerColor = primaryContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier
    ) {

        Icon(if (conjugate) Icons.Rounded.Shuffle else Icons.Rounded.CheckBoxOutlineBlank, "Import Dictionary Button.")
    }
}

@Composable
fun SearchHistoryButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = {  },
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
fun SearchBar(modifier: Modifier = Modifier, query: MutableState<TextFieldValue>) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .clip(RoundedCornerShape(20.dp))

    ) {
        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            modifier = Modifier.fillMaxHeight(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Rounded.Clear, contentDescription = "Clear Query", tint = onPrimaryContainerDark, modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { query.value = TextFieldValue() })
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

@Composable
fun MyApp(modifier: Modifier = Modifier, kanji: List<String> = listOf("感", "辞", "書")) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    if (shouldShowOnboarding) {
        OnboardingScreen(modifier, onContinueClicked = { shouldShowOnboarding = false })
    } else {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = kanji) { k ->
                Greeting(k)
            }
        }
    }

}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        SubPage(name)
    }
}


@Composable
fun SubPage(name: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 24.dp,
        label = "",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = extraPadding
            )

        )
        {
            Text(
                text = name,
                modifier = modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            if (expanded) {
                Text(
                    text = "Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ".repeat(4)
                )
            }
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) stringResource(R.string.show_less) else stringResource(
                        R.string.show_more
                    )
                )
            }
        }
    }
}


@Composable
fun OnboardingScreen(modifier: Modifier = Modifier, onContinueClicked: () -> Unit) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun SubPagePreview() {
    MyApp()
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val lines = remember {
        mutableStateListOf<Line>()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(20.dp))

        ) {
            TextField(
                value = query,
                onValueChange = { query = it },

                modifier = Modifier.fillMaxHeight(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(id = R.color.secondary1),
                    unfocusedContainerColor = colorResource(id = R.color.light1),
                    focusedContainerColor = colorResource(id = R.color.light1),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(id = R.color.secondary1),
                ),
                shape = RectangleShape
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .background(color = colorResource(id = R.color.secondary1))
                    .fillMaxHeight()

            ) {
                Icon(
                    Icons.Rounded.Search,
                    tint = colorResource(id = R.color.light1),
                    contentDescription = "Search Button"
                )
            }
        }

        Row(
            modifier = Modifier
                .height(400.dp)
                .padding(top = 40.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.secondary1))
                    .fillMaxSize()
                    .pointerInput(true) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position
                            )
                            lines.add(line)
                        }
                    }
            )
            {
                lines.forEach { line ->
                    drawLine(
                        color = Color(0xFFFFF4E9),
                        start = line.start,
                        end = line.end,
                        strokeWidth = line.strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {

            FloatingActionButton(
                onClick = {
                    if (lines.lastIndex >= 0) {
                        lines.removeAt(lines.lastIndex)
                    }
                },
                containerColor = colorResource(id = R.color.secondary1),
                contentColor = colorResource(id = R.color.light1),
                modifier = Modifier
            ) {
                Icon(painterResource(id = R.drawable.undo), "Undo button.")
            }

            FloatingActionButton(
                onClick = { lines.clear() },
                containerColor = colorResource(id = R.color.secondary1),
                contentColor = colorResource(id = R.color.light1),
                modifier = Modifier

            ) {
                Icon(Icons.Rounded.Clear, "Clear button.")
            }


        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {

            TextButton(
                onClick = { },
                modifier = Modifier
            ) {
                Text("金", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton(
                onClick = { },
                modifier = Modifier
            ) {
                Text("育", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }

            TextButton(
                onClick = { },
                modifier = Modifier
            ) {
                Text("実", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton(
                onClick = { },
                modifier = Modifier
            ) {
                Text("鬱", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton(
                onClick = { },
                modifier = Modifier
            ) {
                Text("匂", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {

            FloatingActionButton(
                onClick = { },
                containerColor = colorResource(id = R.color.secondary1),
                contentColor = colorResource(id = R.color.light1),
                modifier = Modifier
                    .padding(end = 4.dp)
            ) {
                Icon(painterResource(id = R.drawable.upload2), "Import Dictionary Button.")
            }

            FloatingActionButton(
                onClick = { },
                containerColor = colorResource(id = R.color.secondary1),
                contentColor = colorResource(id = R.color.light1),
                modifier = Modifier

            ) {
                Icon(Icons.Rounded.Settings, "Settings Button.")
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MainPage()
}