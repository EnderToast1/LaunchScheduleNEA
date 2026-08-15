package com.niaw.launchnea.utils

import launchnea.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

fun getIconForChip(reusable: Reusable): DrawableResource {

    when (reusable.reuseType) {
        ReuseType.BOOSTER -> {
            return when (reusable.landingType) {
                LandingType.ASDS -> Res.drawable.asds_icon
                LandingType.RTLS -> {
                    if (reusable.reuseName?.contains("Booster", ignoreCase = true) == true) {Res.drawable.boostercatch} else {Res.drawable.rtls}
                }
                LandingType.OCEAN -> Res.drawable.waves
                LandingType.PCL -> Res.drawable.booster_parachute
                LandingType.HC -> Res.drawable.helicopter_catch
                LandingType.HL -> Res.drawable.horizontal_landing
                LandingType.EXP -> Res.drawable.waves
                LandingType.VL -> Res.drawable.rtls
                LandingType.PFL -> Res.drawable.booster_parachute

                else -> Res.drawable.unknown
            }

        }
        ReuseType.SHIP_CAPSULE -> {
            return when (reusable.landingType) {
                LandingType.RTLS -> Res.drawable.ship_catch
                LandingType.OCEAN -> Res.drawable.waves
                LandingType.PCL -> Res.drawable.capsule_parachute
                LandingType.HL -> Res.drawable.horizontal_landing
                LandingType.ATM -> Res.drawable.atmospheric_burn_up
                LandingType.EXP -> Res.drawable.waves
                LandingType.VL -> Res.drawable.rtls
                LandingType.PFL -> Res.drawable.capsule_parachute

                else -> Res.drawable.unknown
            }


        }
        else -> {
            return Res.drawable.unknown
        }
    }



}