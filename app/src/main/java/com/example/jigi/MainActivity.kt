package com.example.jigi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jigi.ui.theme.JigiTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JigiTheme {
                MainPage()
            }

        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val lines = remember {
        mutableStateListOf<Line>()
    }

    Column (
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Row (
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
                Icon(Icons.Rounded.Search,
                    tint = colorResource(id = R.color.light1),
                    contentDescription = "Search Button")
            }
        }

        Row(
            modifier = Modifier
                .height(400.dp)
                .padding(top = 40.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(20.dp))
        ){
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
                lines.forEach{ line ->
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
        ){

            FloatingActionButton(
                onClick = {
                    if(lines.lastIndex >= 0) {
                        lines.removeAt(lines.lastIndex)
                    } },
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
        ){

            TextButton (
                onClick = {  },
                modifier = Modifier
            ) {
                Text("金", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton(
                onClick = {  },
                modifier = Modifier
            ) {
                Text("育", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }

            TextButton(
                onClick = {  },
                modifier = Modifier
            ) {
                Text("実", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton (
                onClick = {  },
                modifier = Modifier
            ) {
                Text("鬱", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            TextButton(
                onClick = {  },
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
        ){

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
                onClick = {  },
                containerColor = colorResource(id = R.color.secondary1),
                contentColor = colorResource(id = R.color.light1),
                modifier = Modifier

            ) {
                Icon(Icons.Rounded.Settings, "Settings Button.")
            }


        }
    }

}
data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWidth: androidx.compose.ui.unit.Dp = 6.dp
)

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
        MainPage()
}