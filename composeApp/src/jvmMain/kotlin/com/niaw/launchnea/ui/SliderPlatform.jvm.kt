package com.niaw.launchnea.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.descriptors.StructureKind

@Composable
actual fun SliderPlatform(givenList: List<Any>, listState: LazyListState) {

    Box(modifier = Modifier.fillMaxSize()) {
        if (givenList.isNotEmpty()) {
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}