package com.example.yachtevaluator.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.domain.model.RollCount

@Composable
fun DiceRow(
    dice: List<Int>,
    lockedDice: List<Boolean>,
    gameMode: GameMode,
    rollCount: RollCount,
    onDiceClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        dice.forEachIndexed { index, value ->
            DieView(
                value = value,
                isLocked = lockedDice[index],
                onClick = { onDiceClick(index) },
                enabled = when (gameMode) {
                    GameMode.PLAY -> rollCount != RollCount.ZERO
                    GameMode.ANALYSIS -> true
                },
                gameMode = gameMode,
                modifier = Modifier.width(56.dp)
            )
        }
    }
}
