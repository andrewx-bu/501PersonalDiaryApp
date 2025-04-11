package com.example.personaldiaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personaldiaryapp.ui.theme.PersonalDiaryAppTheme
import java.util.Date
import java.util.Locale
import androidx.compose.material3.*
import java.text.SimpleDateFormat
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(this)
        viewModel = ViewModel(dataStoreManager)

        enableEdgeToEdge()
        setContent {
            PersonalDiaryAppTheme {
                Column {
                    SettingsScreen(viewModel = viewModel)
                    DiaryScreen()
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: ViewModel) {
    val theme by viewModel.theme.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Theme")
        Row {
            Button(
                onClick = { viewModel.updateTheme("Light") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (theme == "Light") Color.Magenta else Color.Unspecified
                )
            ) {
                Text("Light")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { viewModel.updateTheme("Dark") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (theme == "Dark") Color.Magenta else Color.Unspecified
                )
            ) {
                Text("Dark")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Font Size: ${fontSize ?: 16}")
        Slider(
            value = (fontSize ?: 16).toFloat(),
            onValueChange = { viewModel.updateFontSize(it.toInt()) },
            valueRange = 12f..18f,
            steps = 5
        )
    }
}

@Composable
fun DiaryScreen() {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = remember {
        current.format(formatter)
    }

    var selectedDate by remember { mutableStateOf(today) }

    Column(modifier = Modifier.padding(16.dp)) {
        DiaryDatePicker(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text("Selected date: $selectedDate")
        Button(onClick = { showDialog = true }) {
            Text("Pick Date")
        }
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        onDateSelected(formatMillisToYMD(millis))
                    }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun formatMillisToYMD(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}
