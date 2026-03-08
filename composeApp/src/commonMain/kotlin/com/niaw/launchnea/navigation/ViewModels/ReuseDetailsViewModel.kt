package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn



class ReuseLaunchesViewModel(
    private val scheduleDao: ScheduleDao,
    private val reuseID: String
) : ViewModel() {


    val upcomingLaunches: StateFlow<List<Launch>> = scheduleDao
        .getUpcomingLaunchesByReuseId(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val pastLaunches: StateFlow<List<Launch>> = scheduleDao
        .getPreviousLaunchesByReuseId(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val reuses: StateFlow<Int> = scheduleDao.getReuseCountById(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val averageReuseTime = scheduleDao.getAverageReuseTime(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val fastestReuseTime = scheduleDao.getFastestReuseTime(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}