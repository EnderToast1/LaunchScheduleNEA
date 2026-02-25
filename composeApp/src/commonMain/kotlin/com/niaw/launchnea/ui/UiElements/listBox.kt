package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.NoDrinks
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage


interface ListBoxItem {
    fun listBoxText(): DisplayItems
}


val cardShape = RoundedCornerShape(topStart = 20.dp, bottomStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp
)
val boxModifier = Modifier.fillMaxWidth()
                    .height(140.dp)
                    .border(width = 1.dp, color = Color(0xB3787878),
                  shape = cardShape)
                    .clip(cardShape)
                    .background(Color(0xFFFFFFFF))


@Composable
fun <T : ListBoxItem> ListBox(item: T, onClick: (T) -> Unit) {

        Box(//TODO add colour for success
            modifier = boxModifier.clickable { onClick(item) }
        ) {
                //Content
              Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                  val displayItems = item.listBoxText()

                  BoxLeftImage(displayItems)
                  Box(Modifier.weight(1f).padding(16.dp)) {
                      BoxDetailsDisplay(displayItems)
                  }

            }
        }

}


@Composable
private fun BoxLeftImage( item: DisplayItems) {
    val url = item.imageGroup?.imageUrl
    val shouldShow = url != null && item.imageGroup.show

    if (shouldShow) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Launch Image",
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            contentScale = ContentScale.FillHeight,
            loading = {//While image loading
                Box(
                    modifier = Modifier
                        .width(145.dp)
                        .fillMaxHeight()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            },
            // This catches 'NoSuchKey' / 404 errors
            error = {
                ImagePlaceholder()
            }
        )
    }
}

@Composable
private fun ImagePlaceholder() {
    Box(
        modifier = Modifier
            .width(140.dp)
            .fillMaxHeight()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ImageNotSupported,
            contentDescription = "Image not found",
            tint = Color.LightGray,
            modifier = Modifier.size(32.dp)
        )
    }
}


@Composable
private fun BoxDetailsDisplay(item: DisplayItems) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BoxChipsRow(item.chips)
        item.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        item.secondRow?.let { Text(it, style = MaterialTheme.typography.bodySmall)}

        item.thirdRow?.let { Text(it, style = MaterialTheme.typography.bodySmall) }

        item.fourthRow?.let { Text(it, style = MaterialTheme.typography.bodySmall)}
    }
}

@Composable
fun BoxChipsRow(chips: List<Chip>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {//TODO add 
       chips.forEach { chip ->
            BoxChip(chip)
        }

    }
}

@Composable
fun BoxChip(chip: Chip ) {
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
