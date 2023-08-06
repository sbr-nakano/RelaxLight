package jp.stenhousebayroad.relaxlight.candle

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

sealed class LightAlgorithm {
    object WordLight:LightAlgorithm()
    object StretchBox:LightAlgorithm()
}

@Composable
fun Light(algorithm: LightAlgorithm) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.apply {
        isStatusBarVisible = false
        isNavigationBarVisible = false
        isSystemBarsVisible = false
    }

    when(algorithm) {
        is LightAlgorithm.WordLight -> WordLight("\uD835\uDD4F")
        is LightAlgorithm.StretchBox -> TODO()
        else -> {}
    }
}

@Composable
fun WordLight(word: String) {
    val strength: MutableState<Float> = remember { mutableFloatStateOf(0.4f) }

    LaunchedEffect(strength){
        while (true) {
            strength.value = oneOverFrequency(strength.value)
            delay(1000 / 12)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0.25f, (1 - strength.value) * 0.2f, 0.0f, 1f)),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = word,
            color = Color(1.0f, strength.value * 0.8f, 0.0f, strength.value),
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 600.sp,
                shadow = Shadow(
                    color = Color(1.0f, strength.value * 0.8f, 0.0f, strength.value),
                    offset = Offset(0f, 0f),
                    blurRadius = strength.value * 400
                )
            )
        )
    }
}

fun oneOverFrequency(value: Float): Float {
    Log.d("oneOverFrequency()", "value: $value")
    val x = if (value < 0.5) {
        value + 2 * value * value
    } else {
        value - 2 * (1 - value) * (1 - value)
    }

    return if (x < 0.05 || x > 0.95) {
        Math.random().toFloat() * 0.8f + 0.1f
    } else x
}