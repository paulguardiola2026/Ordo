package com.example.capstone2026.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalendarFabMenu(
    onAddEventClick: () -> Unit,
    onUploadSyllabusClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(visible = expanded) {
            Button(onClick = {
                expanded = false
                onUploadSyllabusClick()
            }) {
                Text("Upload Syllabus")
            }
        }

        AnimatedVisibility(visible = expanded) {
            Button(onClick = {
                expanded = false
                onAddEventClick()
            }) {
                Text("Add Event")
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Open actions")
        }
    }
}