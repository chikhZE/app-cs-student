package com.example.cs_student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cs_student.screens.HomeScreen
import com.example.cs_student.screens.LoginScreen
import com.example.cs_student.screens.ModulesScreen
import com.example.cs_student.screens.NavBottom
import com.example.cs_student.screens.SettingsScreen
import com.example.cs_student.ui.screens.ModuleDetailScreen
import com.example.cs_student.ui.screens.ResourcesListScreen
import com.example.cs_student.ui.theme.CSstudentTheme
import com.google.firebase.auth.FirebaseAuth

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
    val currentUser = FirebaseAuth.getInstance().currentUser
    val studentName = currentUser?.displayName ?: "Student"
    val startDestinationRoute = if (currentUser != null) "main_flow" else "login_screen"
    NavHost(
        navController = navController,
        startDestination = startDestinationRoute
    ) {
        composable("login_screen") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main_flow") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            )
        }

        composable("main_flow") {
            MainWithBottomBarScreen(navController,studentName)
        }
    }
}

@Composable
fun MainWithBottomBarScreen(rootNavController : NavController,studentName: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val isConnected by observeNetworkConnectivity(context).collectAsState(initial = true)
    Scaffold(
        bottomBar = { if(currentRoute != "module_detail_screen/{moduleCode}" && currentRoute != "resources_screen/{moduleCode}/{contentType}") NavBottom(navController) }
    ) { innerPadding ->
        Box() {
            NavHost(
                navController = navController,
                startDestination = "home_screen"
            ) {
                composable("home_screen") {
                    HomeScreen(modifier = Modifier.padding(innerPadding),studentName,navController)
                }
                composable("modules_screen",) {
                    ModulesScreen(modifier = Modifier.padding(innerPadding),navController)
                }
                composable("add_screen") {
                    Text(modifier = Modifier.padding(innerPadding), text = "this is admin only")
                }
                composable("settings_screen") {
                    SettingsScreen(Modifier.padding(innerPadding),{
                        rootNavController.navigate("login_screen") {
                            popUpTo("main_flow") { inclusive = true }
                        }
                    })
                }
                composable("module_detail_screen/{moduleCode}") { backStackEntry ->
                    val moduleCode = backStackEntry.arguments?.getString("moduleCode") ?: ""
                    ModuleDetailScreen(moduleCode,navController,{navController.popBackStack()})
                }
                composable("resources_screen/{moduleCode}/{contentType}") { backStackEntry ->
                    val moduleCode = backStackEntry.arguments?.getString("moduleCode") ?: ""
                    val contentType = backStackEntry.arguments?.getString("contentType") ?: ""
                    ResourcesListScreen(
                        moduleCode = moduleCode,
                        contentType = contentType,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            if (!isConnected) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top=20.dp),
                    color = Color(0xFFD32F2F)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No Internet",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No internet connection. You are offline.",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

    }
}