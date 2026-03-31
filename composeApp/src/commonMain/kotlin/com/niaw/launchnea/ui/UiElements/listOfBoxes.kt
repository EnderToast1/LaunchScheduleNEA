package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.Settings.GlobalSettings.dateTimeFormat
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.ViewModels.ListBoxRocketInfoViewModel
import com.niaw.launchnea.navigation.ViewModels.ReuseBoxInfo
import com.niaw.launchnea.navigation.ViewModels.RocketLaunchesViewModel
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ListOfBoxesLaunches(scheduleDao: ScheduleDao?, listOfItem: List<Launch>, listState: LazyListState, onItemClick: (String) -> Unit) {
    if (listOfItem.isEmpty()){
        EmptyListDisplay()
    }

        LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
            items(listOfItem){ item ->
            ListBox(item.listBoxText(),onItemClick )
        }
    }
}

@Composable
fun ListOfBoxesRocket(scheduleDao: ScheduleDao?, listOfItem: List<Rocket>, listState: LazyListState, onItemClick: (String) -> Unit) {
    if (listOfItem.isEmpty()){
        EmptyListDisplay()
    }


    if (scheduleDao!=null) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listOfItem) { item ->
                val itemText = item.listBoxText()

                val viewModel = remember(item.rocketID) {
                    ListBoxRocketInfoViewModel(scheduleDao, item.rocketID)
                }

                val flights by viewModel.flights.collectAsState()
                itemText.thirdRow="Flights: $flights"

                val lastFlight:Launch? by viewModel.lastFlight.collectAsState()
                if (lastFlight != null) {
                    val duration = Duration.between(lastFlight?.launchTimeUTC, ZonedDateTime.now())

                    val days = duration.toMillis() / (1000.0 * 60 * 60 * 24)
                    val absDays = kotlin.math.abs(days)
                    val formattedDays = "%.1f".format(absDays)

                    val unit = if (absDays == 1.0) "day" else "days"

                    itemText.fourthRow =
                        "Last Flight: ${lastFlight?.launchTimeUTC?.format(DateTimeFormatter.ofPattern(dateTimeFormat))} ($formattedDays $unit ago)"
                }




                ListBox(itemText, onItemClick)
            }
        }
    }
}

@Composable
fun ListOfBoxesReuse(scheduleDao: ScheduleDao?, listOfItem: List<Reuse>, listState: LazyListState, onItemClick: (String) -> Unit) {
    if (listOfItem.isEmpty()){
        EmptyListDisplay()
    }
    if (scheduleDao!=null) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listOfItem) { item ->
                val itemText = item.listBoxText()

                val viewModel = remember(item.reuseID) {
                    ReuseBoxInfo(scheduleDao, item.reuseID)
                }

                val flights by viewModel.flights.collectAsState()
                val lastFlight:Launch? by viewModel.lastFlight.collectAsState()
                if (lastFlight != null) {
                    val duration = Duration.between(lastFlight?.launchTimeUTC, ZonedDateTime.now())

                    val days = duration.toMillis() / (1000.0 * 60 * 60 * 24)
                    val absDays = kotlin.math.abs(days)
                    val formattedDays = "%.1f".format(absDays)

                    val unit = if (absDays == 1.0) "day" else "days"

                    itemText.fourthRow =
                        "Last Flight: ${lastFlight?.launchTimeUTC?.format(DateTimeFormatter.ofPattern(dateTimeFormat))} ($formattedDays $unit ago)"
                }

                itemText.chips= listOf(Chip("$flights"))

                ListBox(itemText, onItemClick)
            }
        }
    }
}

@Composable
fun EmptyListDisplay(){
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Text("No items to display", style = MaterialTheme.typography.headlineLarge)
    }
}