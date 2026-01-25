package com.example.yachtevaluator.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yachtevaluator.R
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.presentation.intent.GameIntent
import com.example.yachtevaluator.presentation.state.EvaluationUiState
import com.example.yachtevaluator.presentation.viewmodel.GameViewModel
import com.example.yachtevaluator.ui.component.DiceRow
import com.example.yachtevaluator.ui.component.EvaluationPanel
import com.example.yachtevaluator.ui.component.ModeTabs
import com.example.yachtevaluator.ui.component.RollButton
import com.example.yachtevaluator.ui.component.RollCountSelector
import com.example.yachtevaluator.ui.component.ScoreTable
import com.example.yachtevaluator.ui.theme.YachtEvaluatorTheme

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gameState = uiState.gameState
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error message
    LaunchedEffect(uiState.evaluationState) {
        if (uiState.evaluationState is EvaluationUiState.Error) {
            snackbarHostState.showSnackbar(
                (uiState.evaluationState as EvaluationUiState.Error).message
            )
        }
    }

    YachtEvaluatorTheme(gameMode = gameState.mode) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Mode tabs
                ModeTabs(
                    currentMode = gameState.mode,
                    onModeChange = { viewModel.onIntent(GameIntent.ChangeMode(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dice actions
                when (gameState.mode) {
                    GameMode.PLAY -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RollButton(
                                rollCount = gameState.rollCount,
                                onClick = { viewModel.onIntent(GameIntent.RollDice) }
                            )
                        }
                    }
                    GameMode.ANALYSIS -> {
                        RollCountSelector(
                            selectedRollCount = gameState.rollCount,
                            onRollCountSelected = { viewModel.onIntent(GameIntent.SetRollCount(it)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { viewModel.onIntent(GameIntent.RequestEvaluation) },
                        enabled = gameState.rollCount != RollCount.ZERO,
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
                        onClick = { viewModel.onIntent(GameIntent.ResetGame) },
                        modifier = Modifier.semantics {
                            contentDescription = "Reset game"
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.reset),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Score table
                ScoreTable(
                    scoreSheet = gameState.scoreSheet,
                    predictedScores = uiState.predictedScores,
                    gameMode = gameState.mode,
                    rollCount = gameState.rollCount,
                    onConfirmScore = { category ->
                        viewModel.onIntent(GameIntent.ConfirmScore(category))
                    },
                    onScoreClick = { category ->
                        // In analysis mode, could open a score edit dialog
                        // For now, this is a placeholder
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Evaluation panel (bottom sheet)
            EvaluationPanel(
                evaluationState = uiState.evaluationState,
                gameMode = gameState.mode,
                onDismiss = { viewModel.onIntent(GameIntent.DismissEvaluation) },
                onApplyRecommendation = { recommendation ->
                    viewModel.onIntent(GameIntent.ApplyRecommendation(recommendation))
                }
            )
        }
    }
}
