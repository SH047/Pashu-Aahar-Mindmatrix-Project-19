package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.AppLanguage
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import com.mindmatrix.pashuaahar.presentation.PashuAaharUiState
import com.mindmatrix.pashuaahar.presentation.components.FarmBackground
import com.mindmatrix.pashuaahar.presentation.components.LivelyCowIllustration
import com.mindmatrix.pashuaahar.presentation.components.StepContainer
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Clay
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.PulseButton
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.Soil
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

@Composable
fun OnboardingScreen(
    state: PashuAaharUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onFarmerLevelSelected: (FarmerLevel) -> Unit,
    onContinue: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 3

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.BottomCenter) {
                    FarmBackground(modifier = Modifier.fillMaxSize())
                    LivelyCowIllustration(modifier = Modifier.padding(bottom = 20.dp))
                }
                
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Pashu-Aahar",
                        style = MaterialTheme.typography.headlineLarge,
                        color = FieldGreen
                    )
                    Text(
                        text = "Visual care for every cow",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    when (currentStep) {
                        0 -> {
                            StepContainer(
                                title = "Choose language",
                                subtitle = "Select your preferred language to continue",
                                currentStep = 0,
                                totalSteps = totalSteps
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    LanguageCard(
                                        title = "English",
                                        marker = "EN",
                                        accent = Sky,
                                        selected = state.selectedLanguage == AppLanguage.English,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            onLanguageSelected(AppLanguage.English)
                                            currentStep = 1
                                        }
                                    )
                                    LanguageCard(
                                        title = "ಕನ್ನಡ",
                                        marker = "ಕ",
                                        accent = Turmeric,
                                        selected = state.selectedLanguage == AppLanguage.Kannada,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            onLanguageSelected(AppLanguage.Kannada)
                                            currentStep = 1
                                        }
                                    )
                                }
                            }
                        }

                        1 -> {
                            StepContainer(
                                title = "Farm size",
                                subtitle = "How many cows do you have in your farm?",
                                currentStep = 1,
                                totalSteps = totalSteps
                            ) {
                                Column {
                                    FarmerLevel.entries.forEach { level ->
                                        FarmSizeCard(
                                            level = level,
                                            selected = state.selectedFarmerLevel == level,
                                            onClick = {
                                                onFarmerLevelSelected(level)
                                                currentStep = 2
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }

                        2 -> {
                            StepContainer(
                                title = "Our Mission",
                                subtitle = "Join the dairy prosperity revolution",
                                currentStep = 2,
                                totalSteps = totalSteps
                            ) {
                                Column {
                                    BeautifulCard(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                text = "Stop spending on expensive branded feed.",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = FieldGreen,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "We help you make Scientific Balanced Feed at home using local grains like Maize to maximize milk yield and increase your daily profit.",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(32.dp))
                                    PulseButton(
                                        text = "Get Started",
                                        onClick = onContinue,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "By clicking Get Started, you agree to our terms of service.",
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageCard(
    title: String,
    marker: String,
    accent: Color,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    BeautifulCard(
        modifier = modifier
            .height(170.dp),
        onClick = onClick,
        border = BorderStroke(if (selected) 3.dp else 1.dp, accent.copy(alpha = if (selected) 0.95f else 0.28f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = marker, style = MaterialTheme.typography.headlineLarge, color = accent, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FarmSizeCard(
    level: FarmerLevel,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accent = when (level) {
        FarmerLevel.Beginner -> LeafGreen
        FarmerLevel.Intermediate -> Soil
        FarmerLevel.LargeScale -> FieldGreen
    }

    BeautifulCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp),
        onClick = onClick,
        border = BorderStroke(if (selected) 3.dp else 1.dp, accent.copy(alpha = if (selected) 0.9f else 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FarmVisual(level = level, accent = accent)
            Spacer(modifier = Modifier.size(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = level.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = level.herdRange, style = MaterialTheme.typography.titleLarge, color = accent)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = level.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                )
            }
        }
    }
}

@Composable
private fun FarmVisual(level: FarmerLevel, accent: Color) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(when (level) {
                FarmerLevel.Beginner -> Meadow
                FarmerLevel.Intermediate -> SurfaceMuted
                FarmerLevel.LargeScale -> FieldGreen.copy(alpha = 0.12f)
            }),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(96.dp)) {
            when (level) {
                FarmerLevel.Beginner -> {
                    drawOval(accent.copy(alpha = 0.28f), topLeft = Offset(size.width * 0.18f, size.height * 0.42f), size = androidx.compose.ui.geometry.Size(size.width * 0.64f, size.height * 0.34f))
                    drawCircle(accent, size.minDimension * 0.08f, Offset(size.width * 0.42f, size.height * 0.56f))
                    drawCircle(accent, size.minDimension * 0.08f, Offset(size.width * 0.6f, size.height * 0.56f))
                }
                FarmerLevel.Intermediate -> {
                    drawRoundRect(Clay.copy(alpha = 0.32f), topLeft = Offset(size.width * 0.15f, size.height * 0.38f), size = androidx.compose.ui.geometry.Size(size.width * 0.7f, size.height * 0.42f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
                    drawLine(accent, Offset(size.width * 0.12f, size.height * 0.4f), Offset(size.width * 0.5f, size.height * 0.16f), strokeWidth = 5.dp.toPx())
                    drawLine(accent, Offset(size.width * 0.5f, size.height * 0.16f), Offset(size.width * 0.88f, size.height * 0.4f), strokeWidth = 5.dp.toPx())
                }
                FarmerLevel.LargeScale -> {
                    repeat(3) { index ->
                        val top = size.height * (0.2f + index * 0.18f)
                        drawRoundRect(accent.copy(alpha = 0.25f), topLeft = Offset(size.width * 0.16f, top), size = androidx.compose.ui.geometry.Size(size.width * 0.68f, size.height * 0.12f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
                    }
                    drawCircle(Turmeric.copy(alpha = 0.7f), size.minDimension * 0.12f, Offset(size.width * 0.78f, size.height * 0.22f))
                }
            }
        }
    }
}
