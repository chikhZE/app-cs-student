package com.example.cs_student.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cs_student.R
import com.example.cs_student.ui.theme.CSstudentTheme
import com.example.cs_student.ui.theme.CardSurface
import com.example.cs_student.ui.theme.DarkBackground
import com.example.cs_student.ui.theme.HackerGreen

@Composable
fun HomeScreen(modifier: Modifier = Modifier,studentName: String, navController: NavController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StudySpaceCard(Modifier,studentName, { navController.navigate("modules_screen") })
        AnnouncementsSection()
    }
}

@Composable
fun StudySpaceCard(modifier: Modifier = Modifier,studentName: String,buttonOnClick : () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF142216)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(HackerGreen)
                )
                Text(
                    text = stringResource(R.string.study_space_tag),
                    color = HackerGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append(stringResource(R.string.hello)," ",studentName,", ",stringResource(R.string.future)," ")


                    }
                    withStyle(style = SpanStyle(color = HackerGreen)) {
                        append(stringResource(R.string.computer_scientist))
                    }
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            )

            Text(
                text = stringResource(R.string.study_space_desc),
                color = Color(0xFFB0B0B0),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Button(
                onClick = {buttonOnClick()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = HackerGreen,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.explore_modules),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AnnouncementsSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.latest_announcements),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.announcements_subtitle),
                    color = Color(0xFF9E9E9E),
                    fontSize = 13.sp
                )
            }
            Text(
                text = stringResource(R.string.updates_count),
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        AnnouncementCard(
            tag = stringResource(R.string.tag_important),
            date = stringResource(R.string.exam_date),
            title = stringResource(R.string.exam_title),
            description = stringResource(R.string.exam_desc)
        )

        AnnouncementCard(
            tag = stringResource(R.string.tag_welcome),
            date = "",
            title = stringResource(R.string.welcome_title),
            description = stringResource(R.string.welcome_desc)
        )
    }
}

@Composable
fun AnnouncementCard(
    tag: String,
    date: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF2A2A2A)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(0xFFD0D0D0),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (date.isNotEmpty()) {
                    Text(
                        text = date,
                        color = Color(0xFF808080),
                        fontSize = 11.sp
                    )
                }
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                color = Color(0xFFB0B0B0),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
/*
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun HomeScreenPreview() {
    CSstudentTheme {
        HomeScreen(Modifier,"ZineEddine",)
    }
}
*/