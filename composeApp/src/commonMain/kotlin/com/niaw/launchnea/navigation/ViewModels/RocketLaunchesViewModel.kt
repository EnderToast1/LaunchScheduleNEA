package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class RocketLaunchesViewModel (
    private val scheduleDao: ScheduleDao,
    private val rocketID: String
    ) : ViewModel() {


    val upcomingLaunches: StateFlow<List<Launch>> = scheduleDao
        .getUpcomingLaunchesByRocketId(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val pastLaunches: StateFlow<List<Launch>> = scheduleDao
        .getPreviousLaunchesByRocketId(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}