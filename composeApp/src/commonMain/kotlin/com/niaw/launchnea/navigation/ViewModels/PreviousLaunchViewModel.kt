package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class PreviousLaunchViewModel (
    private val dao: ScheduleDao
)
    : ViewModel(),LaunchesViewModel {

    private val searchQuery = MutableStateFlow("")

    override fun updateSearch(query: String) {
        searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val launches: StateFlow<List<Launch>> =
        searchQuery
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    dao.getPreviousLaunches()
                } else {
                    dao.getPreviousLaunchesLikeName(query)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
