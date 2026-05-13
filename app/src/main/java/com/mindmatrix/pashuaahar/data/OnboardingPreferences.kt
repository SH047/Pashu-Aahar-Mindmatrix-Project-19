package com.mindmatrix.pashuaahar.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mindmatrix.pashuaahar.domain.AppLanguage
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.onboardingDataStore by preferencesDataStore(name = "onboarding_preferences")

data class OnboardingPreferences(
    val language: AppLanguage = AppLanguage.English,
    val farmerLevel: FarmerLevel = FarmerLevel.Beginner,
    val isOnboardingComplete: Boolean = false
)

class OnboardingPreferencesStore(private val context: Context) {
    private companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_FARMER_LEVEL = stringPreferencesKey("farmer_level")
        val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val preferences: Flow<OnboardingPreferences> = context.onboardingDataStore.data.map { values ->
        val languageStr = values[KEY_LANGUAGE] ?: AppLanguage.English.name
        val language = runCatching { AppLanguage.valueOf(languageStr) }.getOrDefault(AppLanguage.English)
        
        val farmerLevelStr = values[KEY_FARMER_LEVEL] ?: FarmerLevel.Beginner.name
        val farmerLevel = runCatching { FarmerLevel.valueOf(farmerLevelStr) }.getOrDefault(FarmerLevel.Beginner)

        val onboardingComplete = values[KEY_ONBOARDING_COMPLETE] ?: false

        OnboardingPreferences(
            language = language,
            farmerLevel = farmerLevel,
            isOnboardingComplete = onboardingComplete
        )
    }

    suspend fun saveLanguage(language: AppLanguage) {
        context.onboardingDataStore.edit { values ->
            values[KEY_LANGUAGE] = language.name
        }
    }

    suspend fun saveFarmerLevel(level: FarmerLevel) {
        context.onboardingDataStore.edit { values ->
            values[KEY_FARMER_LEVEL] = level.name
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        context.onboardingDataStore.edit { values ->
            values[KEY_ONBOARDING_COMPLETE] = complete
        }
    }
}
