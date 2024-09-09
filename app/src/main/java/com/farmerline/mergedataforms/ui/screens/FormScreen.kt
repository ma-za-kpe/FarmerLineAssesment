package com.farmerline.mergedataforms.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen(
    formId: String,
    modifier: Modifier = Modifier
) {
    var formTitle by remember { mutableStateOf("") }
    var formDescription by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Form: $formId",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = formTitle,
            onValueChange = { formTitle = it },
            label = { Text("Form Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = formDescription,
            onValueChange = { formDescription = it },
            label = { Text("Form Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { /* TODO: Implement form submission logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Form")
        }
    }
}