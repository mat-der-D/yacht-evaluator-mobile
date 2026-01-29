# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android mobile app for the Yacht dice game (similar to Yahtzee). The app provides two modes:
- **Play Mode**: Roll dice and play the game with AI-powered optimal move suggestions
- **Analysis Mode**: Manually set up board states and analyze optimal plays

The app communicates with an external API (https://yacht-evaluator-api-1092304578340.asia-northeast1.run.app) to evaluate game states and provide recommendations.

## Build & Development

This project is built via **Android Studio**. The Gradle wrapper (`gradlew`) is not included in the repository.

- **Build debug APK**: Build > Build Bundle(s) / APK(s) > Build APK(s)
- **Build release APK/AAB**: Build > Generate Signed Bundle / APK...
- **Run lint**: Analyze > Inspect Code...

**Note**: Test infrastructure does not exist yet.

## Architecture

### High-Level Structure

The app follows a clean architecture pattern with clear separation of concerns:

**Presentation Layer** (`presentation/`):
- **MVI (Model-View-Intent)** pattern for state management
- `GameViewModel`: Central state holder that processes `GameIntent` actions
- `GameReducer`: Pure function that reduces `GameState` + `GameIntent` → new `GameState`
- `GameUiState`: Immutable UI state containing `gameState`, `evaluationState`, and `predictedScores`
- `EvaluationUiState`: Sealed class representing API evaluation states (Idle/Loading/Success/Error)

**Domain Layer** (`domain/`):
- `ScoreCalculator`: Calculates scores for dice combinations locally
- `ScoreValidator`: Validates game state and score entries
- Models: `GameState`, `Category`, `ScoreSheet`, `GameMode`, `RollCount`

Key scoring rules in `ScoreCalculator`:
- **Full House**: No dice appears exactly once (allows 5-of-a-kind or 3+2, but not 2+2+1)
- **Small Straight**: 4 consecutive numbers (15 points)
- **Big Straight**: 5 consecutive numbers (30 points)
- **Yacht**: All 5 dice same value (50 points)
- **Upper Bonus**: 35 points if upper section total ≥ 63

**Data Layer** (`data/`):
- `EvaluationRepository`: Handles API calls and error handling
- `EvaluationApi`: Retrofit interface for external API
- `ErrorHandler`: Centralizes error message generation from exceptions
- DTOs: `EvaluateRequest`, `EvaluateResponse`

**UI Layer** (`ui/`):
- `GameScreen`: Main screen composable
- Components: `DiceRow`, `ScoreTable`, `EvaluationPanel`, `ModeTabs`, `RollButton`, etc.
- Material 3 with dynamic theming based on `GameMode` (Play = blue theme, Analysis = green theme)

### Dependency Injection

Uses **Hilt** for DI:
- `AppModule`: Provides Retrofit, OkHttp, Moshi, and ErrorHandler
- `RepositoryModule`: Provides EvaluationRepository
- `@HiltAndroidApp` on `YachtEvaluatorApp`
- `@HiltViewModel` on `GameViewModel`

### Key Data Flow

1. User action → `GameIntent` dispatched to `GameViewModel.onIntent()`
2. `GameReducer.reduce()` computes new `GameState`
3. ViewModel updates `StateFlow<GameUiState>`
4. For evaluation requests: Repository calls API, result mapped to `EvaluationUiState`
5. UI observes state via `collectAsState()` and recomposes

### State Management Details

The `GameState` contains:
- `dice: List<Int>` - Current dice values (1-6), always exactly 5 elements
- `lockedDice: List<Boolean>` - Parallel list indicating which dice are locked (same length as dice)
- `scoreSheet: ScoreSheet` - Filled categories and scores
- `rollCount: RollCount` - ZERO, ONE, TWO, or THREE
- `mode: GameMode` - PLAY or ANALYSIS

State updates are handled differently based on mode:
- **Play Mode**: Dice are rolled randomly, locked dice remain unchanged
- **Analysis Mode**: Dice are set manually by tapping (cycles 1→2→3→4→5→6→1), no rolling

**GameIntent Types**: `RollDice`, `ToggleDiceLock`, `ApplyDiceLock`, `IncrementDice`, `SetRollCount`, `UpdateScore`, `ConfirmScore`, `ChangeMode`, `ResetGame`, `RequestEvaluation`, `ApplyRecommendation`, `DismissEvaluation`

### API Integration

The external API endpoint `/api/v1/evaluate` accepts:
- Current `scoreSheet` state (with explicit `null` for unfilled categories)
- `dice` values
- `rollCount`

Returns recommendations with expected values for:
- Which categories to fill immediately
- Which dice to keep if rolling again

**Note**: `EvaluationRepositoryImpl` uses manual JSON construction with `org.json.JSONObject` instead of Moshi serialization. This is intentional to ensure null values are serialized explicitly as `JSONObject.NULL` rather than being omitted.

## Important Notes

### Keystore Management
The release build requires a `keystore.properties` file (not in version control) with:
```
storeFile=<path-to-keystore>
storePassword=<password>
keyAlias=<alias>
keyPassword=<password>
```

See `GOOGLE_PLAY_PUBLISH.md` for complete Google Play publishing instructions.

### Internationalization
The app supports Japanese and English:
- Strings: `app/src/main/res/values/strings.xml` (English)
- Strings: `app/src/main/res/values-ja/strings.xml` (Japanese)

### Network Configuration
The API base URL is hardcoded in `AppModule.kt`. Timeouts are set to 30 seconds for connect/read/write operations.

### Theme System
The app uses Material 3 with custom color schemes that change based on game mode (blue for Play, green for Analysis). See `ui/theme/Theme.kt`.
