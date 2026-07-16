package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    val versions: StateFlow<List<RocketVersion>> = scheduleDao
        .getRocketVersionsByRocketId(rocketID)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
        )

    val successes:StateFlow<Int> = scheduleDao.getRocketSuccessCountById(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )


    val failures:StateFlow<Int> = scheduleDao.getRocketFailuresCountById(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )


    val partialFailures:StateFlow<Int> = scheduleDao.getRocketPartialSuccessCountById(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val launches:StateFlow<List<Launch>> = scheduleDao.getPreviousLaunchesByRocketId(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


}