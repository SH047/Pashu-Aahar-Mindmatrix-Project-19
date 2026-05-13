package com.mindmatrix.pashuaahar.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.mindmatrix.pashuaahar.R

/**
 * Animated empty state with Lottie animation
 */
@Composable
fun EmptyStateWithAnimation(
    message: String,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    lottieAnimation: Int? = null,
    illustration: Int? = null
) {
    // Entry animation
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(600)) + 
                slideInVertically(initialOffsetY = { it / 2 })
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated illustration
            when {
                lottieAnimation != null -> {
                    LottieAnimationView(
                        animationRes = lottieAnimation,
                        modifier = Modifier.size(240.dp)
                    )
                }
                illustration != null -> {
                    FloatingIllustration(
                        illustrationRes = illustration,
                        modifier = Modifier.size(200.dp)
                    )
                }
                else -> {
                    // Fallback to simple icon
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .alpha(0.3f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Message with fade-in
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Pulsing Add button
            PulsingButton(
                onClick = onAddClick,
                text = "Add Your First Cow"
            )
        }
    }
}

/**
 * Lottie animation wrapper
 */
@Composable
fun LottieAnimationView(
    animationRes: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationRes)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * Floating illustration with subtle animation
 */
@Composable
fun FloatingIllustration(
    illustrationRes: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    
    Image(
        painter = painterResource(illustrationRes),
        contentDescription = null,
        modifier = modifier.graphicsLayer {
            translationY = offsetY
        }
    )
}

/**
 * Button with pulsing scale animation
 */
@Composable
fun PulsingButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

/**
 * Success animation overlay
 */
@Composable
fun SuccessAnimation(
    visible: Boolean,
    onComplete: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.3f),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // This will only work if you have success_check in res/raw
            // LottieAnimationView(
            //     animationRes = R.raw.success_check,
            //     modifier = Modifier.size(200.dp),
            //     iterations = 1
            // )
            
            // Fallback while waiting for assets
            Text(
                text = "✓",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    
    LaunchedEffect(visible) {
        if (visible) {
            kotlinx.coroutines.delay(2000)
            onComplete()
        }
    }
}

/**
 * Beautiful card with shimmer loading effect
 */
@Composable
fun AnimatedBeautifulCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.animateContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .then(
                    if (isLoading) Modifier.shimmerEffect() else Modifier
                )
        ) {
            content()
        }
    }
}

/**
 * Shimmer loading effect
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    this.alpha(alpha)
}
