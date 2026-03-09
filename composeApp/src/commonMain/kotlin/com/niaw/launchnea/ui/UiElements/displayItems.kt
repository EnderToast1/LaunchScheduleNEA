package com.niaw.launchnea.ui.UiElements

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

data class DisplayItems(
    val id : String,
    val title : String?=null,
    val imageGroup: ImageGroup?=null,
    val secondRowIcon: DisplayIcon? = null,
    val secondRow : String?=null,
    val thirdRowIcon: DisplayIcon? = null,
    val thirdRow : String?=null,
    val fourthRowIcon: DisplayIcon? = null,
    var fourthRow : String?=null,
    var chips : List<Chip> = emptyList(),
    var success: Status? =Status.UNKNOWN

)
sealed class DisplayIcon {
    data class Vector(val icon: ImageVector) : DisplayIcon()
    data class Resource(val res: DrawableResource) : DisplayIcon()
}
