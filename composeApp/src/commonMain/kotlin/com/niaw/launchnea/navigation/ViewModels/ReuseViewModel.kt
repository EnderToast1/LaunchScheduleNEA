package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Reuse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ReuseViewModel(
    private val dao: ScheduleDao
    )
    : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val onlyShowActive = MutableStateFlow(false)

    fun updateSearch(query: String) {
            searchQuery.value = query
    }
    fun updateOnlyShowActive(showOnlyActive: Boolean) {
        onlyShowActive.value = showOnlyActive
    }

        @OptIn(ExperimentalCoroutinesApi::class)
        val reuse: StateFlow<List<Reuse>> =
            combine(
                searchQuery,
                onlyShowActive
            ) { query, activeOnly ->
                query to activeOnly
            }
                .flatMapLatest { (query, activeOnly) ->

                    when {
                        activeOnly && query.isBlank() -> {
                            dao.getActiveReuse()
                        }

                        activeOnly -> {
                            dao.getActiveReuseLikeName(query)
                        }

                        query.isBlank() -> {
                            dao.getReuseOrdered()
                        }

                        else -> {
                            dao.getReuseLikeName(query)
                        }
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
}
