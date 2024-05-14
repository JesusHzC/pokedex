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
