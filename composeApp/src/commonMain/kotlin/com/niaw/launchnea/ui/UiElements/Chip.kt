package com.niaw.launchnea.ui.UiElements

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import com.niaw.launchnea.utils.LandingType
import com.niaw.launchnea.utils.Status
import org.jetbrains.compose.resources.DrawableResource

data class Chip(
    val text : String?=null,
    val status : Status?=null,
    val success : Boolean?=null,
    val icon: DrawableResource?=null
)
