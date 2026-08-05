package com.niaw.launchnea.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.asds_icon
import launchnea.composeapp.generated.resources.capsule_parachute
import launchnea.composeapp.generated.resources.filledrocketh
import launchnea.composeapp.generated.resources.search_book
import launchnea.composeapp.generated.resources.waves
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun getIconForChip(reusable: Reusable): DrawableResource {//TODO Finish

    when (reusable.reuseType) {
        ReuseType.BOOSTER -> {
            return when (reusable.landingType) {
                LandingType.ASDS -> Res.drawable.asds_icon
                LandingType.RTLS -> Res.drawable.search_book   //IF starship catch else Pad
                LandingType.OCEAN -> Res.drawable.waves
                LandingType.PCL -> Res.drawable.search_book  //Booster under parachutes
                LandingType.HC -> Res.drawable.search_book  //Booster Under Heli
                LandingType.HL -> Res.drawable.search_book  // Booster flipped 90 to flat
                LandingType.EXP -> Res.drawable.waves
                LandingType.VL -> Res.drawable.search_book   //same as RTLS
                LandingType.PFL -> Res.drawable.search_book  //same as PCL
                else -> Res.drawable.search_book
            }

        }
        ReuseType.SHIP_CAPSULE -> {
            return when (reusable.landingType) {
                LandingType.RTLS -> Res.drawable.search_book   //Ship catch icon
                LandingType.OCEAN -> Res.drawable.waves
                LandingType.PCL -> Res.drawable.capsule_parachute
                LandingType.HL -> Res.drawable.search_book  //Space Plane landing
                LandingType.ATM -> Res.drawable.search_book  //capsule burning up
                LandingType.EXP -> Res.drawable.waves
                LandingType.VL -> Res.drawable.search_book   //same as RTLS
                LandingType.PFL -> Res.drawable.search_book //same as PCL
                else -> Res.drawable.search_book
            }


        }
        else -> {
            return Res.drawable.search_book
        }
    }



}