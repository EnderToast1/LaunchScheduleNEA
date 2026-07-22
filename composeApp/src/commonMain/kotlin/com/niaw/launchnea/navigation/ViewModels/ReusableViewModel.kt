package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.niaw.launchnea.database.ScheduleDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime

class ReusableViewModel(
    private val scheduleDao: ScheduleDao,
    private val reuseID: String,
    private val launchTime: ZonedDateTime
) : ViewModel() {
   private val _dayTurnAround = MutableStateFlow<Duration?>(null)
    val dayTurnAround: StateFlow<Duration?> = _dayTurnAround

    init {
        viewModelScope.launch {
            val lastFlight = scheduleDao.getTimeSinceLastFlight(reuseID, launchTime)
            if (lastFlight != null) {
                _dayTurnAround.value = Duration.between(lastFlight, launchTime)
            }
            else{
                _dayTurnAround.value = Duration.ofDays(99999)
            }
        }
    }
}
