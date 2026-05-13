package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.presentation.PashuAaharUiState
import com.mindmatrix.pashuaahar.presentation.components.CowAvatar
import com.mindmatrix.pashuaahar.presentation.components.HealthMeter
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Clay
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.Soil
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

private data class ResourceCard(
    val title: String,
    val color: Color,
    val kind: ResourceKind
)

private enum class ResourceKind {
    Vaccine,
    Scheme,
    Feed,
    Video
}

private val resources = listOf(
    ResourceCard("Vaccination schedule", Turmeric, ResourceKind.Vaccine),
    ResourceCard("Pashu Kisan Credit Card", FieldGreen, ResourceKind.Scheme),
    ResourceCard("VIDEO: Hygiene & Sanitation", Sky, ResourceKind.Video),
    ResourceCard("VIDEO: Fodder Storage Tips", Clay, ResourceKind.Video),
    ResourceCard("Clean water routine", Sky, ResourceKind.Feed),
    ResourceCard("Deworming reminder", Clay, ResourceKind.Vaccine)
)

@Composable
fun HealthTipsScreen(
    state: PashuAaharUiState,
    onCowSelected: (String) -> Unit,
    onBcsSelected: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                ScreenHeader(title = "Health & Tips", subtitle = state.cowProfile.name)
                Spacer(modifier = Modifier.height(14.dp))
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = FieldGreen,
                    modifier = Modifier.clip(RoundedCornerShape(22.dp))
                ) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Health & BCS") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Schemes & Tips") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (selectedTab == 0) {
                    HealthBcsTab(state = state, onCowSelected = onCowSelected, onBcsSelected = onBcsSelected)
                } else {
                    SchemesTipsTab()
                }
            }
        }
    }
}

@Composable
private fun HealthBcsTab(
    state: PashuAaharUiState,
    onCowSelected: (String) -> Unit,
    onBcsSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.cowProfiles) { cow ->
                CowAvatar(
                    name = cow.name,
                    avatarStyle = cow.avatarStyle,
                    selected = cow.id == state.selectedCowId,
                    modifier = Modifier
                        .size(78.dp)
                        .clickable { onCowSelected(cow.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        HealthMeter(score = state.bodyCondition.score, label = state.bodyCondition.band.label)
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Tap matching body shape", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            (1..5).forEach { score ->
                BcsSilhouette(
                    score = score,
                    selected = state.cowProfile.bcsScore == score,
                    onClick = { onBcsSelected(score) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        BeautifulCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = state.bodyCondition.message,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun BcsSilhouette(
    score: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = when (score) {
        1, 2 -> Clay
        3 -> FieldGreen
        else -> Soil
    }
    BeautifulCard(
        modifier = modifier
            .height(126.dp),
        onClick = onClick,
        border = BorderStroke(if (selected) 3.dp else 1.dp, color.copy(alpha = if (selected) 0.95f else 0.24f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CowShape(score = score, color = color)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CowShape(score: Int, color: Color) {
    Canvas(modifier = Modifier.size(54.dp)) {
        val bodyHeight = size.height * (0.24f + score * 0.035f)
        val bodyWidth = size.width * (0.46f + score * 0.045f)
        drawOval(
            color = color.copy(alpha = 0.26f),
            topLeft = Offset((size.width - bodyWidth) / 2f, size.height * 0.42f),
            size = Size(bodyWidth, bodyHeight)
        )
        drawCircle(color, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.4f, size.height * 0.54f))
        drawCircle(color, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.58f, size.height * 0.54f))
        drawLine(color, Offset(size.width * 0.33f, size.height * 0.43f), Offset(size.width * 0.2f, size.height * 0.25f), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.67f, size.height * 0.43f), Offset(size.width * 0.8f, size.height * 0.25f), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun SchemesTipsTab() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        items(resources) { item ->
            ResourceVisualCard(item)
        }
    }
}

@Composable
private fun ResourceVisualCard(item: ResourceCard) {
    BeautifulCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.32f),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .background(item.color.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                ResourceArt(item.kind, item.color)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(Color.White)
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = item.title, style = MaterialTheme.typography.headlineMedium, color = FieldGreen)
            }
        }
    }
}

@Composable
private fun ResourceArt(kind: ResourceKind, color: Color) {
    Canvas(modifier = Modifier.size(160.dp)) {
        drawCircle(color.copy(alpha = 0.18f), radius = size.minDimension * 0.48f)
        when (kind) {
            ResourceKind.Vaccine -> {
                drawRoundRect(color, topLeft = Offset(size.width * 0.35f, size.height * 0.25f), size = Size(size.width * 0.18f, size.height * 0.52f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
                drawLine(Color.White, Offset(size.width * 0.39f, size.height * 0.38f), Offset(size.width * 0.49f, size.height * 0.38f), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.53f, size.height * 0.28f), Offset(size.width * 0.72f, size.height * 0.18f), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
            }
            ResourceKind.Scheme -> {
                drawRoundRect(color, topLeft = Offset(size.width * 0.22f, size.height * 0.32f), size = Size(size.width * 0.56f, size.height * 0.36f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx()))
                drawCircle(Color.White, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.5f, size.height * 0.5f))
            }
            ResourceKind.Feed -> {
                repeat(5) { index ->
                    val x = size.width * (0.26f + index * 0.1f)
                    drawLine(color, Offset(x, size.height * 0.72f), Offset(x + size.width * 0.09f, size.height * 0.28f), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
                }
            }
            ResourceKind.Video -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width * 0.42f, size.height * 0.38f)
                    lineTo(size.width * 0.65f, size.height * 0.5f)
                    lineTo(size.width * 0.42f, size.height * 0.62f)
                    close()
                }
                drawPath(path, color)
                drawCircle(color, radius = size.minDimension * 0.4f, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx()))
            }
        }
    }
}
