package com.example.learning_android.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangeDropdown(
    label: String,
    selectedTime: Instant,
    onTimeChange: (time: Instant) -> Unit,
    modifier: Modifier = Modifier
){
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember {mutableStateOf(false)}

    var tempDate by remember { mutableStateOf<LocalDate?>(null)}

    val timeLabel = selectedTime
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("MMM d, HH:mm"))

    ExposedDropdownMenuBox(
        expanded = showTimePicker || showDatePicker,
        onExpandedChange = {
            if(showTimePicker || showDatePicker) {
                showTimePicker = false
                showDatePicker = false
            }
            else{
                showDatePicker = true
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = timeLabel,
            onValueChange = {},
            readOnly = true,
            label = {Text(label)},
            trailingIcon = {
                val rotation by animateFloatAsState(
                    targetValue = if (showTimePicker || showDatePicker) 180f else 0f,
                    label = "ChevronRotation"
                )

                Icon(
                    painter = painterResource(R.drawable.ic_chevron_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            textStyle = TextStyle(fontSize = 14.sp),
            enabled = true
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedTime.toEpochMilli()
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                confirmButton = {
                    TextButton(onClick = {
                        tempDate = datePickerState.getSelectedDate();
                        showDatePicker = false
                        showTimePicker = true
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false}
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        }
        if(showTimePicker) {
            val currentTime = selectedTime.atZone(ZoneId.systemDefault())
            val timePickerState = rememberTimePickerState(
                initialHour = currentTime.hour,
                initialMinute = currentTime.minute,
                is24Hour = true,
            )

            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text("Select Time") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val date = tempDate ?: LocalDate.now();

                            val hour = timePickerState.hour
                            val minutes = timePickerState.minute

                            val timeInstant = date.atTime(hour, minutes).atZone(ZoneId.systemDefault()).toInstant()

                            onTimeChange(timeInstant)

                            showDatePicker = false
                            showTimePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showTimePicker = false
                            showDatePicker = true
                        }
                    ) {
                        Text("Back")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.background,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.onSurface,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                        clockDialColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}