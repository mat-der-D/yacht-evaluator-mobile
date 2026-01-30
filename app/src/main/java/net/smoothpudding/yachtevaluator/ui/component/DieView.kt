package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.ui.theme.DiceBackground
import net.smoothpudding.yachtevaluator.ui.theme.DiceBorder
import net.smoothpudding.yachtevaluator.ui.theme.DiceDot
import net.smoothpudding.yachtevaluator.ui.theme.DiceLockedBorder

@Composable
fun DieView(
    value: Int,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gameMode: GameMode = GameMode.PLAY
) {
    val borderColor = if (isLocked && gameMode == GameMode.PLAY) DiceLockedBorder else DiceBorder
    val borderWidth = if (isLocked && gameMode == GameMode.PLAY) 3.dp else 2.dp
    val showLock = isLocked && gameMode == GameMode.PLAY

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(DiceBackground)
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .semantics {
                contentDescription = "Dice showing $value, ${if (isLocked) "locked" else "unlocked"}"
            },
        contentAlignment = Alignment.Center
    ) {
        DiceDots(value = value)

        if (showLock) {
            Text(
                text = "\uD83D\uDD12", // Lock emoji
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
private fun DiceDots(value: Int) {
    val dotSize = 9.5.dp
    val dotSizeOne = 12.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (value) {
            1 -> {
                Dot(modifier = Modifier.size(dotSizeOne), color = Color.Red)
            }
            2 -> {
                Column {
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                }
            }
            3 -> {
                Column {
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                }
            }
            4 -> {
                Column {
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                }
            }
            5 -> {
                Column {
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                }
            }
            6 -> {
                Column {
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Dot(modifier = Modifier.size(dotSize))
                        Spacer(modifier = Modifier.weight(1f))
                        Dot(modifier = Modifier.size(dotSize))
                    }
                }
            }
        }
    }
}

@Composable
private fun Dot(modifier: Modifier = Modifier, color: Color = DiceDot) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
    )
}
