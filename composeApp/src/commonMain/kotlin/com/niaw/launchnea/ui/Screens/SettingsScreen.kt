package com.niaw.launchnea.ui.Screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.niaw.launchnea.Settings.AppSettings
import com.niaw.launchnea.utils.ColourTheme
import com.niaw.launchnea.utils.DateFormat
import com.niaw.launchnea.Settings.GlobalSettings
import com.niaw.launchnea.Settings.GlobalSettings.dateFormat
import com.niaw.launchnea.Settings.GlobalSettings.dateTimeFormat
import com.niaw.launchnea.Settings.GlobalSettings.failureColour
import com.niaw.launchnea.Settings.GlobalSettings.holdColour
import com.niaw.launchnea.Settings.GlobalSettings.inFlightColour
import com.niaw.launchnea.Settings.GlobalSettings.partialFailureColour
import com.niaw.launchnea.Settings.GlobalSettings.successColour
import com.niaw.launchnea.Settings.GlobalSettings.timeFormat
import com.niaw.launchnea.utils.ThemeMode
import com.niaw.launchnea.utils.TimeFormat
import com.niaw.launchnea.Settings.getSeedColour
import com.niaw.launchnea.Settings.initialMonth
import com.niaw.launchnea.Settings.initialOrder
import com.niaw.launchnea.Settings.initialWeekday
import com.niaw.launchnea.Settings.toDateFormat
import com.niaw.launchnea.Settings.toLabel
import com.niaw.launchnea.Settings.toTimeFormat
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.choose_Theme_colour
import launchnea.composeapp.generated.resources.choose_success_type_colour
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(currentTheme: ThemeMode, currentColourTheme: ColourTheme, appSettings: AppSettings,
                   onThemeChanged: (ThemeMode) -> Unit, //callback
                   onColourThemeChanged: (ColourTheme) -> Unit //callback
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(scrollState).padding(5.dp)) {

        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer).padding(5.dp)) {
            Text("Choose Theme", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(15.dp,5.dp))
            ThemePickerBox(
                currentMode = currentTheme,
                onModeSelected = { newMode ->
                    onThemeChanged(newMode)
                    appSettings.saveTheme(newMode) // Persists to Disk
                })
            ColourThemePickerBox(
                currentColourTheme = currentColourTheme,
                onColourThemeChanged = { newColourTheme ->
                    onColourThemeChanged(newColourTheme)
                    appSettings.saveColourTheme(newColourTheme) //Persists to Disk
                })
        }
        Spacer(Modifier.height(10.dp))
        TimeDateBox(appSettings)
        Spacer(Modifier.height(10.dp))
        SuccessStatusBox(appSettings)


    }

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

@Composable
fun ColourThemePickerBox(
    currentColourTheme: ColourTheme,
    onColourThemeChanged: (ColourTheme) -> Unit
){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = stringResource(Res.string.choose_Theme_colour),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between orbs
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColourTheme.entries.forEach { theme ->
                ColorSeedOrb(
                    modifier = Modifier.weight(1f), // Forces orb to take equal width
                    theme = theme,
                    isSelected = currentColourTheme == theme,
                    onClick = { onColourThemeChanged(theme) }
                )
            }
        }
    }
}


@Composable
fun ColorSeedOrb(
    modifier: Modifier = Modifier,
    theme: ColourTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val seedColor = theme.getSeedColour()
    // Create a scale value that animates between 1.0f and 1.25f
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.6f, // Adds a tiny bit of "bounce"
            stiffness = 400f
        ),
        label = "OrbScale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f) //Keeps it a circle as it scales with width
            .scale(scale)
            .clip(CircleShape)
            .background(seedColor)
            .border(
                width = if (isSelected) 2.dp else (-1).dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                modifier = Modifier.fillMaxSize(0.5f), // Icon scales with the orb size
                tint = if (seedColor.luminance() > 0.5f) Color.Black else Color.White
            )
        }
    }
}

