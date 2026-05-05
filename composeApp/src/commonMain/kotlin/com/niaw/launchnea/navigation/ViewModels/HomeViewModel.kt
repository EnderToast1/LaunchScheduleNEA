package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel( private val scheduleDao: ScheduleDao) : ViewModel() {

    val StaredupcomingLaunches: StateFlow<List<Launch>> = scheduleDao
        .getStarredUpcomingLaunches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    val StaredpastLaunches: StateFlow<List<Launch>> = scheduleDao
        .getStarredPreviousLaunches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    val upcomingLaunches: StateFlow<List<Launch>> = scheduleDao
        .getUpcomingLaunches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    val pastLaunches: StateFlow<List<Launch>> = scheduleDao
        .getPreviousLaunches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

}