package com.github.jesushzc.pokedex.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

/**
 * Calculate the dominant color of a drawable
 */
fun calcDominantColors(drawable: Drawable, onFinish: (backgroundColor: Color, textColor: Color) -> Unit) {
    val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

    Palette.from(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let { backgroundColorValue ->
            val backgroundColor = Color(backgroundColorValue)

            val backgroundColorInt = backgroundColor.toArgb()

            val textColor = if (ColorUtils.calculateLuminance(backgroundColorInt) < 0.5) {
                Color.White
            } else {
                Color.Black
            }

            onFinish(backgroundColor, textColor)
        }
    }
}

/**
 * Replace / with ## to avoid issues with the URL
 */
fun String.replaceWithSharp(): String {
    return this.replace("/", "##")
}

/**
 * Replace ## with / to avoid issues with the URL
 */
fun String.replaceWithSlash(): String {
    return this.replace("##", "/")
}
