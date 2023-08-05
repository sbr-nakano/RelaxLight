package jp.stenhousebayroad.relaxlight

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.stenhousebayroad.relaxlight.ui.theme.RelaxLightTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    val strength: MutableState<Float> = mutableFloatStateOf(0.4f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelaxLightTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    CandleRoom(strength)
                }
            }
        }
    }
}

@Composable
fun CandleRoom(strength: MutableState<Float>) {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.apply {
        isStatusBarVisible = false
        isNavigationBarVisible = false
        isSystemBarsVisible = false
    }

    LaunchedEffect(strength){
        while (true) {
            strength.value = oneOverFrequency(strength.value)
            delay(1000 / 12)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0.1f, strength.value * 0.1f, 0.0f, 1f)),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Candle(
            600.sp,
            Color(1.0f, strength.value * 0.8f, 0.0f, strength.value),
            strength.value * 400
        )
    }
}

@Composable
fun Candle(fontSize: TextUnit,color: Color, blurRadius: Float) {
    Text(
        text = "\uD835\uDD4F",
        color = color,
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = fontSize,
            shadow = Shadow(
                color = color,
                offset = Offset(0f, 0f),
                blurRadius = blurRadius
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val strength = remember{ mutableFloatStateOf(0.4f) }

    RelaxLightTheme {
        CandleRoom(strength)
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