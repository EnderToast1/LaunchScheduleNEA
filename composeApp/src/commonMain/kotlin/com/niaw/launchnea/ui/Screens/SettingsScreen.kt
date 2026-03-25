package com.niaw.launchnea.ui.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.utils.AppSettings
import com.niaw.launchnea.utils.DateFormat
import com.niaw.launchnea.utils.GlobalSettings
import com.niaw.launchnea.utils.GlobalSettings.dateFormat
import com.niaw.launchnea.utils.GlobalSettings.dateTimeFormat
import com.niaw.launchnea.utils.GlobalSettings.timeFormat
import com.niaw.launchnea.utils.ThemeMode
import com.niaw.launchnea.utils.TimeFormat
import com.niaw.launchnea.utils.toDateFormat
import com.niaw.launchnea.utils.toLabel
import com.niaw.launchnea.utils.toTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(currentTheme: ThemeMode, appSettings: AppSettings,
                   onThemeChanged: (ThemeMode) -> Unit //callback
) {
    println("Click on Settings bottom navigation.")

    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        Text("Theme", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

        ThemePickerBox(
            currentMode = currentTheme,
            onModeSelected = { newMode ->
                onThemeChanged(newMode)
                appSettings.saveTheme(newMode) // Persists to Disk
            }
        )
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.secondaryContainer)) {
            Box(Modifier.fillMaxWidth().padding(10.dp, 5.dp)) {
                TimeFormatPickerBox(
                    currentFormat = timeFormat.toTimeFormat(),
                    onTimeFormatSelected = { newFormat ->
                        GlobalSettings.setTimeFormat(newFormat)
                        GlobalSettings.setDateTimeFormat()
                        appSettings.saveTimeFormat(newFormat)
                    }
                )
            }


            Box(Modifier.fillMaxWidth().padding(10.dp, 5.dp)) {
                DateFormatPickerBox(
                    currentFormat = dateFormat.toDateFormat(),
                    onTimeFormatSelected = { newFormat ->
                        GlobalSettings.setDateFormat(newFormat)
                        GlobalSettings.setDateTimeFormat()
                        appSettings.saveDateFormat(newFormat)
                    }
                )
            }

            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .border(width = 3.dp,color = MaterialTheme.colorScheme.outline,shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp, 5.dp)
            ) {
                Text(
                    text = "Preview ${LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat))}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

    }
    //TODO make Screen
}

@Composable
fun ThemePickerBox(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ThemeMode.entries.forEach { mode ->
                val isSelected = currentMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onModeSelected(mode) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.name,
                        // High contrast text based on selection
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeFormatPickerBox(
    currentFormat: TimeFormat,
    onTimeFormatSelected: (TimeFormat) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = currentFormat.toLabel(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Time Format") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            TimeFormat.entries.forEach { format ->
                DropdownMenuItem(
                    text = { Text(format.toLabel()) },
                    onClick = {
                        onTimeFormatSelected(format)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun DateFormatPickerBox(
    currentFormat: DateFormat,
    onTimeFormatSelected: (DateFormat) -> Unit
) {
    val weekdayOptions = listOf("None", "Short", "Full")
    val orderOptions = listOf("Day-Month-Year", "Month-Day-Year", "Year-Month-Day")
    val monthOptions = listOf("Numeric", "Short", "Full")

    var selectedWeekday by remember { mutableStateOf(weekdayOptions[0]) }
    var selectedOrder by remember { mutableStateOf(orderOptions[0]) }
    var selectedMonth by remember { mutableStateOf(monthOptions[0]) }

    // Build pattern dynamically
    val pattern = remember(selectedWeekday, selectedOrder, selectedMonth) {
        val weekdayPattern = when (selectedWeekday) {
            "None" -> ""
            "Short" -> "E "
            "Full" -> "EEEE "
            else -> ""
        }

        val monthPattern = when (selectedMonth) {
            "Numeric" -> "MM"
            "Short" -> "MMM"
            "Full" -> "MMMM"
            else -> "MM"
        }

        val day = "dd"
        val year = "yyyy"

        val orderPattern = when (selectedOrder) {
            "Day-Month-Year" -> listOf(day, monthPattern, year)
            "Month-Day-Year" -> listOf(monthPattern, day, year)
            "Year-Month-Day" -> listOf(year, monthPattern, day)
            else -> listOf(day, monthPattern, year)
        }

        weekdayPattern + orderPattern.joinToString(separator = "/")
    }

    // Notify parent
    LaunchedEffect(pattern) {
        onTimeFormatSelected(pattern.toDateFormat())
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DropdownMenuBox("Weekday", weekdayOptions, selectedWeekday) { selectedWeekday = it }
        DropdownMenuBox("Order", orderOptions, selectedOrder) { selectedOrder = it }
        DropdownMenuBox("Month Type", monthOptions, selectedMonth) { selectedMonth = it }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }