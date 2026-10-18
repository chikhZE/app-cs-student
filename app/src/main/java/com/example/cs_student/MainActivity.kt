package com.example.cs_student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs_student.screens.HomeScreen
import com.example.cs_student.screens.LoginScreen
import com.example.cs_student.screens.ModulesScreen
import com.example.cs_student.screens.NavBottom
import com.example.cs_student.ui.theme.CSstudentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSstudentTheme {
                RootNavigation()
            }
        }
    }
}

@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login_screen"
    ) {
        composable("login_screen") {
            LoginScreen(
                onGoogleSignInClick = {
                    navController.navigate("main_flow") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            )
        }

        composable("main_flow") {
            MainWithBottomBarScreen()
        }
    }
}

@Composable
fun MainWithBottomBarScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavBottom(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home_screen"
        ) {
            composable("home_screen") {
                HomeScreen(modifier = Modifier.padding(innerPadding))
            }
            composable("modules_screen") {
                ModulesScreen(modifier = Modifier.padding(innerPadding))
            }
            composable("add_screen") {
                Text(modifier = Modifier.padding(innerPadding), text = "this is admin only")
            }
            composable("settings_screen") {
                Text(modifier = Modifier.padding(innerPadding), text = "This is setting screen")
            }
        }
    }
}