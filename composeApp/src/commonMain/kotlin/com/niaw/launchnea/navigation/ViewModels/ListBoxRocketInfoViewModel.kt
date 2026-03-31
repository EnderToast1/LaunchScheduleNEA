package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListBoxRocketInfoViewModel(
    private val scheduleDao: ScheduleDao,
    private val rocketID: String
) : ViewModel() {
    val flights = scheduleDao.getRocketCountById(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val lastFlight: StateFlow<Launch?> = scheduleDao.getLastFlightByRocketId(rocketID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


}