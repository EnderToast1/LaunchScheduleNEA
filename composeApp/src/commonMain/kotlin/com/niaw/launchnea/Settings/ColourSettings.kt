package com.niaw.launchnea.Settings

import androidx.compose.ui.graphics.Color
import com.niaw.launchnea.utils.ColourTheme


fun ColourTheme.getSeedColour(): Color {
    return when(this) {
        ColourTheme.RED -> Color(0xFF9C2A2A)        // Deep Red
        ColourTheme.GREEN -> Color(0xFF386A20)      //  Green
        ColourTheme.BLUE -> Color(0xFF0061A4)       //  Blue
        ColourTheme.YELLOW -> Color(0xFFDAA520)     //  Yellow
        ColourTheme.CYAN -> Color(0xFF006A6A)       // Teal/Cyan
        ColourTheme.MAGENTA -> Color(0xFF6750A4)    // Default M3  Purple
        ColourTheme.MONOCHROME -> Color(0xFF434343) // Dark Grey
    }
}













