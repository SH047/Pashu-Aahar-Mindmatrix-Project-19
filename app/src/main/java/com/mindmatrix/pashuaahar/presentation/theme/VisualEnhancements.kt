package com.mindmatrix.pashuaahar.presentation.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.launch

// 🎨 Enhanced Card with Gradient & Shadow
@Composable
fun BeautifulCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(onClick) {
        if (onClick != null) {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = tween(100)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }
    
    Card(
        modifier = modifier
            .scale(scale.value)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF8BC34A).copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = border,
        onClick = onClick ?: {}
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFFFDD0).copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(16.dp),
            content = content
        )
    }
}

// ✨ Animated Button with Pulse Effect
@Composable
fun PulseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF8BC34A)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

// 🌟 Loading Shimmer Effect
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true
) {
    Box(
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White
                )
            )
    )
}

// 🎯 Success/Error Animated Icon
@Composable
fun AnimatedStatusIcon(
    isSuccess: Boolean,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
        launch {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(600)
            )
        }
    }
    
    Box(
        modifier = modifier
            .size(80.dp)
            .scale(scale.value)
            .background(
                color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isSuccess) "✓" else "✕",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

// 🎭 Slide-in Animation Wrapper
@Composable
fun SlideInContent(
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        ) + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        content()
    }
}

// 🌈 Gradient Background
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFDD0), // Cream
                        Color(0xFFE8F5E9), // Light Green
                        Color(0xFFC8E6C9)  // Meadow
                    )
                )
            ),
        content = content
    )
}