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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket

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
        items(listOfItem){ item ->
            ListBox(item.listBoxText(),onItemClick )
        }
    }
}

@Composable
fun ListOfBoxesReuse(scheduleDao: ScheduleDao?, listOfItem: List<Reuse>, listState: LazyListState, onItemClick: (String) -> Unit) {
    if (listOfItem.isEmpty()){
        EmptyListDisplay()
    }

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
        items(listOfItem){ item ->
            ListBox(item.listBoxText(),onItemClick )
        }
    }
}

@Composable
fun EmptyListDisplay(){
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Text("No items to display", style = MaterialTheme.typography.headlineLarge)
    }
}