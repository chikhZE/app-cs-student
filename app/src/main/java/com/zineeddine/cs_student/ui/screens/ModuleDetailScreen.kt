package com.zineeddine.cs_student.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)
val IconBoxBackground = Color(0xFF282828)
val NeonGreen = Color(0xFF00FF66)
val TextWhite = Color(0xFFEEEEEE)
val IconColor = Color(0xFFB0B0B0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleDetailScreen(
    moduleCode: String,
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = moduleCode,
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryCard(
                title = "Documents",
                icon = Icons.Default.Description,
                iconTint = NeonGreen,
                onClick = { navController.navigate("resources_screen/${moduleCode}/pdf")}
            )

            CategoryCard(
                title = "Videos",
                icon = Icons.Default.PlayCircle,
                iconTint = Color(0xFFFF5252),
                onClick = { navController.navigate("resources_screen/${moduleCode}/video")}
            )

            CategoryCard(
                title = "Useful Links",
                icon = Icons.Default.Link,
                iconTint = Color(0xFF448AFF),
                onClick = { navController.navigate("resources_screen/${moduleCode}/link")}
            )
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    iconTint: Color = NeonGreen,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IconBoxBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Navigate",
                tint = IconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
