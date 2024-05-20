package com.github.jesushzc.pokedex.utils

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
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

/**
 * Clear focus when the keyboard is dismissed
 */
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}

fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect);
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom;
    return keypadHeight > screenHeight * 0.15
}

/**
 * Convert an Int color to a Compose color
 */
fun convertColor(color: Int): Color {
    val red = android.graphics.Color.red(color) / 255f
    val green = android.graphics.Color.green(color) / 255f
    val blue = android.graphics.Color.blue(color) / 255f
    val alpha = android.graphics.Color.alpha(color) / 255f

    return Color(red = red, green = green, blue = blue, alpha = alpha)
}

/**
 * Calculate the luminance of a color
 */
fun calculateLuminance(color: Color): Double {
    val red = if (color.red <= 0.03928) color.red / 12.92 else Math.pow(((color.red + 0.055) / 1.055).toDouble(), 2.4)
    val green = if (color.green <= 0.03928) color.green / 12.92 else Math.pow(((color.green + 0.055) / 1.055).toDouble(), 2.4)
    val blue = if (color.blue <= 0.03928) color.blue / 12.92 else Math.pow(((color.blue + 0.055) / 1.055).toDouble(), 2.4)

    return 0.2126 * red + 0.7152 * green + 0.0722 * blue
}

/**
 * Get a contrasting text color based on the background color
 */
fun getContrastingTextColor(backgroundColor: Int): Color {
    val background = convertColor(backgroundColor)
    val luminance = calculateLuminance(background)
    return if (luminance > 0.5) Color.Black else Color.White
}
