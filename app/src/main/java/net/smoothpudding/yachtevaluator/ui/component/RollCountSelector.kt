package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.model.RollCount

@Composable
fun RollCountSelector(
    selectedRollCount: RollCount,
    isGameComplete: Boolean,
    onRollCountSelected: (RollCount) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .selectableGroup()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RollCount.entries.forEach { rollCount ->
            val rollCountText = stringResource(
                when (rollCount) {
                    RollCount.ZERO -> R.string.roll_count_0
                    RollCount.ONE -> R.string.roll_count_1
                    RollCount.TWO -> R.string.roll_count_2
                    RollCount.THREE -> R.string.roll_count_3
                }
            )

            Row(
                modifier = Modifier
                    .selectable(
                        selected = selectedRollCount == rollCount,
                        onClick = { if (!isGameComplete) onRollCountSelected(rollCount) },
                        enabled = !isGameComplete,
                        role = Role.RadioButton
                    )
                    .semantics {
                        contentDescription = "Roll count ${rollCount.value}, ${3 - rollCount.value} remaining"
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRollCount == rollCount,
                    onClick = null,
                    enabled = !isGameComplete,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.outline
                    )
                )
                Text(
                    text = rollCountText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedRollCount == rollCount) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
