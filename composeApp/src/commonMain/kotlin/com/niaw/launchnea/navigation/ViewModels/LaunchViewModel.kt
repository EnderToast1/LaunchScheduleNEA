package com.niaw.launchnea.navigation.ViewModels

import com.niaw.launchnea.database.entities.Launch
import kotlinx.coroutines.flow.StateFlow

interface LaunchesViewModel {
    val launches: StateFlow<List<Launch>>
    fun updateSearch(query: String)
}