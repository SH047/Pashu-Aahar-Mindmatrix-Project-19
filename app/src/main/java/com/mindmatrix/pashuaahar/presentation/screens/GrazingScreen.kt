package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.data.SeedData
import com.mindmatrix.pashuaahar.domain.GrazingGrass
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.components.VisualBadge
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceWarm

@Composable
fun GrazingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        ScreenHeader(
            title = "Grazing library",
            subtitle = "Offline grass nutrition notes for field decisions."
        )
        Spacer(modifier = Modifier.height(18.dp))
        SeedData.grasses.forEach { grass ->
            GrassCard(grass = grass)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GrassCard(grass: GrazingGrass) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = grass.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            VisualBadge(label = grass.season, color = FieldGreen)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${grass.proteinPercent}% protein, ${grass.waterNeed} water need",
                style = MaterialTheme.typography.titleLarge,
                color = Sky
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = grass.recommendation, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
