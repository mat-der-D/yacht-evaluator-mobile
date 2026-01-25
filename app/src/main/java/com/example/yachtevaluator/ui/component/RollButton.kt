package com.example.yachtevaluator.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.yachtevaluator.R
import com.example.yachtevaluator.domain.model.RollCount

@Composable
fun RollButton(
    rollCount: RollCount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingRolls = rollCount.remaining()
    val isEnabled = rollCount.canRoll()

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .height(48.dp)
            .semantics {
                contentDescription = "Roll dice, $remainingRolls rolls remaining"
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.roll_dice),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.rolls_left, remainingRolls),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
