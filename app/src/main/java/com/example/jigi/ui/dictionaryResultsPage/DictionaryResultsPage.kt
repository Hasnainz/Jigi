package com.example.jigi.ui.dictionaryResultsPage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.jigi.viewprovider.AppViewModelProvider
import com.example.jigi.ui.searchPage.SearchPageViewModel
import com.example.jigi.ui.theme.onPrimaryContainerLight
import com.example.jigi.ui.theme.primaryContainerLight
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

//    Column {
//        Text("Query: \"${searchPageState.value.query}\", Option: ${searchPageState.value.selectedSearchOption}")
//        Text("$queryResult")
//    }

    LazyColumn (
        modifier = modifier,
    ) {
        items(queryResult) { entry ->
            DictionaryEntryCard(word = entry.word, reading = entry.reading, definition = entry.definition)
        }

    }



}

@Preview
@Composable
fun DictionaryEntryCard(
    word: String = "",
    reading: String = "",
    definition: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = primaryContainerLight,
            contentColor = onPrimaryContainerLight
        ),
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
    ){

        Column() {
            Row(
                modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            ) {
                TextWithReading(listOf(
                    TextData(
                        text = word,
                        reading = reading
                    )),
                    showReadings = true
                )
//                Text(
//                    text = word,
//                    style = MaterialTheme.typography.headlineMedium,
//                )
//                Text(
//                    text = reading,
//                    style = MaterialTheme.typography.bodyMedium,
//                )
            }

            HorizontalDivider(
                color = onPrimaryContainerLight,
                modifier = modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            Text(
                text = definition,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            )
        }

    }
}


data class TextData(
    val text: String,
    val reading: String? = null,
)

@Composable
fun TextWithReading(
    textContent: List<TextData>,
    showReadings: Boolean = false,
    modifier: Modifier = Modifier
) {
    val (text, inlineContent) = remember(textContent) {
        calculateAnnotatedString(textContent = textContent, showReadings = showReadings)
    }

    Text(
        text = text,
        inlineContent = inlineContent,
//        fontSize = 36.dp,
        modifier = modifier
    )
}

fun calculateAnnotatedString(textContent: List<TextData>, showReadings: Boolean):
        Pair<AnnotatedString, Map<String, InlineTextContent>> {
    val inlineContent = mutableMapOf<String, InlineTextContent>()

    return buildAnnotatedString {
        for (elem in textContent) {
            val text = elem.text
            val reading = elem.reading

            // If there is not reading available, simply add the text and move to the next element.
            if (reading == null) {
                append(text)
                continue
            }

            // Words larger than one character/kanji need a small amount of additional space in their
            // x-dimension.
            val width = (text.length.toDouble() + (text.length - 1) * 0.05).em
            appendInlineContent(text, text)
            inlineContent[text] = InlineTextContent(
                // TODO: find out why height and width need magic numbers.
                placeholder = Placeholder(
                    width = width,
                    height = 1.97.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom,
                ),
                children = {
                    val readingFontSize = LocalTextStyle.current.fontSize / 2
                    val boxHeight = with(LocalDensity.current) { readingFontSize.toDp() }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Box(modifier = Modifier.requiredHeight(boxHeight + 3.dp)) {
                            if (showReadings) {
                                Text(
                                    modifier = Modifier.wrapContentWidth(unbounded = true),
                                    text = reading,
                                    style = TextStyle.Default.copy(fontSize = readingFontSize)
                                )
                            }
                        }
                        Text(
                            text = text,
//                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            )
        }
    } to inlineContent
}

@Preview
@Composable
internal fun PreviewTextWithReading() {
    val textContent = listOf(
        TextData(text = "このルールを"),
        TextData(text = "守", reading = "まも"),
        TextData(text = "るらない"),
        TextData(text = "人", reading = "ひと"),
        TextData(text = "は"),
        TextData(text = "旅行", reading = "りょこう"),
        TextData(text = "ができなくなることもあります。"),
    )

    MaterialTheme {
        TextWithReading(textContent = textContent, showReadings = true)
    }
}

@Preview
@Composable
internal fun PreviewTextWithoutReading() {
    val textContent = listOf(
        TextData(text = "このルールを"),
        TextData(text = "守", reading = "まも"),
        TextData(text = "るらない"),
        TextData(text = "人", reading = "ひと"),
        TextData(text = "は"),
        TextData(text = "旅行", reading = "りょこう"),
        TextData(text = "ができなくなることもあります。"),
    )

    MaterialTheme {
        TextWithReading(textContent = textContent, showReadings = false)
    }
}
