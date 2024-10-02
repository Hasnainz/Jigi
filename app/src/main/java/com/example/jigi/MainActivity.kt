package com.example.jigi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jigi.ui.theme.JigiTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


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

                modifier = Modifier
//                    .clip(RoundedCornerShape(12.dp,0.dp, 0.dp, 12.dp))
                    .fillMaxHeight(),
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
//                    .clip(RoundedCornerShape(12.dp,12.dp, 12.dp, 12.dp))
                    .fillMaxHeight()

            ) {
                Icon(Icons.Outlined.Search,
                    tint = colorResource(id = R.color.light1),
                    contentDescription = "Search Button")
            }
        }
        Row(){
            IconButton(
                onClick = {},
            ) {
                Icon(Icons.Outlined.Search,
                    tint = colorResource(id = R.color.light1),
                    contentDescription = "Search Button")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
        MainPage()
}