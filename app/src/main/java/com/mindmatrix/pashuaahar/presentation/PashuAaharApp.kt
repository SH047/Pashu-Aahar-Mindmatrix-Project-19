package com.mindmatrix.pashuaahar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindmatrix.pashuaahar.presentation.components.NavGlyph
import com.mindmatrix.pashuaahar.presentation.screens.AddCowScreen
import com.mindmatrix.pashuaahar.presentation.screens.CowDetailScreen
import com.mindmatrix.pashuaahar.presentation.screens.CustomFeedScreen
import com.mindmatrix.pashuaahar.presentation.screens.DashboardScreen
import com.mindmatrix.pashuaahar.presentation.screens.FarmManagementScreen
import com.mindmatrix.pashuaahar.presentation.screens.HealthTipsScreen
import com.mindmatrix.pashuaahar.presentation.screens.HerdListScreen
import com.mindmatrix.pashuaahar.presentation.screens.NutritionScreen
import com.mindmatrix.pashuaahar.presentation.screens.OnboardingScreen
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.Meadow

sealed class AppRoute(val path: String) {
    data object Onboarding : AppRoute("onboarding")
    data object Dashboard : AppRoute("dashboard")
    data object HerdList : AppRoute("herd_list")
    data object AddCow : AppRoute("add_cow")
    data object CowDetail : AppRoute("cow_detail")
    data object FarmManagement : AppRoute("farm_management")
    data object Nutrition : AppRoute("nutrition")
    data object CustomFeed : AppRoute("custom_feed")
    data object HealthTips : AppRoute("health_tips")
}

private data class BottomNavItem(
    val route: AppRoute,
    val label: String,
    val glyph: NavGlyph
)

private val bottomNavItems = listOf(
    BottomNavItem(AppRoute.Dashboard, "Dashboard", NavGlyph.Home),
    BottomNavItem(AppRoute.Nutrition, "Plan", NavGlyph.Leaf),
    BottomNavItem(AppRoute.CustomFeed, "Mix", NavGlyph.Leaf),
    BottomNavItem(AppRoute.HealthTips, "Health", NavGlyph.Shield)
)

@Composable
fun PashuAaharApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val state = viewModel.state.value
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route.path } == true
    }

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            if (showBottomBar && state.isOnboardingComplete) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White)
                ) {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 6.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.route.path } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navController.navigateBottom(item.route) },
                                icon = {
                                    com.mindmatrix.pashuaahar.presentation.components.NavIcon(
                                        glyph = item.glyph,
                                        selected = selected
                                    )
                                },
                                label = { Text(item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = FieldGreen,
                                    selectedTextColor = FieldGreen,
                                    indicatorColor = Meadow,
                                    unselectedIconColor = FieldGreen.copy(alpha = 0.52f),
                                    unselectedTextColor = FieldGreen.copy(alpha = 0.62f)
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (state.isOnboardingComplete) AppRoute.Dashboard.path else AppRoute.Onboarding.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Onboarding.path) {
                OnboardingScreen(
                    state = state,
                    onLanguageSelected = viewModel::selectLanguage,
                    onFarmerLevelSelected = viewModel::selectFarmerLevel,
                    onContinue = {
                        viewModel.completeOnboarding()
                        navController.navigate(AppRoute.Dashboard.path) {
                            popUpTo(AppRoute.Onboarding.path) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppRoute.Dashboard.path) {
                DashboardScreen(
                    state = state,
                    onCowSelected = {
                        viewModel.selectCowProfile(it)
                        navController.navigate(AppRoute.CowDetail.path)
                    },
                    onDailyCareToggled = viewModel::toggleDailyCare,
                    onDismissReminder = viewModel::dismissReminder,
                    onFarmHeaderClick = { navController.navigate(AppRoute.FarmManagement.path) }
                )
            }
            composable(AppRoute.FarmManagement.path) {
                FarmManagementScreen(
                    userProfile = state.userProfile,
                    currentLevel = state.selectedFarmerLevel,
                    onProfileUpdate = viewModel::updateUserProfile,
                    onLevelChange = viewModel::selectFarmerLevel,
                    onAddCow = { navController.navigate(AppRoute.AddCow.path) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.HerdList.path) {
                HerdListScreen(
                    state = state,
                    onCowSelected = {
                        viewModel.selectCowProfile(it)
                        navController.navigate(AppRoute.CowDetail.path)
                    },
                    onDeleteCow = viewModel::deleteCowProfile,
                    onAddCow = { navController.navigate(AppRoute.AddCow.path) }
                )
            }
            composable(AppRoute.AddCow.path) {
                AddCowScreen(
                    nextAvatarStyle = state.cowProfiles.size % 6,
                    onSave = {
                        viewModel.createCowProfile(it)
                        navController.navigate(AppRoute.HerdList.path) {
                            popUpTo(AppRoute.HerdList.path) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.CowDetail.path) {
                CowDetailScreen(
                    profile = state.cowProfile,
                    bodyCondition = state.bodyCondition,
                    onDelete = { id ->
                        viewModel.deleteCowProfile(id)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.Nutrition.path) {
                NutritionScreen(
                    state = state,
                    onCowSelected = viewModel::selectCowProfile,
                    onToggleFeed = viewModel::toggleFeedIngredient,
                    onModeChange = viewModel::setSuperMixMode,
                    onGrazingChange = viewModel::setGrazingHours
                )
            }
            composable(AppRoute.CustomFeed.path) {
                CustomFeedScreen(
                    ingredients = state.feedIngredients,
                    onRecipeSaved = { 
                        viewModel.updateCustomMix(it.keys)
                        navController.navigate(AppRoute.Nutrition.path)
                    }
                )
            }
            composable(AppRoute.HealthTips.path) {
                HealthTipsScreen(
                    state = state,
                    onCowSelected = viewModel::selectCowProfile,
                    onBcsSelected = { score ->
                        viewModel.updateCowProfile(state.cowProfile.copy(bcsScore = score))
                    }
                )
            }
        }
    }
}

private fun NavHostController.navigateBottom(route: AppRoute) {
    navigate(route.path) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
