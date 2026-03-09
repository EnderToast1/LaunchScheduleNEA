package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao

@Composable
fun ListOfBoxes(scheduleDao: ScheduleDao?, listOfItem: List<Launches>, listState: LazyListState, onItemClick: (String) -> Unit) {

    //TODO if List empty display no items
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOfItem(Launches) { item ->
            ListBox(scheduleDao = scheduleDao,item = item.listBoxText, onClick =onItemClick )
        }
    }
}

@Composable
fun ListOfBoxes(scheduleDao: ScheduleDao?, listOfItem: List<Rocket>, listState: LazyListState, onItemClick: (String) -> Unit) {

    //TODO if List empty display no items
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOfItem(Rocket) { item ->
            ListBox(scheduleDao = scheduleDao,item = item.listBoxText, onClick =onItemClick )
        }
    }
}

@Composable
fun ListOfBoxes(scheduleDao: ScheduleDao?, listOfItem: List<Reuse>, listState: LazyListState, onItemClick: (String) -> Unit) {

    //TODO if List empty display no items
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOfItem(Reuse) { item ->
            ListBox(scheduleDao = scheduleDao,item = item.listBoxText, onClick =onItemClick )
        }
    }
}
