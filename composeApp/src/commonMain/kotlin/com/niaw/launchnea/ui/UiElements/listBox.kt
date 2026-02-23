package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


val cardShape = RoundedCornerShape(topStart = 20.dp, bottomStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp
)
val boxModifier = Modifier.fillMaxWidth().height(140.dp).border(width = 1.dp, color = Color(0xB3787878), shape = cardShape).clip(cardShape).background(Color(0xFFFFFFFF)).padding(16.dp)


@Composable
fun <T : ListBoxItem> listBox(item: T, onClick: (T) -> Unit) {

        Box(//TODO add colour for success
            modifier = boxModifier.clickable { onClick(item) }
        ) {
                //Content
              Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                  val displayItems = item.listBoxText()

                    boxLeftImage(displayItems)
                    boxDetailsDisplay(displayItems)

            }
        }

}

interface ListBoxItem {
    fun listBoxText(): DisplayItems
}



@Composable
private fun boxLeftImage(item: DisplayItems) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .background(Color(0xFF1C1C22)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,//TODO add image URL display
            contentDescription = null
        )
        //TODO add licence and credit
    }
}



@Composable
private fun boxDetailsDisplay(item: DisplayItems) {
    boxChipsRow(item.chips)
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        item.secondRow?.let { Text(it, style = MaterialTheme.typography.bodySmall)}

        item.thirdRow?.let { Text(it, style = MaterialTheme.typography.bodySmall) }

        item.fourthRow?.let { Text(it, style = MaterialTheme.typography.bodySmall)}
    }
}

@Composable
fun boxChipsRow(chips: List<Chip>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {//TODO add 
       chips.forEach { chip ->
            boxChip(chip)
        }

    }
}

@Composable
fun boxChip(chip: Chip ) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFD0D0D0))
            .padding(horizontal = 5.dp, vertical = 1.dp)
    ) {
        //TODO add row with image and colour for success
        chip.abbrev?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
    }
}
