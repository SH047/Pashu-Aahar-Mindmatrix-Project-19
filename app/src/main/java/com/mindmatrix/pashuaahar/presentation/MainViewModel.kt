package com.mindmatrix.pashuaahar.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.pashuaahar.data.OnboardingPreferencesStore
import com.mindmatrix.pashuaahar.data.PashuAaharRepository
import com.mindmatrix.pashuaahar.data.SeedData
import com.mindmatrix.pashuaahar.domain.AppLanguage
import com.mindmatrix.pashuaahar.domain.BodyConditionResult
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import com.mindmatrix.pashuaahar.domain.FeedIngredient
import com.mindmatrix.pashuaahar.domain.FeedPlan
import com.mindmatrix.pashuaahar.domain.HealthCalculator
import com.mindmatrix.pashuaahar.domain.NutritionCalculator
import com.mindmatrix.pashuaahar.domain.UserProfile
import kotlinx.coroutines.launch

data class PashuAaharUiState(
    val isLoading: Boolean = false,
    val selectedFarmerLevel: FarmerLevel = FarmerLevel.Beginner,
    val selectedLanguage: AppLanguage = AppLanguage.English,
    val isOnboardingComplete: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val cowProfiles: List<CowProfile> = listOf(CowProfile()),
    val selectedCowId: String = CowProfile().id,
    val cowProfile: CowProfile = CowProfile(),
    val todayKey: String = "",
    val dailyCareStatus: Map<String, Set<DailyCareActivity>> = emptyMap(),
    val cowBodyConditions: Map<String, BodyConditionResult> = emptyMap(),
    val feedIngredients: List<FeedIngredient> = SeedData.ingredients,
    val feedCompletionStatus: Map<String, Set<String>> = emptyMap(),
    val feedingHistory: Map<String, Float> = emptyMap(),
    val dismissedReminderIds: Set<String> = emptySet(),
    val isSuperMixMode: Boolean = false,
    val grazingHours: Float = 2f,
    val feedPlan: FeedPlan = NutritionCalculator().calculate(CowProfile(), SeedData.ingredients),
    val bodyCondition: BodyConditionResult = HealthCalculator().bodyCondition(CowProfile())
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PashuAaharRepository(application.applicationContext)
    private val onboardingPreferencesStore = OnboardingPreferencesStore(application.applicationContext)
    private val nutritionCalculator = NutritionCalculator()
    private val healthCalculator = HealthCalculator()

    private val _state = mutableStateOf(PashuAaharUiState())
    val state: State<PashuAaharUiState> = _state

    init {
        publish()
        viewModelScope.launch {
            onboardingPreferencesStore.preferences.collect { preferences ->
                repository.updateLanguage(preferences.language)
                repository.updateFarmerLevel(preferences.farmerLevel)
                _isOnboardingComplete = preferences.isOnboardingComplete
                publish()
            }
        }
    }

    private var _isOnboardingComplete = false

    fun selectFarmerLevel(level: FarmerLevel) {
        repository.updateFarmerLevel(level)
        viewModelScope.launch {
            onboardingPreferencesStore.saveFarmerLevel(level)
        }
        publish()
    }

    fun selectLanguage(language: AppLanguage) {
        repository.updateLanguage(language)
        viewModelScope.launch {
            onboardingPreferencesStore.saveLanguage(language)
        }
        publish()
    }

    fun updateUserProfile(profile: UserProfile) {
        repository.updateUserProfile(profile)
        publish()
    }

    fun selectCowProfile(id: String) {
        repository.selectCowProfile(id)
        publish()
    }

    fun addCowProfile() {
        repository.addCowProfile()
        publish()
    }

    fun createCowProfile(profile: CowProfile) {
        repository.createCowProfile(profile)
        publish()
    }

    fun updateCowProfile(profile: CowProfile) {
        repository.updateCowProfile(profile)
        publish()
    }

    fun deleteCowProfile(id: String) {
        repository.deleteCowProfile(id)
        publish()
    }

    fun toggleDailyCare(cowId: String, activity: DailyCareActivity) {
        repository.toggleDailyCare(cowId, activity)
        publish()
    }

    fun toggleFeedIngredient(cowId: String, ingredientId: String) {
        repository.toggleFeedIngredient(cowId, ingredientId)
        publish()
    }

    fun setSuperMixMode(enabled: Boolean) {
        repository.setSuperMixMode(enabled)
        publish()
    }

    fun updateCustomMix(ingredientIds: Set<String>) {
        repository.updateCustomMix(ingredientIds)
        publish()
    }

    private var _grazingHours = 2f
    fun setGrazingHours(hours: Float) {
        _grazingHours = hours
        publish()
    }

    fun dismissReminder(id: String) {
        repository.dismissReminder(id)
        publish()
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingPreferencesStore.setOnboardingComplete(true)
        }
    }

    private fun publish() {
        val profile = repository.selectedCowProfile
        val dailyCareStatus = repository.dailyCareStatus
        val cowBodyConditions = repository.cowProfiles.associate { cow ->
            cow.id to healthCalculator.bodyCondition(cow, careCompletionRatio(cow.id, dailyCareStatus))
        }
        _state.value = PashuAaharUiState(
            isLoading = false,
            selectedFarmerLevel = repository.farmerLevel,
            selectedLanguage = repository.language,
            isOnboardingComplete = _isOnboardingComplete,
            userProfile = repository.userProfile,
            cowProfiles = repository.cowProfiles,
            selectedCowId = repository.selectedCowId,
            cowProfile = profile,
            todayKey = repository.todayKey,
            dailyCareStatus = dailyCareStatus,
            cowBodyConditions = cowBodyConditions,
            feedCompletionStatus = repository.feedCompletionStatus,
            feedingHistory = repository.feedingHistory,
            dismissedReminderIds = repository.dismissedReminderIds,
            isSuperMixMode = repository.isSuperMixMode,
            grazingHours = _grazingHours,
            feedPlan = nutritionCalculator.calculate(
                profile = profile,
                ingredients = SeedData.ingredients,
                isSuperMix = repository.isSuperMixMode,
                customMixIds = repository.customMixIngredientIds,
                grazingHours = _grazingHours
            ),
            bodyCondition = cowBodyConditions.getValue(profile.id)
        )
    }

    private fun careCompletionRatio(
        cowId: String,
        dailyCareStatus: Map<String, Set<DailyCareActivity>>
    ): Float {
        val completed = dailyCareStatus[cowId].orEmpty().size
        return completed / DailyCareActivity.entries.size.toFloat()
    }
}
