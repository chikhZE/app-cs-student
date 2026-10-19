package com.example.cs_student.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs_student.ui.theme.DarkBackground
import com.example.cs_student.ui.theme.HackerGreen

// Define the colors based on the design system
val DarkGreyField = Color(0xFF222222)
val NeonGreen = HackerGreen
val CrispWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    modifier: Modifier = Modifier,
    onPostClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // Local state variables for form inputs and selections
    var selectedContentType by remember { mutableStateOf("News") } // "News" or "Res"
    var selectedModule by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var resetKey by remember { mutableStateOf(0) }

    // Predefined lists for dropdowns
    val contentTypes = listOf("News", "Res")
    val modulesList = listOf("Algo", "MP", "CRI-STRM", "Archi", "ProbStat")
    val typesList = listOf("pdf", "link", "video")

    val scrollState = rememberScrollState()

    // Main layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
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
                        .clickable { selectedContentType = type },
                    color = if (selectedContentType == type) NeonGreen else DarkGreyField,
                    shape = RoundedCornerShape(25.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = type,
                            color = if (selectedContentType == type) DarkBackground else CrispWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                    Spacer(modifier = Modifier.width(8.dp))

            }
        }

        // Form fields (Scrollable)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedContentType == "News") {
                // News Form
                NewsForm(resetKey = resetKey)
            } else {
                // Resource Form
                ResourceForm(
                    modulesList = modulesList,
                    typesList = typesList,
                    selectedModule = selectedModule,
                    onModuleSelect = { selectedModule = it },
                    selectedType = selectedType,
                    onTypeSelect = { selectedType = it },
                    resetKey = resetKey
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
                onClick = onPostClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(27.5.dp)
            ) {
                Text(
                    text = "Post Content",
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Cancel Button
            Text(
                text = "Cancel",
                color = CrispWhite.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable {
                        resetKey++
                        selectedContentType = "News"
                        selectedModule = ""
                        selectedType = ""
                        onBackClick()
                    },
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun NewsForm(resetKey: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FormTextField(label = "Date (Click to select)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "Title (English)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "Title (French)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(
            label = "Description (English)",
            modifier = Modifier.padding(bottom = 12.dp),
            isMultiline = true,
            resetKey = resetKey
        )
        FormTextField(
            label = "Description (French)",
            modifier = Modifier.padding(bottom = 12.dp),
            isMultiline = true,
            resetKey = resetKey
        )
        FormTextField(label = "Tag (English)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "Tag (French)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
    }
}

@Composable
fun ResourceForm(
    modulesList: List<String>,
    typesList: List<String>,
    selectedModule: String,
    onModuleSelect: (String) -> Unit,
    selectedType: String,
    onTypeSelect: (String) -> Unit,
    resetKey: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Module Dropdown
        FormDropdownSelector(
            label = "Module",
            options = modulesList,
            selectedOption = selectedModule,
            onOptionSelect = onModuleSelect,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Semester Input
        FormTextField(label = "Semester (e.g., S1, S2)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "Title (English)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "Title (French)", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        FormTextField(label = "URL", modifier = Modifier.padding(bottom = 12.dp), resetKey = resetKey)
        // Resource Type Dropdown
        FormDropdownSelector(
            label = "Type",
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
    modifier: Modifier = Modifier,
    isMultiline: Boolean = false,
    resetKey: Int = 0
) {
    var text by remember(resetKey) { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = label, color = CrispWhite.copy(alpha = 0.5f)) },
        modifier = modifier.fillMaxWidth(),
        textStyle = LocalTextStyle.current.copy(color = CrispWhite, fontSize = 16.sp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkGreyField,
            unfocusedContainerColor = DarkGreyField,
            focusedBorderColor = NeonGreen,
            unfocusedBorderColor = DarkGreyField,
            cursorColor = NeonGreen
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
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = DarkGreyField,
                cursorColor = NeonGreen
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
