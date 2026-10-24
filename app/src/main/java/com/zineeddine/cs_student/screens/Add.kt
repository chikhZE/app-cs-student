package com.zineeddine.cs_student.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zineeddine.cs_student.R
import com.zineeddine.cs_student.data.AddContentViewModel
import com.zineeddine.cs_student.ui.theme.DarkBackground
import com.zineeddine.cs_student.ui.theme.HackerGreen

// Define the colors based on the design system
val DarkGreyField = Color(0xFF222222)
val CrispWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    modifier: Modifier = Modifier,
    onPostClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AddContentViewModel = viewModel()
) {
    // Local state variables for selections
    var selectedContentType by remember { mutableStateOf("News") } // "News", "Res", or "Schedule"
    var selectedModule by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Predefined lists for dropdowns
    val contentTypes = listOf("News", "Res", "Schedule")
    val resModulesList = listOf("Algo", "MP", "CRI-STRM", "Analysis", "Algebra", "ICT")
    val resTypesList = listOf("pdf", "link", "video")
    val scheduleDaysList = listOf("Saturday","Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val scheduleTypesList = listOf("Cours", "TP", "TD")

    val scrollState = rememberScrollState()

    // Main layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Content Type Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            contentTypes.forEach { type ->
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clickable {
                            selectedContentType = type
                            selectedModule = ""
                            selectedType = ""
                            selectedDay = ""
                        },
                    color = if (selectedContentType == type) MaterialTheme.colorScheme.secondary else DarkGreyField,
                    shape = RoundedCornerShape(25.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = type,
                            color = if (selectedContentType == type) MaterialTheme.colorScheme.background else CrispWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (type != contentTypes.last()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Form fields (Scrollable)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (selectedContentType) {
                "News" -> NewsForm(viewModel = viewModel)
                "Res" -> ResourceForm(
                    viewModel = viewModel,
                    modulesList = resModulesList,
                    typesList = resTypesList,
                    selectedModule = selectedModule,
                    onModuleSelect = { selectedModule = it },
                    selectedType = selectedType,
                    onTypeSelect = { selectedType = it }
                )
                "Schedule" -> ScheduleForm(
                    viewModel = viewModel,
                    modulesList = resModulesList,
                    daysList = scheduleDaysList,
                    typesList = scheduleTypesList,
                    selectedModule = selectedModule,
                    onModuleSelect = { selectedModule = it },
                    selectedDay = selectedDay,
                    onDaySelect = { selectedDay = it },
                    selectedType = selectedType,
                    onTypeSelect = { selectedType = it }
                )
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Post Button
            Button(
                onClick = {
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(27.5.dp)
            ) {
                Text(
                    text = stringResource(R.string.post_content),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Cancel Button
            Text(
                text = stringResource(R.string.cancel),
                color = CrispWhite.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable {
                        viewModel.clearForm()
                        selectedContentType = "News"
                        selectedModule = ""
                        selectedType = ""
                        selectedDay = ""
                        onBackClick()
                    },
                fontSize = 16.sp
            )
        }
    }

    // Confirmation Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(text = stringResource(R.string.confirm_post), color = CrispWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = stringResource(R.string.confirm_post_message), color = CrispWhite.copy(alpha = 0.8f))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.handlePostSubmission(
                            contentType = selectedContentType,
                            moduleCode = selectedModule,
                            selectedType = selectedType,
                            selectedDay = selectedDay
                        )
                        viewModel.clearForm()
                        selectedModule = ""
                        selectedType = ""
                        selectedDay = ""
                        onPostClick()
                    }
                ) {
                    Text(text = stringResource(R.string.post), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text(text = stringResource(R.string.cancel), color = CrispWhite.copy(alpha = 0.7f))
                }
            },
            containerColor = DarkGreyField,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun NewsForm(viewModel: AddContentViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FormTextField(
            label = stringResource(R.string.form_date_label),
            value = viewModel.date,
            onValueChange = { viewModel.date = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_title_en_label),
            value = viewModel.newsTitleEn,
            onValueChange = { viewModel.newsTitleEn = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_title_fr_label),
            value = viewModel.newsTitleFr,
            onValueChange = { viewModel.newsTitleFr = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_desc_en_label),
            value = viewModel.descEn,
            onValueChange = { viewModel.descEn = it },
            modifier = Modifier.padding(bottom = 12.dp),
            isMultiline = true
        )
        FormTextField(
            label = stringResource(R.string.form_desc_fr_label),
            value = viewModel.descFr,
            onValueChange = { viewModel.descFr = it },
            modifier = Modifier.padding(bottom = 12.dp),
            isMultiline = true
        )
        FormTextField(
            label = stringResource(R.string.form_tag_en_label),
            value = viewModel.tagEn,
            onValueChange = { viewModel.tagEn = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_tag_fr_label),
            value = viewModel.tagFr,
            onValueChange = { viewModel.tagFr = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

@Composable
fun ResourceForm(
    viewModel: AddContentViewModel,
    modulesList: List<String>,
    typesList: List<String>,
    selectedModule: String,
    onModuleSelect: (String) -> Unit,
    selectedType: String,
    onTypeSelect: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Module Dropdown
        FormDropdownSelector(
            label = stringResource(R.string.form_module_label),
            options = modulesList,
            selectedOption = selectedModule,
            onOptionSelect = onModuleSelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Semester Input
        FormTextField(
            label = stringResource(R.string.form_semester_label),
            value = viewModel.semester,
            onValueChange = { viewModel.semester = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_title_en_label),
            value = viewModel.resTitleEn,
            onValueChange = { viewModel.resTitleEn = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_title_fr_label),
            value = viewModel.resTitleFr,
            onValueChange = { viewModel.resTitleFr = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FormTextField(
            label = stringResource(R.string.form_url_label),
            value = viewModel.url,
            onValueChange = { viewModel.url = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Resource Type Dropdown
        FormDropdownSelector(
            label = stringResource(R.string.form_type_label),
            options = typesList,
            selectedOption = selectedType,
            onOptionSelect = onTypeSelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

@Composable
fun ScheduleForm(
    viewModel: AddContentViewModel,
    modulesList: List<String>,
    daysList: List<String>,
    typesList: List<String>,
    selectedModule: String,
    onModuleSelect: (String) -> Unit,
    selectedDay: String,
    onDaySelect: (String) -> Unit,
    selectedType: String,
    onTypeSelect: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Module Dropdown
        FormDropdownSelector(
            label = stringResource(R.string.form_module_label),
            options = modulesList,
            selectedOption = selectedModule,
            onOptionSelect = onModuleSelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Day Dropdown
        FormDropdownSelector(
            label = "Day",
            options = daysList,
            selectedOption = selectedDay,
            onOptionSelect = onDaySelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Time Input
        FormTextField(
            label = "Time (e.g. 8:00 - 9:40)",
            value = viewModel.scheduleTime,
            onValueChange = { viewModel.scheduleTime = it },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Class Type Dropdown
        FormDropdownSelector(
            label = stringResource(R.string.form_type_label),
            options = typesList,
            selectedOption = selectedType,
            onOptionSelect = onTypeSelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

// Custom styled TextField for forms
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isMultiline: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = CrispWhite.copy(alpha = 0.5f)) },
        modifier = modifier.fillMaxWidth(),
        textStyle = LocalTextStyle.current.copy(color = CrispWhite, fontSize = 16.sp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkGreyField,
            unfocusedContainerColor = DarkGreyField,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = DarkGreyField,
            cursorColor = MaterialTheme.colorScheme.secondary
        ),
        singleLine = !isMultiline,
        minLines = if (isMultiline) 3 else 1
    )
}

// Custom styled Dropdown selector for forms
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label, color = CrispWhite.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            textStyle = LocalTextStyle.current.copy(color = CrispWhite, fontSize = 16.sp),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGreyField,
                unfocusedContainerColor = DarkGreyField,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = DarkGreyField,
                cursorColor = MaterialTheme.colorScheme.secondary
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(DarkGreyField)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, color = CrispWhite, fontSize = 16.sp) },
                    onClick = {
                        onOptionSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Preview to check the design
@Preview(showBackground = true)
@Composable
fun AddContentScreenPreview() {
    AddContentScreen(onPostClick = {}, onBackClick = {})
}
