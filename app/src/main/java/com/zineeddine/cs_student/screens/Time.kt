package com.zineeddine.cs_student.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zineeddine.cs_student.R
import com.zineeddine.cs_student.data.ScheduleEntity
import com.zineeddine.cs_student.data.ScheduleViewModel
import com.zineeddine.cs_student.ui.theme.CardSurface
import com.zineeddine.cs_student.ui.theme.DarkBackground
import com.zineeddine.cs_student.ui.theme.HackerGreen
import com.zineeddine.cs_student.ui.theme.TechBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ScheduleItem(
    @StringRes val subjectRes: Int,
    @StringRes val typeRes: Int,
    @StringRes val dayRes: Int,
    val time: String,
)
val daysList = listOf(
    R.string.day_saturday,
    R.string.day_sunday,
    R.string.day_monday,
    R.string.day_tuesday,
    R.string.day_wednesday,
    R.string.day_thursday ,
    R.string.day_friday,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel()
) {
    val scheduleList by viewModel.scheduleList.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    var scheduleToDelete by remember { mutableStateOf<ScheduleEntity?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var selectedDayRes by remember { mutableIntStateOf(R.string.day_saturday) }
    val selectedDayText = stringResource(selectedDayRes)
    val englishDayName = when (selectedDayRes) {
        R.string.day_sunday -> "Sunday"
        R.string.day_monday -> "Monday"
        R.string.day_tuesday -> "Tuesday"
        R.string.day_wednesday -> "Wednesday"
        R.string.day_thursday -> "Thursday"
        R.string.day_saturday -> "Saturday"
        R.string.day_friday -> "Friday"
        else -> ""
    }

    val filteredItems = remember(scheduleList, selectedDayText) {
        scheduleList.filter { item ->
            item.dayRes.equals(englishDayName, ignoreCase = true)
        }
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    isRefreshing = true
                    viewModel.refreshSchedule()
                    delay(1000)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(daysList) { dayRes ->
                        val isSelected = dayRes == selectedDayRes

                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDayRes = dayRes },
                            label = {
                                Text(
                                    text = stringResource(dayRes),
                                    color = if (isSelected) Color.Black else Color.White
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredItems) { scheduleItem ->
                        ScheduleCard(
                            isAdmin = isAdmin,
                            item = scheduleItem,
                            onDelete = { item ->
                                scheduleToDelete = item
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    if (scheduleToDelete != null) {
        AlertDialog(
            onDismissRequest = { scheduleToDelete = null },
            title = {
                Text(text = stringResource(R.string.confirm_deletion), color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = stringResource(R.string.confirm_deletion_message), color = Color.White.copy(alpha = 0.8f))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scheduleToDelete?.let { item ->
                            viewModel.deleteSchedule(item)
                        }
                        scheduleToDelete = null
                    }
                ) {
                    Text(text = stringResource(R.string.delete), color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { scheduleToDelete = null }
                ) {
                    Text(text = stringResource(R.string.cancel), color = Color.White.copy(alpha = 0.7f))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun getLocalizedDayName(dayStr: String): String {
    val dayRes = when (dayStr.lowercase().trim()) {
        "sunday" -> R.string.day_sunday
        "monday" -> R.string.day_monday
        "tuesday" -> R.string.day_tuesday
        "wednesday" -> R.string.day_wednesday
        "thursday" -> R.string.day_thursday
        else -> null
    }
    return if (dayRes != null) stringResource(dayRes) else dayStr
}

@Composable
fun ScheduleCard(
    isAdmin: Boolean = false,
    item: ScheduleEntity,
    onDelete: (ScheduleEntity) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.subjectRes,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClassTypeBadge(typeRes = item.typeRes)
                    if (isAdmin) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { onDelete(item) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Schedule Item",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Day Icon",
                        tint = HackerGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = getLocalizedDayName(item.dayRes),
                        color = Color(0xFFECEFF4),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time Icon",
                        tint = TechBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.time,
                        color = Color(0xFFECEFF4),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}

@Composable
fun ClassTypeBadge(typeRes: String) {
    val (backgroundColor, textColor, borderColor) = when (typeRes.uppercase()) {
        "COURS" -> Triple(
            HackerGreen.copy(alpha = 0.15f),
            HackerGreen,
            HackerGreen
        )
        "TD" -> Triple(
            TechBlue.copy(alpha = 0.15f),
            Color(0xFF64B5F6),
            TechBlue
        )
        "TP" -> Triple(
            Color(0xFFFFB74D).copy(alpha = 0.15f),
            Color(0xFFFFB74D),
            Color(0xFFFFB74D)
        )
        else -> Triple(
            Color.DarkGray.copy(alpha = 0.3f),
            Color.LightGray,
            Color.Gray
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = typeRes,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen()
}
