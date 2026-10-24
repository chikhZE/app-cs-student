package com.zineeddine.cs_student.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.zineeddine.cs_student.data.ResourceItem
import com.zineeddine.cs_student.data.ResourcesListViewModel
import com.zineeddine.cs_student.R
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesListScreen(
    isAdmin: Boolean = false,
    moduleCode: String,
    contentType: String,
    onBackClick: () -> Unit,
    viewModel: ResourcesListViewModel = viewModel()
) {

    val uriHandler = LocalUriHandler.current
    val resourcesList by viewModel.resources.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var resourceToDelete by remember { mutableStateOf<ResourceItem?>(null) }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {coroutineScope.launch {
            viewModel.loadResources()
        }},
    ) {
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
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        )
        { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(resourcesList) { resource ->
                    ResourceCard(
                        isAdmin = isAdmin,
                        resource = resource,
                        onOpenUrl = { url ->
                            try {
                                uriHandler.openUri(url)
                            } catch (e: Exception) {

                            }
                        },
                        onDelete = { item ->
                            resourceToDelete = item
                        }
                    )
                }
            }
        }
    }

    if (resourceToDelete != null) {
        AlertDialog(
            onDismissRequest = { resourceToDelete = null },
            title = {
                Text(text = stringResource(R.string.confirm_deletion), color = TextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = stringResource(R.string.confirm_deletion_message), color = TextWhite.copy(alpha = 0.8f))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        resourceToDelete?.let { item ->
                            viewModel.deleteResources(item)
                        }
                        resourceToDelete = null
                    }
                ) {
                    Text(text = stringResource(R.string.delete), color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { resourceToDelete = null }
                ) {
                    Text(text = stringResource(R.string.cancel), color = TextWhite.copy(alpha = 0.7f))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun ResourceCard(
    isAdmin: Boolean = false,
    resource: ResourceItem,
    onOpenUrl: (String) -> Unit,
    onDelete: (ResourceItem) -> Unit
) {
    val (icon, iconTint) = when (resource.type) {
        "video" -> Pair(Icons.Default.PlayCircle, Color(0xFFFF5252))
        "pdf" -> Pair(Icons.Default.Description, NeonGreen)
        else -> Pair(Icons.Default.Link, Color(0xFF448AFF))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onOpenUrl(resource.url) }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (resource.authorPhotoUrl.isNotEmpty()) {
                        AsyncImage(
                            model = resource.authorPhotoUrl,
                            contentDescription = "Author Profile Picture",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default User Icon",
                                tint = Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (resource.authorName.isNotEmpty()) resource.authorName else "Student",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if(isAdmin) {
                    IconButton(
                        onClick = { onDelete(resource) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Resource",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

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
                        text = resource.type,
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

            val currentLang = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
            val titleText = resource.title[currentLang] ?: resource.title["en"] ?: ""

            Text(
                text = titleText,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
