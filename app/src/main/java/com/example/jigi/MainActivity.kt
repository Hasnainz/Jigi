package com.example.jigi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.jigi.ui.navigation.JigiApp

import com.example.jigi.ui.theme.*


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                JigiApp()
            }

        }
    }
}



//TODO :: Search page with dictionary
//TODO :: Settings page for Dictionary
//TODO :: Handwriting Recognition page
//TODO :: Search Options




//@Composable
//fun MyApp(modifier: Modifier = Modifier, kanji: List<String> = listOf("感", "辞", "書")) {
//    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
//    if (shouldShowOnboarding) {
//        OnboardingScreen(modifier, onContinueClicked = { shouldShowOnboarding = false })
//    } else {
//        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
//            items(items = kanji) { k ->
//                Greeting(k)
//            }
//        }
//    }
//
//}
//
//@Composable
//private fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Card(
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.primary
//        ),
//        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
//    ) {
//        SubPage(name)
//    }
//}
//
//
//@Composable
//fun SubPage(name: String, modifier: Modifier = Modifier) {
//    var expanded by rememberSaveable { mutableStateOf(false) }
//    val extraPadding by animateDpAsState(
//        if (expanded) 48.dp else 24.dp,
//        label = "",
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioNoBouncy,
//            stiffness = Spring.StiffnessHigh
//        )
//    )
//    Surface(
//        color = MaterialTheme.colorScheme.primary,
//        modifier = modifier.padding(vertical = 4.dp, horizontal = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(
//                start = 24.dp,
//                end = 24.dp,
//                top = 24.dp,
//                bottom = extraPadding
//            )
//
//        )
//        {
//            Text(
//                text = name,
//                modifier = modifier.padding(24.dp),
//                style = MaterialTheme.typography.headlineLarge
//            )
//            if (expanded) {
//                Text(
//                    text = "Composem ipsum color sit lazy, " +
//                            "padding theme elit, sed do bouncy. ".repeat(4)
//                )
//            }
//            IconButton(
//                onClick = { expanded = !expanded }
//            ) {
//                Icon(
//                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
//                    contentDescription = if (expanded) stringResource(R.string.show_less) else stringResource(
//                        R.string.show_more
//                    )
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun OnboardingScreen(modifier: Modifier = Modifier, onContinueClicked: () -> Unit) {
//
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to the Basics Codelab!")
//        Button(
//            modifier = Modifier.padding(vertical = 24.dp),
//            onClick = onContinueClicked
//        ) {
//            Text("Continue")
//        }
//    }
//}
//
//
//@Preview(showBackground = true, widthDp = 320)
//@Composable
//fun SubPagePreview() {
//    MyApp()
//}
//
//@Composable
//fun MainPage(modifier: Modifier = Modifier) {
//    var query by remember { mutableStateOf("") }
//    val lines = remember {
//        mutableStateListOf<Line>()
//    }
//
//    Column(
//        modifier = Modifier.fillMaxHeight(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    )
//    {
//        Row(
//            modifier = Modifier
//                .height(intrinsicSize = IntrinsicSize.Min)
//                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
//                .clip(RoundedCornerShape(20.dp))
//
//        ) {
//            TextField(
//                value = query,
//                onValueChange = { query = it },
//
//                modifier = Modifier.fillMaxHeight(),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                singleLine = true,
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = colorResource(id = R.color.secondary1),
//                    unfocusedContainerColor = colorResource(id = R.color.light1),
//                    focusedContainerColor = colorResource(id = R.color.light1),
//                    unfocusedIndicatorColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent,
//                    cursorColor = colorResource(id = R.color.secondary1),
//                ),
//                shape = RectangleShape
//            )
//            IconButton(
//                onClick = {},
//                modifier = Modifier
//                    .background(color = colorResource(id = R.color.secondary1))
//                    .fillMaxHeight()
//
//            ) {
//                Icon(
//                    Icons.Rounded.Search,
//                    tint = colorResource(id = R.color.light1),
//                    contentDescription = "Search Button"
//                )
//            }
//        }
//
//        Row(
//            modifier = Modifier
//                .height(400.dp)
//                .padding(top = 40.dp, start = 40.dp, end = 40.dp)
//                .clip(RoundedCornerShape(20.dp))
//        ) {
//            Canvas(
//                modifier = Modifier
//                    .background(color = colorResource(id = R.color.secondary1))
//                    .fillMaxSize()
//                    .pointerInput(true) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consume()
//                            val line = Line(
//                                start = change.position - dragAmount,
//                                end = change.position
//                            )
//                            lines.add(line)
//                        }
//                    }
//            )
//            {
//                lines.forEach { line ->
//                    drawLine(
//                        color = Color(0xFFFFF4E9),
//                        start = line.start,
//                        end = line.end,
//                        strokeWidth = line.strokeWidth.toPx(),
//                        cap = StrokeCap.Round,
//                    )
//                }
//            }
//        }
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier
//                .height(intrinsicSize = IntrinsicSize.Min)
//                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .fillMaxWidth()
//        ) {
//
//            FloatingActionButton(
//                onClick = {
//                    if (lines.lastIndex >= 0) {
//                        lines.removeAt(lines.lastIndex)
//                    }
//                },
//                containerColor = colorResource(id = R.color.secondary1),
//                contentColor = colorResource(id = R.color.light1),
//                modifier = Modifier
//            ) {
//                Icon(painterResource(id = R.drawable.undo), "Undo button.")
//            }
//
//            FloatingActionButton(
//                onClick = { lines.clear() },
//                containerColor = colorResource(id = R.color.secondary1),
//                contentColor = colorResource(id = R.color.light1),
//                modifier = Modifier
//
//            ) {
//                Icon(Icons.Rounded.Clear, "Clear button.")
//            }
//
//
//        }
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .height(intrinsicSize = IntrinsicSize.Min)
//                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .fillMaxWidth()
//        ) {
//
//            TextButton(
//                onClick = { },
//                modifier = Modifier
//            ) {
//                Text("金", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//            TextButton(
//                onClick = { },
//                modifier = Modifier
//            ) {
//                Text("育", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//
//            TextButton(
//                onClick = { },
//                modifier = Modifier
//            ) {
//                Text("実", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//            TextButton(
//                onClick = { },
//                modifier = Modifier
//            ) {
//                Text("鬱", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//            TextButton(
//                onClick = { },
//                modifier = Modifier
//            ) {
//                Text("匂", fontWeight = FontWeight.Bold, fontSize = 24.sp)
//            }
//        }
//        Row(
//            horizontalArrangement = Arrangement.End,
//            modifier = Modifier
//                .height(intrinsicSize = IntrinsicSize.Min)
//                .padding(top = 12.dp, start = 40.dp, end = 40.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .fillMaxWidth()
//        ) {
//
//            FloatingActionButton(
//                onClick = { },
//                containerColor = colorResource(id = R.color.secondary1),
//                contentColor = colorResource(id = R.color.light1),
//                modifier = Modifier
//                    .padding(end = 4.dp)
//            ) {
//                Icon(painterResource(id = R.drawable.upload2), "Import Dictionary Button.")
//            }
//
//            FloatingActionButton(
//                onClick = { },
//                containerColor = colorResource(id = R.color.secondary1),
//                contentColor = colorResource(id = R.color.light1),
//                modifier = Modifier
//
//            ) {
//                Icon(Icons.Rounded.Settings, "Settings Button.")
//            }
//
//
//        }
//    }
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MainPagePreview() {
//    MainPage()
//}