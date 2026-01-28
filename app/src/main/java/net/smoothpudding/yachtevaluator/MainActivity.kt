package net.smoothpudding.yachtevaluator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.smoothpudding.yachtevaluator.ui.screen.GameScreen
import net.smoothpudding.yachtevaluator.ui.theme.YachtEvaluatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YachtEvaluatorTheme {
                GameScreen()
            }
        }
    }
}
