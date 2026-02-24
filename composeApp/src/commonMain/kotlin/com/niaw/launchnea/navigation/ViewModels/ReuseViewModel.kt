package com.niaw.launchnea.navigation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Reuse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ReuseViewModel(
    private val dao: ScheduleDao
    )
    : ViewModel() {

        private val searchQuery = MutableStateFlow("")

        fun updateSearch(query: String) {
            searchQuery.value = query
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        val reuse: StateFlow<List<Reuse>> =
            searchQuery
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        dao.getReuse()
                    } else {
                        dao.getReuseLikeName(query)
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
    }
