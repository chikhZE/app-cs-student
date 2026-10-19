package com.zineeddine.cs_student.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zineeddine.cs_student.ui.theme.DarkBackground
import com.zineeddine.cs_student.ui.theme.HackerGreen
import com.zineeddine.cs_student.ui.theme.TechBlue

@Composable
fun NavBottom(
    navController: NavController,
    isAdmin: Boolean = false
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = DarkBackground,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "home_screen",
            onClick = {
                if(currentRoute != "home_screen") {
                    navController.navigate("home_screen"){
                        popUpTo("home_screen") {inclusive = false}
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White) },
            label = { Text(text = "Home" , color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TechBlue
            )
        )
        NavigationBarItem(
            selected = currentRoute == "modules_screen",
            onClick = {
                if(currentRoute != "modules_screen") {
                    navController.navigate("modules_screen"){
                        popUpTo("home_screen") {inclusive = false}
                    }
                }
            },
            icon = { Icon(Icons.Filled.LibraryBooks, contentDescription = null, tint = Color.White) },
            label = { Text(text = "Modules" , color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TechBlue
            )
        )
        if(isAdmin) {
            NavigationBarItem(
                selected = currentRoute == "add_screen",
                onClick = {
                    if(currentRoute != "add_screen") {
                        navController.navigate("add_screen"){
                            popUpTo("home_screen") {inclusive = false}
                            launchSingleTop = true

                        }
                    }

                },

                icon = { Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White) },
                label = { Text(text = "Add" , color = Color.White) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = TechBlue
                )
            )
        }

        NavigationBarItem(
            selected = currentRoute == "settings_screen",
            onClick = {
                if(currentRoute != "settings_screen") {
                    navController.navigate("settings_screen"){
                        popUpTo("home_screen") {inclusive = false}
                        launchSingleTop = true

                    }
                }
            },

            icon = { Icon(Icons.Filled.Settings, contentDescription = null, tint = Color.White) },
            label = { Text(text = "Settings" , color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = TechBlue
            )
        )
    }
}
