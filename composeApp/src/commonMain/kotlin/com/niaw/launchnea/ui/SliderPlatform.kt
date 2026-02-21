package com.niaw.launchnea.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import kotlinx.serialization.descriptors.StructureKind

@Composable
expect fun SliderPlatform(givenList: List<Any>,listState: LazyListState)
