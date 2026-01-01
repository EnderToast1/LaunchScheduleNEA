package com.niaw.launchnea

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime

import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Location
import com.niaw.launchnea.DataClasses.ResultsState
import com.niaw.launchnea.database.ScheduleDao
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun App(scheduleDao: ScheduleDao) {
    MaterialTheme{
        val launches by scheduleDao.getLaunches().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()

        LaunchedEffect(true) {
            val launchList = listOf(
                Launches(
                    "1",
                    "Name1",
                    "Pad1",
                    "agency1",
                    "rocket1",
                    "Version1",
                    ZonedDateTime.of(2026, 1, 2, 12, 33, 20, 0, ZoneId.of("Europe/Paris")),
                    LocalDateTime.of(2026, 1, 2, 10, 12, 20),
                    emptyList(),
                    ResultsState.SUCCESS,
                    ZonedDateTime.of(2026, 1, 2, 12, 32, 12, 0, ZoneId.of("Europe/Paris")),
                    ZonedDateTime.of(2026, 1, 3, 12, 32, 12, 0, ZoneId.of("Europe/Paris")),
                    "IMG_URL1",
                    "Credit1",
                    "LicenseType1",
                    90.4,
                    "AgencyName1",
                    "RocketName1",
                    "VersionName1",
                    "desc1",
                    "orbit1",
                    Location(40.3, 13.2),
                    "Loc1",
                    "Country1",
                    "PadTZ1"
                )
            )

            launchList.forEach { launches: Launches ->
                scheduleDao.upsertLaunches(launches)
            }

        }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(), contentPadding = PaddingValues(16.dp)
            ){
                items(launches){launch ->
                    Text(text = launch.missionName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    scheduleDao.deleteLaunches(launch)
                                }
                            }
                            .padding(16.dp))
                }
            }


    }
}