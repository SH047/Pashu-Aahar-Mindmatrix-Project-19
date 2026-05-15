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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.mindmatrix.pashuaahar.domain.FeedIngredient
import com.mindmatrix.pashuaahar.domain.AppLanguage
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import com.mindmatrix.pashuaahar.domain.UserProfile
import com.mindmatrix.pashuaahar.presentation.theme.PashuAaharTheme
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
import com.mindmatrix.pashuaahar.presentation.screens.SavingsDashboardScreen
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
    data object Savings : AppRoute("savings")
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
    BottomNavItem(AppRoute.Savings, "Savings", NavGlyph.BarChart),
    BottomNavItem(AppRoute.HealthTips, "Health", NavGlyph.Shield)
)

@Composable
fun PashuAaharApp(viewModel: MainViewModel) {
    val state = viewModel.state.value
    PashuAaharAppContent(
        state = state,
        onLanguageSelected = viewModel::selectLanguage,
        onFarmerLevelSelected = viewModel::selectFarmerLevel,
        onCompleteOnboarding = viewModel::completeOnboarding,
        onSelectCowProfile = viewModel::selectCowProfile,
        onDailyCareToggled = viewModel::toggleDailyCare,
        onDismissReminder = viewModel::dismissReminder,
        onUpdateUserProfile = viewModel::updateUserProfile,
        onDeleteCowProfile = viewModel::deleteCowProfile,
        onCreateCowProfile = viewModel::createCowProfile,
        onToggleFeedIngredient = viewModel::toggleFeedIngredient,
        onSetSuperMixMode = viewModel::setSuperMixMode,
        onSetGrazingHours = viewModel::setGrazingHours,
        onUpdateCustomMix = { viewModel.updateCustomMix(it) },
        onUpdateCowProfile = viewModel::updateCowProfile,
        onAddIngredientToMix = viewModel::addIngredientToMix,
        onRemoveIngredientFromMix = viewModel::removeIngredientFromMix
    )
}

