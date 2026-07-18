package com.niaw.launchnea.ui.UiElements

import com.niaw.launchnea.utils.LandingType
import com.niaw.launchnea.utils.Status

data class Chip(
    val text : String?=null,
    val status : Status?=null,
    val success : Boolean?=null,
    val type: LandingType?=null
)
