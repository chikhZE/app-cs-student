package com.example.cs_student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cs_student.screens.NavBottom
import com.example.cs_student.ui.theme.CSstudentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSstudentTheme {
                App()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true
)
@Composable
fun GreetingPreview() {
    CSstudentTheme {
        Greeting("Android")
    }
}


@Composable
fun App() {
    Scaffold(
        bottomBar = { NavBottom() }
    ) { innerPadding ->
        com.example.cs_student.screens.HomeScreen(modifier = Modifier.padding(innerPadding))
    }
}