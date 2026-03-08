package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.database.ScheduleDao
import org.jetbrains.compose.resources.painterResource


interface ListBoxItem {
    fun listBoxText(scheduleDao: ScheduleDao?): DisplayItems
}


val cardShape = RoundedCornerShape(topStart = 20.dp, bottomStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp
)


@Composable
fun <T : ListBoxItem> ListBox(scheduleDao: ScheduleDao?,item: T, onClick: (T) -> Unit) {
    val boxContent=item.listBoxText(scheduleDao)




    Surface(
        onClick = { onClick(item) },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = cardShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            //Content
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BoxLeftImage(boxContent)
                Box(Modifier.weight(1f).padding(16.dp)) {
                    BoxDetailsDisplay(boxContent)
                }

            }
        }
    }
}


@Composable
fun BoxLeftImage( item: DisplayItems) {
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
fun ImagePlaceholder() {
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
fun BoxDetailsDisplay(item: DisplayItems) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BoxChipsRow(item.chips)
        item.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        LowerRow(item.secondRowIcon,item.secondRow)
        LowerRow(item.thirdRowIcon,item.thirdRow)
        LowerRow(item.fourthRowIcon,item.fourthRow)

    }
}

@Composable
fun LowerRow(icon: DisplayIcon?,value:String?) {
    if (icon != null || value != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(20.dp),
                contentAlignment = Alignment.Center
            ) {
                when (icon) {
                    is DisplayIcon.Vector ->
                        Icon(
                            imageVector = icon.icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                    is DisplayIcon.Resource ->
                        Icon(
                            painter = painterResource(icon.res),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                    null -> Unit
                }
            }

            if (value != null) {
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
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
        //TODO add row with Icon and colour for success
        Text(
            text = chip.text ?: "Unknown",
            style = MaterialTheme.typography.labelMedium
        )
    }
}
