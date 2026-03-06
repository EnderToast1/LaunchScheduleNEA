package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun IconTextRow(text:String, description: String, textStyle: TextStyle, painter: Painter, modifier: Modifier, tint: Color){
    val iconSize = with(LocalDensity.current) {
        textStyle.fontSize.toDp()
    }

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = textStyle,
            color = tint
        )
    }
}