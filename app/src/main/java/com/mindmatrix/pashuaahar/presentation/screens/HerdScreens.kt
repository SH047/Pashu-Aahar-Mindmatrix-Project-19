package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.Breed
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.presentation.PashuAaharUiState
import com.mindmatrix.pashuaahar.presentation.components.CowAvatar
import com.mindmatrix.pashuaahar.presentation.components.EmptyStateWithAnimation
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.components.SuccessAnimation
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.Healthy
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.PulseButton
import com.mindmatrix.pashuaahar.presentation.theme.ShimmerBox
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HerdListScreen(
    state: PashuAaharUiState,
    onCowSelected: (String) -> Unit,
    onDeleteCow: (String) -> Unit,
    onAddCow: () -> Unit
) {
    GradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("My Herd", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = onAddCow) {
                            Icon(Icons.Rounded.Add, "Add Cow")
                        }
                    }
                )
            },
            floatingActionButton = {
                if (state.cowProfiles.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = onAddCow,
                        containerColor = FieldGreen,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Rounded.Add, "Add Cow")
                    }
                }
            }
        ) { paddingValues ->
            SlideInContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                ) {
                    when {
                        state.cowProfiles.isEmpty() -> {
                            EmptyStateWithAnimation(
                                message = "No cows in your herd yet!\nAdd your first one to get started.",
                                onAddClick = onAddCow
                            )
                        }
                        else -> {
                            Column {
                                Text(
                                    text = "${state.cowProfiles.size} profiles ready",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(state.cowProfiles) { cow ->
                                        HerdCowCard(
                                            cow = cow,
                                            selected = cow.id == state.selectedCowId,
                                            onClick = { onCowSelected(cow.id) },
                                            onDelete = { onDeleteCow(cow.id) }
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
}

@Composable
private fun HerdCowCard(
    cow: CowProfile, 
    selected: Boolean, 
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Cow?") },
            text = { Text("Are you sure you want to delete ${cow.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    BeautifulCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.82f),
        onClick = onClick,
        border = BorderStroke(if (selected) 3.dp else 1.dp, if (selected) FieldGreen else FieldGreen.copy(alpha = 0.2f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CowAvatar(
                    name = cow.name,
                    avatarStyle = cow.avatarStyle,
                    selected = selected,
                    modifier = Modifier.size(124.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = cow.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Healthy)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "Healthy", style = MaterialTheme.typography.labelLarge)
                }
            }

            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddCowScreen(
    nextAvatarStyle: Int,
    onSave: (CowProfile) -> Unit,
    onBack: () -> Unit
) {
    var showSuccess by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf(Breed.DESI_GIR) }
    var ageText by remember { mutableStateOf("36") }
    var weight by remember { mutableFloatStateOf(380f) }
    var targetYield by remember { mutableFloatStateOf(12f) }
    var offspringText by remember { mutableStateOf("0") }
    var lactationStage by remember { mutableStateOf("Early") }
    var fmd by remember { mutableStateOf(false) }
    var brucellosis by remember { mutableStateOf(false) }
    var dewormed by remember { mutableStateOf(false) }
    var photoTaken by remember { mutableStateOf(false) }
    var generating by remember { mutableStateOf(false) }

    LaunchedEffect(photoTaken) {
        if (photoTaken) {
            generating = true
            delay(900)
            generating = false
        }
    }

    if (showSuccess) {
        SuccessAnimation(
            visible = true,
            onComplete = {
                showSuccess = false
                onBack()
            }
        )
    }

    GradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Add Cow", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            SlideInContent {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    ScreenHeader(title = "New Profile", subtitle = "Create a visual profile for your cow")
                    Spacer(modifier = Modifier.height(18.dp))
                    AvatarCaptureCard(
                        name = name.ifBlank { "New cow" },
                        avatarStyle = nextAvatarStyle,
                        photoTaken = photoTaken,
                        generating = generating,
                        onTakePhoto = { photoTaken = true }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Rounded.Pets, null) }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Breed", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Breed.entries.forEach { b ->
                            FilterChip(
                                selected = breed == b,
                                onClick = { breed = b },
                                label = { Text(b.name.replace("_", " ")) },
                                shape = RoundedCornerShape(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Weight ${weight.roundToInt()} kg", style = MaterialTheme.typography.titleLarge)
                    Slider(
                        value = weight, 
                        onValueChange = { weight = it }, 
                        valueRange = 160f..720f,
                        colors = SliderDefaults.colors(thumbColor = FieldGreen, activeTrackColor = FieldGreen)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Target Yield ${targetYield.roundToInt()} Liters", style = MaterialTheme.typography.titleLarge)
                    Slider(
                        value = targetYield,
                        onValueChange = { targetYield = it },
                        valueRange = 2f..45f,
                        colors = SliderDefaults.colors(thumbColor = FieldGreen, activeTrackColor = FieldGreen)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it.filter(Char::isDigit).take(3) },
                        label = { Text("Age in months") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Rounded.Tag, null) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = offspringText,
                        onValueChange = { offspringText = it.filter(Char::isDigit).take(2) },
                        label = { Text("Number of Offspring (Calves)") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Lactation period", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("Early", "Mid", "Late", "Dry").forEach { stage ->
                            FilterChip(
                                selected = lactationStage == stage,
                                onClick = { lactationStage = stage },
                                label = { Text(stage) },
                                shape = RoundedCornerShape(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Vaccination status", style = MaterialTheme.typography.titleLarge)
                    VaccinationCheck("FMD", fmd) { fmd = it }
                    VaccinationCheck("Brucellosis", brucellosis) { brucellosis = it }
                    VaccinationCheck("Dewormed", dewormed) { dewormed = it }
                    Spacer(modifier = Modifier.height(18.dp))
                    PulseButton(
                        text = "Save Cow Profile",
                        onClick = {
                            onSave(
                                CowProfile(
                                    id = UUID.randomUUID().toString(),
                                    name = name.ifBlank { "Cow" },
                                    breed = breed,
                                    weightKg = weight.roundToInt(),
                                    ageInMonths = ageText.toIntOrNull() ?: 36,
                                    targetYieldLiters = targetYield,
                                    offspringCount = offspringText.toIntOrNull() ?: 0,
                                    lactationStage = lactationStage,
                                    lactationDay = when (lactationStage) {
                                        "Early" -> 45
                                        "Mid" -> 140
                                        "Late" -> 260
                                        else -> 0
                                    },
                                    avatarStyle = nextAvatarStyle,
                                    fmdVaccinated = fmd,
                                    brucellosisVaccinated = brucellosis,
                                    dewormed = dewormed
                                )
                            )
                            showSuccess = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun AvatarCaptureCard(
    name: String,
    avatarStyle: Int,
    photoTaken: Boolean,
    generating: Boolean,
    onTakePhoto: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Meadow),
        border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.22f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                when {
                    generating -> ShimmerBox(modifier = Modifier.fillMaxSize())
                    photoTaken -> CowAvatar(name = name, avatarStyle = avatarStyle, modifier = Modifier.size(170.dp))
                    else -> Text(text = "Camera", style = MaterialTheme.typography.headlineMedium, color = FieldGreen)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onTakePhoto,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Take Photo for AI Logo")
            }
            AnimatedVisibility(visible = photoTaken && !generating) {
                Text(
                    text = "AI logo ready",
                    style = MaterialTheme.typography.bodyLarge,
                    color = FieldGreen,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun VaccinationCheck(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CowDetailScreen(
    profile: CowProfile,
    bodyCondition: com.mindmatrix.pashuaahar.domain.BodyConditionResult,
    onDelete: (String) -> Unit,
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Cow?") },
            text = { Text("Are you sure you want to permanently remove ${profile.name} from your herd? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(profile.id)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScreenHeader(title = profile.name, subtitle = "Scientific Cow Profile")
                Spacer(modifier = Modifier.height(28.dp))
                
                // AI Logo Header
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        CowAvatar(name = profile.name, avatarStyle = profile.avatarStyle, selected = true, modifier = Modifier.size(180.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Discrete AI Logo", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Scientific Data Card
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Breed", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = profile.breed.name.replace("_", " "), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Target Yield", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = "${profile.targetYieldLiters.roundToInt()} L", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FieldGreen)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Age", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = "${profile.ageInMonths} Months", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Weight", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = "${profile.weightKg} kg", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FieldGreen)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Lactation Stage", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = profile.lactationStage, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Offspring", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = "${profile.offspringCount} Calves", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Body Condition (BCS)", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = "${bodyCondition.score} / 5", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Turmeric)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Health Status", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(text = bodyCondition.band.label, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Healthy)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Vaccination Card
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Vaccination & Health", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        VaccineRow("FMD", profile.fmdVaccinated)
                        VaccineRow("Brucellosis", profile.brucellosisVaccinated)
                        VaccineRow("Deworming", profile.dewormed)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedButton(
                    onClick = onBack, 
                    shape = RoundedCornerShape(22.dp), 
                    modifier = Modifier.fillMaxWidth().height(58.dp)
                ) {
                    Text("Back to Herd")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete Profile")
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun VaccineRow(label: String, isDone: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isDone) Healthy.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (isDone) "✓" else "✕", color = if (isDone) Healthy else Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}