@Composable
fun TimeDateBox(
    appSettings: AppSettings
){
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.secondaryContainer)) {
        Box(Modifier.fillMaxWidth().padding(10.dp, 10.dp,10.dp,0.dp)) {
            Text("Choose Date / Time Formating Options", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }



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
        var currentDateFormat by remember {
            mutableStateOf(appSettings.getDateFormat())
        }
        Box(Modifier.fillMaxWidth().padding(10.dp, 5.dp)) {
            DateFormatPickerBox(
                currentFormat = currentDateFormat,
                onDateFormatSelected = { newFormat ->
                    currentDateFormat = newFormat
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
                text = "Preview: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat))}",
                style = MaterialTheme.typography.headlineMedium
            )
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
    onDateFormatSelected: (DateFormat) -> Unit
) {
    val weekdayOptions = listOf("None", "Short", "Full")
    val orderOptions = listOf("Day-Month-Year", "Month-Day-Year", "Year-Month-Day")
    val monthOptions = listOf("Numeric", "Short", "Full")

    var selectedWeekday by rememberSaveable {
        mutableStateOf(currentFormat.initialWeekday())
    }

    var selectedOrder by rememberSaveable {
        mutableStateOf(currentFormat.initialOrder())
    }

    var selectedMonth by rememberSaveable {
        mutableStateOf(currentFormat.initialMonth())
    }

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
        onDateFormatSelected(pattern.toDateFormat())
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


@Composable
fun SuccessStatusBox(appSettings: AppSettings) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp)) {
        Text(stringResource(Res.string.choose_success_type_colour), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)


        SuccessTypeColourPickerBox("Success",Color.Green,successColour){ newColour ->
            successColour =(newColour)
            appSettings.saveColourSuccess(newColour)
        }

        SuccessTypeColourPickerBox("Partial Failure",Color.Yellow,partialFailureColour){ newColour ->
            partialFailureColour=newColour
            appSettings.saveColourPartialFailure(newColour)
        }

        SuccessTypeColourPickerBox("Failure",Color.Red,failureColour){ newColour ->
            failureColour=newColour
            appSettings.saveColourFailure(newColour)
        }

        SuccessTypeColourPickerBox("In Flight",Color.Blue,inFlightColour){ newColour ->
            inFlightColour=newColour
            appSettings.saveColourInFlight(newColour)
        }
        SuccessTypeColourPickerBox("Hold",Color(0xFFFF6600),holdColour){ newColour ->
            holdColour=newColour
            appSettings.saveColourHold(newColour)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessTypeColourPickerBox(
    title: String,
    defaultColour: Color,
    currentColour: Color,
    onColourSelected: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showColourPicker by remember { mutableStateOf(false) }

    val controller = rememberColorPickerController()
    var pickedColour by remember { mutableStateOf(currentColour) }

    val selectedLabel = if (currentColour == defaultColour) "Default" else "More"

    Column(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text(title, style = MaterialTheme.typography.labelLarge) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = currentColour,
                    unfocusedBorderColor = currentColour.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("Default") },
                    leadingIcon = {
                        Box(
                            Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(defaultColour)
                        )
                    },
                    onClick = {
                        onColourSelected(defaultColour)
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Other") },
                    leadingIcon = {
                        Box(
                            Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.sweepGradient(
                                        colors = listOf(
                                            Color.Red,
                                            Color.Yellow,
                                            Color.Green,
                                            Color.Cyan,
                                            Color.Blue,
                                            Color.Magenta,
                                            Color.Red
                                        )
                                    )
                                )
                        )
                    },
                    onClick = {
                        pickedColour = currentColour
                        expanded = false
                        showColourPicker = true
                    }
                )
            }
        }
    }


    // Outside the dropdown
    if (showColourPicker) {
        AlertDialog(
            onDismissRequest = { showColourPicker = false },
            title = { Text(stringResource(Res.string.choose_Theme_colour)) },
            text = {
                HsvColorPicker(
                    modifier = Modifier.size(250.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope ->
                        pickedColour = colorEnvelope.color
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onColourSelected(pickedColour)
                        showColourPicker = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showColourPicker = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
