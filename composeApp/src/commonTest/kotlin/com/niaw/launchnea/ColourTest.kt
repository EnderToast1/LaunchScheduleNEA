package com.niaw.launchnea


import androidx.compose.ui.graphics.Color
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.niaw.launchnea.utils.HSL
import com.niaw.launchnea.utils.toHSL
import com.niaw.launchnea.utils.tonal
import kotlin.test.Test

class ColourTest {

    @Test
    fun testColourToHSL() {
        val colour = Color(0xFFFF5100)
        val expected = HSL(19.058825F, 1F, 0.5F)
        val result = colour.toHSL()
        assertThat(result).isEqualTo(expected)

    }


    @Test
    fun testColorTonalAsHSL() {
        val colour = Color(0xFFFF5100)
        val expected = HSL(19.024391F, 0.8039216F, 0.5F)
        val result = tonal(colour, 0.5f, 0.8f)
        println(result.toHexString())
        assertThat(result.toHSL()).isEqualTo(expected)


    }



}


fun Color.toHexString(): String {
    val a = (alpha * 255).toInt()
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()

    return String.format("#%02X %02X %02X", r, g, b)
}