@Composable
fun PashuAaharAppContent(
    state: PashuAaharUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onFarmerLevelSelected: (FarmerLevel) -> Unit,
    onCompleteOnboarding: () -> Unit,
    onSelectCowProfile: (String) -> Unit,
    onDailyCareToggled: (String, DailyCareActivity) -> Unit,
    onDismissReminder: (String) -> Unit,
    onUpdateUserProfile: (UserProfile) -> Unit,
    onDeleteCowProfile: (String) -> Unit,
    onCreateCowProfile: (CowProfile) -> Unit,
    onToggleFeedIngredient: (String, String) -> Unit,
    onSetSuperMixMode: (Boolean) -> Unit,
    onSetGrazingHours: (Float) -> Unit,
    onUpdateCustomMix: (Set<String>) -> Unit,
    onUpdateCowProfile: (CowProfile) -> Unit,
    onAddIngredientToMix: (FeedIngredient) -> Unit,
    onRemoveIngredientFromMix: (String) -> Unit
) {
    val navController = rememberNavController()
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
                    onLanguageSelected = onLanguageSelected,
                    onFarmerLevelSelected = onFarmerLevelSelected,
                    onContinue = {
                        onCompleteOnboarding()
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
                        onSelectCowProfile(it)
                        navController.navigate(AppRoute.CowDetail.path)
                    },
                    onDailyCareToggled = onDailyCareToggled,
                    onDismissReminder = onDismissReminder,
                    onFarmHeaderClick = { navController.navigate(AppRoute.FarmManagement.path) }
                )
            }
            composable(AppRoute.FarmManagement.path) {
                FarmManagementScreen(
                    userProfile = state.userProfile,
                    currentLevel = state.selectedFarmerLevel,
                    onProfileUpdate = onUpdateUserProfile,
                    onLevelChange = onFarmerLevelSelected,
                    onAddCow = { navController.navigate(AppRoute.AddCow.path) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.HerdList.path) {
                HerdListScreen(
                    state = state,
                    onCowSelected = {
                        onSelectCowProfile(it)
                        navController.navigate(AppRoute.CowDetail.path)
                    },
                    onDeleteCow = onDeleteCowProfile,
                    onAddCow = { navController.navigate(AppRoute.AddCow.path) }
                )
            }
            composable(AppRoute.AddCow.path) {
                AddCowScreen(
                    nextAvatarStyle = state.cowProfiles.size % 6,
                    onSave = {
                        onCreateCowProfile(it)
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
                        onDeleteCowProfile(id)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.Nutrition.path) {
                NutritionScreen(
                    state = state,
                    onCowSelected = onSelectCowProfile,
                    onToggleFeed = onToggleFeedIngredient,
                    onModeChange = onSetSuperMixMode,
                    onGrazingChange = onSetGrazingHours
                )
            }
            composable(AppRoute.Nutrition.path) {
                NutritionScreen(
                    state = state,
                    onCowSelected = onSelectCowProfile,
                    onToggleFeed = onToggleFeedIngredient,
                    onModeChange = onSetSuperMixMode,
                    onGrazingChange = onSetGrazingHours,
                    onAddStandardIngredient = onAddIngredientToMix,
                    onRemoveIngredient = onRemoveIngredientFromMix
                )
            }
            composable(AppRoute.CustomFeed.path) {
                CustomFeedScreen(
                    ingredients = state.feedIngredients,
                    onRecipeSaved = { 
                        onUpdateCustomMix(it.keys)
                        navController.navigate(AppRoute.Nutrition.path)
                    }
                )
            }
            composable(AppRoute.HealthTips.path) {
                HealthTipsScreen(
                    state = state,
                    onCowSelected = onSelectCowProfile,
                    onBcsSelected = { score ->
                        onUpdateCowProfile(state.cowProfile.copy(bcsScore = score))
                    }
                )
            }
            composable(AppRoute.Savings.path) {
                SavingsDashboardScreen(
                    customIngredients = state.customMixIngredients,
                    targetProtein = 1.2, // 1.2kg CP target
                    onAddIngredient = onAddIngredientToMix,
                    onRemoveIngredient = onRemoveIngredientFromMix
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

@Preview(showBackground = true)
@Composable
fun PashuAaharAppOnboardingPreview() {
    PashuAaharTheme {
        PashuAaharAppContent(
            state = PashuAaharUiState(isOnboardingComplete = false),
            onLanguageSelected = {},
            onFarmerLevelSelected = {},
            onCompleteOnboarding = {},
            onSelectCowProfile = {},
            onDailyCareToggled = { _, _ -> },
            onDismissReminder = {},
            onUpdateUserProfile = {},
            onDeleteCowProfile = {},
            onCreateCowProfile = {},
            onToggleFeedIngredient = { _, _ -> },
            onSetSuperMixMode = {},
            onSetGrazingHours = {},
            onUpdateCustomMix = {},
            onUpdateCowProfile = {},
            onAddIngredientToMix = {},
            onRemoveIngredientFromMix = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PashuAaharAppDashboardPreview() {
    PashuAaharTheme {
        PashuAaharAppContent(
            state = PashuAaharUiState(isOnboardingComplete = true),
            onLanguageSelected = {},
            onFarmerLevelSelected = {},
            onCompleteOnboarding = {},
            onSelectCowProfile = {},
            onDailyCareToggled = { _, _ -> },
            onDismissReminder = {},
            onUpdateUserProfile = {},
            onDeleteCowProfile = {},
            onCreateCowProfile = {},
            onToggleFeedIngredient = { _, _ -> },
            onSetSuperMixMode = {},
            onSetGrazingHours = {},
            onUpdateCustomMix = {},
            onUpdateCowProfile = {},
            onAddIngredientToMix = {},
            onRemoveIngredientFromMix = {}
        )
    }
}
