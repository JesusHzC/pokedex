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

            // Convertimos el color de fondo a un entero para calcular la luminancia
            val backgroundColorInt = backgroundColor.toArgb()

            // Calculando un color de texto contrastante
            val textColor = if (ColorUtils.calculateLuminance(backgroundColorInt) < 0.5) {
                // Si el fondo es oscuro, el texto debería ser claro
                Color.White
            } else {
                // Si el fondo es claro, el texto debería ser oscuro
                Color.Black
            }

            onFinish(backgroundColor, textColor)
        }
    }
}
