package com.niaw.launchnea.ui.UiElements

data class DisplayItems(
    val title : String?=null,
    val imageGroup: ImageGroup?=null,
    val secondRow : String?=null,
    val thirdRow : String?=null,
    val fourthRow : String?=null,
    val chips : List<Chip> = emptyList()

)
