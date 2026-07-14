package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AgencyViewModel( private val scheduleDao: ScheduleDao, private val agencyID: String) : ViewModel() {

    val upcomingLaunches: StateFlow<List<Launch>> = scheduleDao
        .getUpcomingLaunchesByAgencyId(agencyID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    val pastLaunches: StateFlow<List<Launch>> = scheduleDao
        .getPreviousLaunchesByAgencyId(agencyID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val rockets: StateFlow<List<Rocket>> = scheduleDao
        .getRocketsByAgencyId(agencyID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    val reuse: StateFlow<List<Reuse>> = scheduleDao
        .getReuseByAgencyId(agencyID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )



}