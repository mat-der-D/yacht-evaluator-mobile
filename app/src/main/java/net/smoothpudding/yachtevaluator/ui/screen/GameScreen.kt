package net.smoothpudding.yachtevaluator.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.domain.model.RollCount
import net.smoothpudding.yachtevaluator.presentation.intent.GameIntent
import net.smoothpudding.yachtevaluator.presentation.state.EvaluationUiState
import net.smoothpudding.yachtevaluator.presentation.viewmodel.GameViewModel
import net.smoothpudding.yachtevaluator.BuildConfig
import net.smoothpudding.yachtevaluator.ui.component.DiceRow
import net.smoothpudding.yachtevaluator.ui.component.EvaluationPanel
import net.smoothpudding.yachtevaluator.ui.component.GameHeader
import net.smoothpudding.yachtevaluator.ui.component.ModeTabs
import net.smoothpudding.yachtevaluator.ui.component.RollButton
import net.smoothpudding.yachtevaluator.ui.component.RollCountSelector
import net.smoothpudding.yachtevaluator.ui.component.ScoreTable
import net.smoothpudding.yachtevaluator.ui.theme.LocalModeColors
import net.smoothpudding.yachtevaluator.ui.theme.YachtEvaluatorTheme

private val LowerRegionHeight = 180.dp
private const val SCORE_TABLE_ROW_COUNT = 15
private val DividerTotalHeight = 16.dp

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gameState = uiState.gameState
    val snackbarHostState = remember { SnackbarHostState() }
    var showResetDialog by remember { mutableStateOf(false) }

    // Show error message
    LaunchedEffect(uiState.evaluationState) {
        if (uiState.evaluationState is EvaluationUiState.Error) {
            snackbarHostState.showSnackbar(
                (uiState.evaluationState as EvaluationUiState.Error).message
            )
        }
    }

    YachtEvaluatorTheme(gameMode = gameState.mode) {
        val modeColors = LocalModeColors.current

        // Outer Box to allow overlay
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content with Scaffold
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    ModeTabs(
                        currentMode = gameState.mode,
                        onModeChange = { viewModel.onIntent(GameIntent.ChangeMode(it)) }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Fixed header
                    GameHeader(currentScore = gameState.scoreSheet.finalTotal)

                    // Scrollable content
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (gameState.mode) {
                            GameMode.SETTINGS -> {
                                // Settings screen with version info and settings
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(16.dp)
                                ) {
                                    // Settings section
                                    Text(
                                        text = stringResource(R.string.mode_settings),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )

                                    // Compact mode setting
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stringResource(R.string.settings_compact_mode),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = stringResource(R.string.settings_compact_mode_description),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Switch(
                                            checked = uiState.isCompactMode,
                                            onCheckedChange = { enabled ->
                                                viewModel.onIntent(GameIntent.SetCompactMode(enabled))
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    // Version info at the bottom
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = stringResource(R.string.app_name),
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = stringResource(R.string.version_format, BuildConfig.VERSION_NAME),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            GameMode.PLAY, GameMode.ANALYSIS -> {
                        // Calculate compact row height dynamically
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val compactRowHeight: Dp? = if (uiState.isCompactMode) {
                                val availableHeightForTable = maxHeight - LowerRegionHeight - DividerTotalHeight
                                availableHeightForTable / SCORE_TABLE_ROW_COUNT
                            } else null

                        // Upper scrollable region
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Score table
                            ScoreTable(
                            scoreSheet = gameState.scoreSheet,
                            predictedScores = uiState.predictedScores,
                            gameMode = gameState.mode,
                            rollCount = gameState.rollCount,
                            refreshTrigger = uiState.refreshTrigger,
                            onConfirmScore = { category ->
                                viewModel.onIntent(GameIntent.ConfirmScore(category))
                            },
                            onScoreClick = { category ->
                                // In analysis mode, could open a score edit dialog
                                // For now, this is a placeholder
                            },
                            onScoreUpdate = { category, value ->
                                viewModel.onIntent(GameIntent.UpdateScore(category, value))
                            },
                            isCompactMode = uiState.isCompactMode,
                            compactRowHeight = compactRowHeight
                        )

                            // Spacer to allow scrolling past lower region
                            Spacer(modifier = Modifier.height(LowerRegionHeight))
                        }

                        // Gray divider
                        HorizontalDivider(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = LowerRegionHeight),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        // Lower fixed region
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .height(LowerRegionHeight)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom)
                        ) {
                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            Button(
                                onClick = { viewModel.onIntent(GameIntent.RequestEvaluation) },
                                enabled = gameState.rollCount != RollCount.ZERO && !gameState.scoreSheet.isComplete,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.semantics {
                                    contentDescription = "Evaluate current game state"
                                }
                            ) {
                                Text(
                                    text = "\uD83D\uDCCA ${stringResource(R.string.evaluate)}",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            OutlinedButton(
                                onClick = { showResetDialog = true },
                                modifier = Modifier.semantics {
                                    contentDescription = "Reset game"
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.reset),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        // Dice actions
                        when (gameState.mode) {
                            GameMode.PLAY -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RollButton(
                                        rollCount = gameState.rollCount,
                                        isGameComplete = gameState.scoreSheet.isComplete,
                                        onClick = { viewModel.onIntent(GameIntent.RollDice) }
                                    )
                                }
                            }
                            GameMode.ANALYSIS -> {
                                RollCountSelector(
                                    selectedRollCount = gameState.rollCount,
                                    isGameComplete = gameState.scoreSheet.isComplete,
                                    onRollCountSelected = { viewModel.onIntent(GameIntent.SetRollCount(it)) }
                                )
                            }
                            GameMode.SETTINGS -> {
                                // No dice actions in settings mode
                            }
                        }

                        // Dice section
                        DiceRow(
                            dice = gameState.dice,
                            lockedDice = gameState.lockedDice,
                            gameMode = gameState.mode,
                            rollCount = gameState.rollCount,
                            onDiceClick = { index ->
                                when (gameState.mode) {
                                    GameMode.PLAY -> viewModel.onIntent(GameIntent.ToggleDiceLock(index))
                                    GameMode.ANALYSIS -> viewModel.onIntent(GameIntent.IncrementDice(index))
                                    GameMode.SETTINGS -> {} // No dice interaction in settings mode
                                }
                            }
                        )
                    }
                        } // BoxWithConstraints
                            }
                        }
                    }
                }

                // Reset confirmation dialog
                if (showResetDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetDialog = false },
                        title = {
                            Text(
                                text = stringResource(R.string.reset_confirmation_title),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.reset_confirmation_message),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onIntent(GameIntent.ResetGame)
                                    showResetDialog = false
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.reset),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showResetDialog = false }
                            ) {
                                Text(text = stringResource(R.string.cancel))
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    )
                }
            }

            // Overlay: Evaluation panel (renders on top of all content)
            EvaluationPanel(
                evaluationState = uiState.evaluationState,
                gameMode = gameState.mode,
                currentScore = gameState.scoreSheet.finalTotal,
                onDismiss = { viewModel.onIntent(GameIntent.DismissEvaluation) },
                onApplyRecommendation = { recommendation ->
                    viewModel.onIntent(GameIntent.ApplyRecommendation(recommendation))
                }
            )
        }
    }
}
