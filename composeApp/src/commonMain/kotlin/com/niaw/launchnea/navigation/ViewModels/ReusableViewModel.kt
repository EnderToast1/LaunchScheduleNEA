package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.niaw.launchnea.database.ScheduleDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.ZonedDateTime

class ReusableViewModel(
    private val scheduleDao: ScheduleDao,
    private val reuseID: String
) : ViewModel() {
    val long: Long= ZonedDateTime.now().getLong()
    val reuses: StateFlow<Int> = scheduleDao.getReuseCountById(reuseID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )
    val DayTurnAround: StateFlow<Int> =  scheduleDao.getTimeSinceLastFlight(reuseID,)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
        )
}