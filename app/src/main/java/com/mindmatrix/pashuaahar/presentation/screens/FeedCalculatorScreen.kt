package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.domain.FeedLine
import com.mindmatrix.pashuaahar.domain.FeedPlan
import com.mindmatrix.pashuaahar.presentation.components.GrainMixAnimation
import com.mindmatrix.pashuaahar.presentation.components.InfoCard
import com.mindmatrix.pashuaahar.presentation.components.SavingsBarChart
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.components.VisualBadge
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceWarm
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

@Composable
fun FeedCalculatorScreen(
    feedPlan: FeedPlan,
    profile: CowProfile,
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    todayKey: String,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    onCowSelected: (String) -> Unit,
    onDailyCareToggled: (String, DailyCareActivity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        ScreenHeader(
            title = "Feed tracker",
            subtitle = "Tap cow icons to mark today's care: $todayKey"
        )
        Spacer(modifier = Modifier.height(18.dp))

        DailyCareBoard(
            cowProfiles = cowProfiles,
            selectedCowId = selectedCowId,
            dailyCareStatus = dailyCareStatus,
            onCowSelected = onCowSelected,
            onDailyCareToggled = onDailyCareToggled
        )

        Spacer(modifier = Modifier.height(20.dp))
        ScreenHeader(
            title = "${profile.name}'s ration",
            subtitle = "Daily bucket plan using local ingredients."
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            GrainMixAnimation()
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(
                title = "Total",
                value = "${feedPlan.totalKg} kg",
                detail = "Daily feed mix",
                color = FieldGreen,
                modifier = Modifier.weight(1f)
            )
            InfoCard(
                title = "Cost",
                value = "Rs ${feedPlan.totalCost}",
                detail = "Per day estimate",
                color = Turmeric,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        feedPlan.lines.forEach { line ->
            FeedLineCard(line = line, totalKg = feedPlan.totalKg)
            Spacer(modifier = Modifier.height(12.dp))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        OutlinedCard(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = FieldGreen.copy(alpha = 0.03f)),
            border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Daily Prosperity Savings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FieldGreen
                )
                Spacer(modifier = Modifier.height(16.dp))
                SavingsBarChart(
                    marketCost = feedPlan.commercialCost,
                    homeCost = feedPlan.totalCost,
                    savings = feedPlan.dailySavings
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "💡 Advice: ${feedPlan.guidance}", style = MaterialTheme.typography.bodyLarge, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
    }
}

@Composable
private fun DailyCareBoard(
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    onCowSelected: (String) -> Unit,
    onDailyCareToggled: (String, DailyCareActivity) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.22f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Daily calendar", style = MaterialTheme.typography.headlineMedium)
                VisualBadge(
                    label = "${completedCareCount(dailyCareStatus)} / ${cowProfiles.size * DailyCareActivity.entries.size}",
                    color = FieldGreen
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Select a cow, then tap each circle when that activity is done.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            CowSelectionRow(
                cowProfiles = cowProfiles,
                selectedCowId = selectedCowId,
                dailyCareStatus = dailyCareStatus,
                onCowSelected = onCowSelected
            )

            Spacer(modifier = Modifier.height(16.dp))
            DailyCareActivity.entries.forEach { activity ->
                ActivityRow(
                    activity = activity,
                    cowProfiles = cowProfiles,
                    dailyCareStatus = dailyCareStatus,
                    onDailyCareToggled = onDailyCareToggled
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun CowSelectionRow(
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    onCowSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cowProfiles.forEach { cow ->
            val selected = cow.id == selectedCowId
            val completeCount = dailyCareStatus[cow.id].orEmpty().size
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCowSelected(cow.id) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CowIcon(
                    name = cow.name,
                    selected = selected,
                    completed = completeCount == DailyCareActivity.entries.size
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = cow.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = "$completeCount/${DailyCareActivity.entries.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f)
                )
            }
        }
    }
}

@Composable
private fun ActivityRow(
    activity: DailyCareActivity,
    cowProfiles: List<CowProfile>,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    onDailyCareToggled: (String, DailyCareActivity) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = activity.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "${activityCompletedCount(activity, dailyCareStatus)} / ${cowProfiles.size}",
                style = MaterialTheme.typography.bodyLarge,
                color = FieldGreen
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            cowProfiles.forEach { cow ->
                val completed = activity in dailyCareStatus[cow.id].orEmpty()
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CowActivityButton(
                        cow = cow,
                        completed = completed,
                        onClick = { onDailyCareToggled(cow.id, activity) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CowActivityButton(
    cow: CowProfile,
    completed: Boolean,
    onClick: () -> Unit
) {
    val color = if (completed) FieldGreen else SurfaceMuted
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (completed) FieldGreen.copy(alpha = 0.18f) else Color.White)
            .border(2.dp, color, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (completed) "✓" else cow.name.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = if (completed) FieldGreen else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CowIcon(
    name: String,
    selected: Boolean,
    completed: Boolean
) {
    val color = when {
        completed -> FieldGreen
        selected -> Sky
        else -> SurfaceMuted
    }
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.16f))
            .border(2.dp, color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = if (completed) FieldGreen else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun completedCareCount(dailyCareStatus: Map<String, Set<DailyCareActivity>>): Int =
    dailyCareStatus.values.sumOf { it.size }

private fun activityCompletedCount(
    activity: DailyCareActivity,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>
): Int = dailyCareStatus.values.count { activity in it }

@Composable
private fun FeedLineCard(line: FeedLine, totalKg: Float) {
    val progress = if (totalKg == 0f) 0f else line.quantityKg / totalKg
    val color = when (line.ingredient.category) {
        "Roughage" -> LeafGreen
        "Protein" -> Sky
        else -> Turmeric
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = line.ingredient.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = line.ingredient.visualHint, style = MaterialTheme.typography.bodyLarge)
                }
                Text(text = "${line.quantityKg} kg", style = MaterialTheme.typography.headlineMedium, color = color)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = color,
                trackColor = SurfaceMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${line.ingredient.crudeProteinPercentage}% protein, Rs ${line.cost}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
    }
}
