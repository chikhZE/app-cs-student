package com.example.cs_student.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResourceDummyItem(
    val id: String,
    val title: String,
    val description: String,
    val semester: String, // "S1"  "S2"
    val resourceType: String, // "PDF", "Video", "Link"
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesListScreen(
    moduleCode: String,
    contentType: String,
    onBackClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val dummyResources = listOf(
        ResourceDummyItem(
            id = "1",
            title = "Cours 01: Introduction aux Algorithmes",
            description = "Cours 01: Introduction aux Algorithmes Cours 01: Introduction aux Algorithmes Cours 01: Introduction aux Algorithmes",
            semester = "S1",
            resourceType = "PDF",
            url = "https://drive.google.com"
        ),
        ResourceDummyItem(
            id = "2",
            title = "Série TD N°1 avec Solution",
            description = "حلول تمارين السلسلة الأولى الخاصة بالسداسي الأول.",
            semester = "S1",
            resourceType = "PDF",
            url = "https://drive.google.com"
        ),
        ResourceDummyItem(
            id = "3",
            title = "شرح الـ Pointers والـ Memory Management",
            description = "فيديو تفصيلي يشرح المؤشرات وطريقة التعامل مع الذاكرة بلغة C.",
            semester = "S2",
            resourceType = "PDF",
            url = "https://youtube.com"
        ),
        ResourceDummyItem(
            id = "4",
            title = "موقع مفيد للممارسة والتمارين (GeeksforGeeks)",
            description = "رابط موقع يحتوي على مسائل برمجية محلولة لتقوية مستواك.",
            semester = "S2",
            resourceType = "PDF",
            url = "https://geeksforgeeks.org"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = moduleCode,
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = contentType.replaceFirstChar { it.uppercase() },
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyResources) { resource ->
                ResourceCard(
                    resource = resource,
                    onOpenUrl = { url ->
                        try {
                            uriHandler.openUri(url)
                        } catch (e: Exception) {

                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ResourceCard(
    resource: ResourceDummyItem,
    onOpenUrl: (String) -> Unit
) {
    val (icon, iconTint) = when (resource.resourceType) {
        "Video" -> Pair(Icons.Default.PlayCircle, Color(0xFFFF5252))
        "PDF" -> Pair(Icons.Default.Description, NeonGreen)
        else -> Pair(Icons.Default.Link, Color(0xFF448AFF))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable { onOpenUrl(resource.url) }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(IconBoxBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    BadgeTag(
                        text = resource.semester,
                        backgroundColor = Color(0xFF2C2C2C),
                        textColor = TextWhite
                    )

                    BadgeTag(
                        text = resource.resourceType,
                        backgroundColor = iconTint.copy(alpha = 0.15f),
                        textColor = iconTint
                    )
                }

                IconButton(
                    onClick = { onOpenUrl(resource.url) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open Link",
                        tint = IconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = resource.title,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = resource.description,
                color = Color.LightGray,
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun BadgeTag(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}