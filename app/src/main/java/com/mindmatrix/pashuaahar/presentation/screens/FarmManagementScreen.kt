package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import com.mindmatrix.pashuaahar.domain.UserProfile
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.PulseButton
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent

@Composable
fun FarmManagementScreen(
    userProfile: UserProfile,
    currentLevel: FarmerLevel,
    onProfileUpdate: (UserProfile) -> Unit,
    onLevelChange: (FarmerLevel) -> Unit,
    onAddCow: () -> Unit,
    onBack: () -> Unit
) {
    var farmName by remember { mutableStateOf(userProfile.farmName) }

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                ScreenHeader(title = "My Dairy Farm", subtitle = "Manage your farm identity")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Farm Identity Card
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Farm Identity", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = farmName,
                            onValueChange = { 
                                farmName = it
                                onProfileUpdate(userProfile.copy(farmName = it))
                            },
                            label = { Text("Farm Name") },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Farmer Level Selector
                Text(text = "Farmer Level", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                FarmerLevel.entries.forEach { level ->
                    val selected = level == currentLevel
                    BeautifulCard(
                        onClick = { onLevelChange(level) },
                        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) FieldGreen else Color.LightGray.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selected, onClick = { onLevelChange(level) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = level.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(text = level.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Quick Actions
                PulseButton(
                    text = "+ Add New Cow",
                    onClick = onAddCow,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Back to Dashboard", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}
