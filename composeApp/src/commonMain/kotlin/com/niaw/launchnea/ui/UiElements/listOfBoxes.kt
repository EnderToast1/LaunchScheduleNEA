package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T : ListBoxItem> ListOfBoxes(listOfItem: List<T>, listState: LazyListState, onItemClick: (T) -> Unit) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(listOfItem) { item ->
            listBox(item = item, onClick =onItemClick )
        }
    }
}