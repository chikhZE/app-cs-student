package com.zineeddine.cs_student

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zineeddine.cs_student.data.MainViewModel
import com.zineeddine.cs_student.screens.AddContentScreen
import com.zineeddine.cs_student.screens.HomeScreen
import com.zineeddine.cs_student.screens.LoginScreen
import com.zineeddine.cs_student.screens.ModulesScreen
import com.zineeddine.cs_student.screens.NavBottom
import com.zineeddine.cs_student.screens.ScheduleScreen
import com.zineeddine.cs_student.screens.SettingsScreen
import com.zineeddine.cs_student.ui.screens.ModuleDetailScreen
import com.zineeddine.cs_student.ui.screens.ResourcesListScreen
import com.zineeddine.cs_student.ui.theme.CSstudentTheme
import com.google.firebase.auth.FirebaseAuth
import com.zineeddine.cs_student.ui.theme.AppTheme
import com.zineeddine.cs_student.ui.theme.DarkColorScheme

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "POST_NOTIFICATIONS permission granted")
        } else {
            Log.w("FCM", "POST_NOTIFICATIONS permission denied")
        }
    }


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContent {
                RootNavigation()

        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
@Composable
fun RootNavigation(mainViewModel: MainViewModel = viewModel()) {
    val selectedTheme by mainViewModel.currentTheme.collectAsState()
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val studentName = currentUser?.displayName ?: "Student"
    val startDestinationRoute = if (currentUser != null) "main_flow" else "login_screen"

    CSstudentTheme(
        darkTheme = true,
        dynamicColor = false,
        selectedTheme = selectedTheme
    ) {
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
                // مرر mainViewModel هنا ليستخدم نفس الـ Instance في SettingsScreen
                MainWithBottomBarScreen(
                    rootNavController = navController,
                    studentName = studentName,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}
@Composable
fun MainWithBottomBarScreen(rootNavController : NavController,studentName: String,mainViewModel: MainViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val studentName = currentUser?.displayName ?: "Student"
    val isAdmin by mainViewModel.isAdmin.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val isConnected by observeNetworkConnectivity(context).collectAsState(initial = true)
    Scaffold(
        bottomBar = { if(currentRoute != "module_detail_screen/{moduleCode}" && currentRoute != "resources_screen/{moduleCode}/{contentType}") NavBottom(navController,isAdmin) }
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
                composable("time_screen") {
                    ScheduleScreen(
                        isAdmin = isAdmin,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable("add_screen") {
                    AddContentScreen(Modifier.padding(innerPadding),{},{})
                }

                composable("settings_screen") {
                    SettingsScreen(Modifier.padding(innerPadding),{
                        rootNavController.navigate("login_screen") {
                            popUpTo("main_flow") { inclusive = true }
                        }
                    }, mainViewModel)
                }
                composable("module_detail_screen/{moduleCode}") { backStackEntry ->
                    val moduleCode = backStackEntry.arguments?.getString("moduleCode") ?: ""
                    ModuleDetailScreen(moduleCode,navController,{navController.popBackStack()})
                }
                composable("resources_screen/{moduleCode}/{contentType}") { backStackEntry ->
                    val moduleCode = backStackEntry.arguments?.getString("moduleCode") ?: ""
                    val contentType = backStackEntry.arguments?.getString("contentType") ?: ""
                    ResourcesListScreen(
                        isAdmin,
                        moduleCode = moduleCode,
                        contentType = contentType,
                        onBackClick = {
                            navController.popBackStack()
                        },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
            ) {
                AnimatedVisibility(
                    visible = !isConnected,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
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

                AnimatedVisibility(
                    visible = !areNotificationsEnabled(context),
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                context.startActivity(intent)
                            },
                        color = Color(0xFFE65100)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsOff,
                                contentDescription = "No Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Notifications disabled. Tap to enable in Settings.",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}



fun areNotificationsEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        androidx.core.app.NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}

