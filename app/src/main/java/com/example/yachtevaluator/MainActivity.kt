package com.example.yachtevaluator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.yachtevaluator.ui.screen.GameScreen
import com.example.yachtevaluator.ui.theme.YachtEvaluatorTheme
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
