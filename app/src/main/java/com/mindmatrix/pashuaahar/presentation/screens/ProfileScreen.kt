package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.Breed
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.UserProfile
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.components.StepContainer
import com.mindmatrix.pashuaahar.presentation.components.VisualBadge
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceWarm
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    cowProfiles: List<CowProfile>,
    selectedCowId: String,
    profile: CowProfile,
    onUserProfileChanged: (UserProfile) -> Unit,
    onCowSelected: (String) -> Unit,
    onCowAdded: () -> Unit,
    onProfileChanged: (CowProfile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        ScreenHeader(
            title = "Profile",
            subtitle = "${userProfile.farmName} · ${cowProfiles.size} cow${if (cowProfiles.size == 1) "" else "s"}"
        )
        Spacer(modifier = Modifier.height(20.dp))

        UserProfileCard(
            userProfile = userProfile,
            onUserProfileChanged = onUserProfileChanged
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Cow profiles", style = MaterialTheme.typography.headlineMedium)
            Button(
                onClick = onCowAdded,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "+ Add cow")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        cowProfiles.forEach { cow ->
            CowProfileRow(
                profile = cow,
                selected = cow.id == selectedCowId,
                onClick = { onCowSelected(cow.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Selected cow", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        CowEditor(
            profile = profile,
            onProfileChanged = onProfileChanged
        )
    }
}

@Composable
private fun UserProfileCard(
    userProfile: UserProfile,
    onUserProfileChanged: (UserProfile) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.25f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Farmer profile", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = userProfile.farmerName,
                onValueChange = { onUserProfileChanged(userProfile.copy(farmerName = it)) },
                label = { Text("Farmer name") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userProfile.farmName,
                onValueChange = { onUserProfileChanged(userProfile.copy(farmName = it)) },
                label = { Text("Farm name") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userProfile.village,
                onValueChange = { onUserProfileChanged(userProfile.copy(village = it)) },
                label = { Text("Village") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userProfile.phoneNumber,
                onValueChange = { onUserProfileChanged(userProfile.copy(phoneNumber = it)) },
                label = { Text("Phone number") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CowProfileRow(
    profile: CowProfile,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) FieldGreen else FieldGreen.copy(alpha = 0.18f)
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${profile.breed.name.replace("_", " ")} · ${profile.weightKg} kg · ${profile.dailyMilkLitres.roundToInt()} L milk",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }
            if (selected) {
                VisualBadge(label = "Active", color = FieldGreen)
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun CowEditor(
    profile: CowProfile,
    onProfileChanged: (CowProfile) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 3

    OutlinedCard(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, LeafGreen.copy(alpha = 0.24f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            when (currentStep) {
                0 -> {
                    StepContainer(
                        title = "Basic Info",
                        subtitle = "Identify your cow",
                        currentStep = 0,
                        totalSteps = totalSteps
                    ) {
                        Column {
                            OutlinedTextField(
                                value = profile.name,
                                onValueChange = { onProfileChanged(profile.copy(name = it)) },
                                label = { Text("Cow name") },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Breed", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Breed.entries.forEach { b ->
                                    androidx.compose.material3.FilterChip(
                                        selected = profile.breed == b,
                                        onClick = { onProfileChanged(profile.copy(breed = b)) },
                                        label = { Text(b.name.replace("_", " ")) },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { currentStep = 1 },
                                modifier = Modifier.align(Alignment.End),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Next Step")
                            }
                        }
                    }
                }

                1 -> {
                    StepContainer(
                        title = "Size & Age",
                        subtitle = "Crucial for nutrition math",
                        currentStep = 1,
                        totalSteps = totalSteps
                    ) {
                        Column {
                            ProfileSlider(
                                title = "Weight",
                                value = profile.weightKg.toFloat(),
                                valueRange = 180f..700f,
                                label = "${profile.weightKg} kg",
                                onValueChanged = { onProfileChanged(profile.copy(weightKg = it.roundToInt())) }
                            )
                            ProfileSlider(
                                title = "Age",
                                value = profile.ageInMonths.toFloat(),
                                valueRange = 1f..180f,
                                label = "${profile.ageInMonths} months",
                                onValueChanged = { onProfileChanged(profile.copy(ageInMonths = it.roundToInt())) }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { currentStep = 0 },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceWarm, contentColor = Color.DarkGray)
                                ) {
                                    Text("Back")
                                }
                                Button(
                                    onClick = { currentStep = 2 },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Next Step")
                                }
                            }
                        }
                    }
                }

                2 -> {
                    StepContainer(
                        title = "Yield & Stage",
                        subtitle = "Help us calculate the recipe",
                        currentStep = 2,
                        totalSteps = totalSteps
                    ) {
                        Column {
                            ProfileSlider(
                                title = "Daily milk",
                                value = profile.dailyMilkLitres,
                                valueRange = 0f..30f,
                                label = "${profile.dailyMilkLitres.roundToInt()} litres",
                                onValueChanged = { onProfileChanged(profile.copy(dailyMilkLitres = it.roundToInt().toFloat())) }
                            )
                            ProfileSlider(
                                title = "Pregnancy month",
                                value = profile.pregnancyMonth.toFloat(),
                                valueRange = 0f..9f,
                                label = if (profile.pregnancyMonth == 0) "Not pregnant" else "${profile.pregnancyMonth} months",
                                onValueChanged = { onProfileChanged(profile.copy(pregnancyMonth = it.roundToInt())) }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { currentStep = 1 },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceWarm, contentColor = Color.DarkGray)
                                ) {
                                    Text("Back")
                                }
                                Button(
                                    onClick = { /* Save / Done is handled by state */ },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = FieldGreen)
                                ) {
                                    Text("Finished")
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
private fun ProfileSlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    label: String,
    onValueChanged: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChanged,
            valueRange = valueRange
        )
    }
}
