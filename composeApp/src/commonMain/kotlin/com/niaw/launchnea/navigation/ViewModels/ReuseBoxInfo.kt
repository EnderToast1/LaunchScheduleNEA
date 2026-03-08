package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReuseBoxInfo (
    private val scheduleDao: ScheduleDao,
    private val reuseID: String
    ) : ViewModel() {


        val lastFlight = scheduleDao.getLastFlightByReuseId(reuseID)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val flights: StateFlow<Int> = scheduleDao.getReuseCountById(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )





}