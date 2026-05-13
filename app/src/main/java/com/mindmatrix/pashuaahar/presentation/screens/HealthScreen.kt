package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import com.mindmatrix.pashuaahar.data.SeedData
import com.mindmatrix.pashuaahar.domain.BodyConditionResult
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.domain.VaccinationItem
import com.mindmatrix.pashuaahar.presentation.components.HealthMeter
import com.mindmatrix.pashuaahar.presentation.components.PregnancyProgress
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.components.VisualBadge
import com.mindmatrix.pashuaahar.presentation.theme.Clay
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceWarm
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

@Composable
fun HealthScreen(
    profile: CowProfile,
    bodyCondition: BodyConditionResult,
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    cowBodyConditions: Map<String, BodyConditionResult>,
    onCowSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        ScreenHeader(
            title = "Health tracker",
            subtitle = "${profile.name}'s body condition, pregnancy and vaccination reminders."
        )
        Spacer(modifier = Modifier.height(18.dp))

        CowHealthBoard(
            cowProfiles = cowProfiles,
            selectedCowId = selectedCowId,
            dailyCareStatus = dailyCareStatus,
            cowBodyConditions = cowBodyConditions,
            onCowSelected = onCowSelected
        )

        Spacer(modifier = Modifier.height(24.dp))
        HealthMeter(score = bodyCondition.score, label = bodyCondition.band.label)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = bodyCondition.message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        PregnancyProgress(month = profile.pregnancyMonth)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Vaccination reminders", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        SeedData.vaccinations.forEach { item ->
            VaccinationCard(item = item)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CowHealthBoard(
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    dailyCareStatus: Map<String, Set<DailyCareActivity>>,
    cowBodyConditions: Map<String, BodyConditionResult>,
    onCowSelected: (String) -> Unit
) {
    Column {
        Text(text = "Cow BCS readings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        cowProfiles.forEach { cow ->
            CowHealthCard(
                profile = cow,
                selected = cow.id == selectedCowId,
                completedCare = dailyCareStatus[cow.id].orEmpty().size,
                bodyCondition = cowBodyConditions[cow.id],
                onClick = { onCowSelected(cow.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CowHealthCard(
    profile: CowProfile,
    selected: Boolean,
    completedCare: Int,
    bodyCondition: BodyConditionResult?,
    onClick: () -> Unit
) {
    val score = bodyCondition?.score ?: 0f
    val band = bodyCondition?.band?.label ?: "Not calculated"
    val accent = if (selected) FieldGreen else FieldGreen.copy(alpha = 0.22f)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, accent),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(FieldGreen.copy(alpha = if (selected) 0.2f else 0.1f))
                        .border(2.dp, accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FieldGreen
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = profile.name, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "$completedCare/${DailyCareActivity.entries.size} care done today",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "BCS $score",
                    style = MaterialTheme.typography.headlineMedium,
                    color = FieldGreen
                )
                Text(text = band, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun VaccinationCard(item: VaccinationItem) {
    val color = if (item.priority == "High") Clay else Turmeric
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            VisualBadge(label = item.priority, color = color)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.dueWindow,
                style = MaterialTheme.typography.headlineMedium,
                color = FieldGreen
            )
            Text(text = item.note, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
