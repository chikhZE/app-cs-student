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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zineeddine.cs_student.R
import com.zineeddine.cs_student.ui.theme.CardSurface
import com.zineeddine.cs_student.ui.theme.DarkBackground
import com.zineeddine.cs_student.ui.theme.HackerGreen
import com.zineeddine.cs_student.ui.theme.TechBlue

data class ScheduleItem(
    val id: String,
    @StringRes val subjectRes: Int,
    @StringRes val typeRes: Int,
    @StringRes val dayRes: Int,
    val time: String,
    @StringRes val roomRes: Int
)

val sampleSchedule = listOf(
    ScheduleItem("1", R.string.module_algo, R.string.type_cours, R.string.day_sunday, "08:00 - 09:30", R.string.room_amphi_c),
    ScheduleItem("2", R.string.module_algo, R.string.type_td, R.string.day_sunday, "09:40 - 11:10", R.string.room_salle_204),
    ScheduleItem("3", R.string.module_archi, R.string.type_cours, R.string.day_monday, "08:00 - 09:30", R.string.room_amphi_a),
    ScheduleItem("4", R.string.module_archi, R.string.type_tp, R.string.day_monday, "09:40 - 11:10", R.string.room_lab_02),
    ScheduleItem("5", R.string.module_analysis, R.string.type_cours, R.string.day_tuesday, "08:00 - 09:30", R.string.room_amphi_b),
    ScheduleItem("6", R.string.module_analysis, R.string.type_td, R.string.day_tuesday, "09:40 - 11:10", R.string.room_salle_108),
    ScheduleItem("7", R.string.module_algebra, R.string.type_cours, R.string.day_wednesday, "08:00 - 09:30", R.string.room_amphi_c),
    ScheduleItem("8", R.string.module_algebra, R.string.type_td, R.string.day_wednesday, "09:40 - 11:10", R.string.room_salle_112),
    ScheduleItem("9", R.string.module_os, R.string.type_cours, R.string.day_thursday, "08:00 - 09:30", R.string.room_amphi_a),
    ScheduleItem("10", R.string.module_web, R.string.type_tp, R.string.day_thursday, "09:40 - 11:10", R.string.room_lab_05)
)

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    items: List<ScheduleItem> = sampleSchedule
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DarkBackground
    ) {



            // Single vertical scrollable list for all classes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.id }) { scheduleItem ->
                    ScheduleCard(item = scheduleItem)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

@Composable
fun ScheduleCard(item: ScheduleItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Subject Name & Class Type Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(item.subjectRes),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                ClassTypeBadge(typeRes = item.typeRes)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day & Time Row
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
                        text = stringResource(item.dayRes),
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
fun ClassTypeBadge(@StringRes typeRes: Int) {
    val typeText = stringResource(typeRes)
    val (backgroundColor, textColor, borderColor) = when (typeText.uppercase()) {
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
            text = typeText,
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
