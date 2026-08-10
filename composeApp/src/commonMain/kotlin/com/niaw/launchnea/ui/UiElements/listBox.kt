package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Paragliding
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.Settings.GlobalSettings.failureColour
import com.niaw.launchnea.Settings.GlobalSettings.holdColour
import com.niaw.launchnea.Settings.GlobalSettings.inFlightColour
import com.niaw.launchnea.Settings.GlobalSettings.partialFailureColour
import com.niaw.launchnea.Settings.GlobalSettings.successColour
import com.niaw.launchnea.utils.LandingType
import com.niaw.launchnea.utils.Status
import com.niaw.launchnea.utils.abbreviate
import org.jetbrains.compose.resources.painterResource


interface ListBoxItem {
    fun listBoxText(): DisplayItems
}


val cardShape = RoundedCornerShape(topStart = 20.dp, bottomStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp
)


@Composable
fun ListBox(boxContent: DisplayItems, onClick: (String) -> Unit) {
    val launchBorder = when (boxContent.success) {
        Status.UNKNOWN,
        Status.TBC,
        Status.TBD,
        Status.GO_FOR_LAUNCH,
        Status.STATUS_MISSING -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        else -> BorderStroke(
            width = 3.dp,
            color = when (boxContent.success) {
                Status.SUCCESS,
                Status.PAYLOAD_DEPLOYED -> successColour

                Status.FAILURE -> failureColour

                Status.PARTIAL_SUCCESS -> partialFailureColour

                Status.ON_HOLD -> holdColour

                Status.IN_FLIGHT -> inFlightColour

                else -> MaterialTheme.colorScheme.outline // fallback
            }
        )
    }


    Surface(
        onClick = { onClick(boxContent.id) },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = cardShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp,
        border = launchBorder
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            //Content
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BoxLeftImage(boxContent.imageGroup)
                Box(Modifier.weight(1f).padding(16.dp,10.dp)) {
                    BoxDetailsDisplay(boxContent)
                }

            }
        }
    }
}


@Composable
fun BoxLeftImage( item: ImageGroup?) {
    val url = item?.imageUrl
    val shouldShow = url != null && item.show
    if ((url == null) and (item?.show == true)) {ImagePlaceholder()}

    if (shouldShow) {
        Box(Modifier.width(145.dp).fillMaxHeight(), contentAlignment = Alignment.BottomStart) {
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
            Box(Modifier.padding(2.dp).clip(RoundedCornerShape(30.dp)).background(Color.LightGray).padding(5.dp,0.dp), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = if ((item.imageCredit=="null") or (item.imageCredit==null)){
                        "Unknown"
                    }else if(item.imageCredit?.length!!>=10) {
                        abbreviate(item.imageCredit)

                    }else{
                        item.imageCredit
                    },
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodyMedium,

                )
            }
        }
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
    ) {
       chips.forEach { chip ->
            BoxChip(chip)
        }

    }
}

@Composable
fun BoxChip(chip: Chip ) {
    var colour=MaterialTheme.colorScheme.secondaryContainer
    if ((chip.success==true) or (chip.status==Status.SUCCESS)){
        colour=successColour
    }else if ((chip.success==false) or (chip.status==Status.FAILURE)){
        colour=failureColour
    }else if (chip.status==Status.PARTIAL_SUCCESS){
        colour=partialFailureColour
    }

    val icon = if (chip.icon != null) {painterResource(chip.icon)}
               else{rememberVectorPainter(Icons.Default.Shield)}

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colour)
            .padding(horizontal = 5.dp, vertical = 3.dp)
    ) {

        IconTextRow(chip.text?.ifBlank { "Unknown" } ?: "Unknown",chip.text?.ifBlank { "Unknown" } ?: "Unknown",MaterialTheme.typography.labelMedium,icon,Modifier,colour.onColor())
        
    }
}


fun Color.onColor(): Color {
    return if (luminance() > 0.5f) Color.DarkGray else Color.White
}