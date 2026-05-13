package com.mindmatrix.pashuaahar.data

import android.content.Context
import com.mindmatrix.pashuaahar.domain.AppLanguage
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.domain.FarmerLevel
import com.mindmatrix.pashuaahar.domain.UserProfile
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PashuAaharRepository(context: Context) {
    private val preferences = context.getSharedPreferences("pashu_aahar_local", Context.MODE_PRIVATE)
    private val defaultCow = CowProfile()
    private val defaultUser = UserProfile()
    val todayKey: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    var farmerLevel: FarmerLevel = runCatching {
        FarmerLevel.valueOf(preferences.getString(KEY_FARMER_LEVEL, FarmerLevel.Beginner.name).orEmpty())
    }.getOrDefault(FarmerLevel.Beginner)
        private set

    var language: AppLanguage = runCatching {
        AppLanguage.valueOf(preferences.getString(KEY_LANGUAGE, AppLanguage.English.name).orEmpty())
    }.getOrDefault(AppLanguage.English)
        private set

    var userProfile: UserProfile = UserProfile(
        farmerName = preferences.getString(KEY_USER_NAME, defaultUser.farmerName) ?: defaultUser.farmerName,
        village = preferences.getString(KEY_USER_VILLAGE, defaultUser.village) ?: defaultUser.village,
        phoneNumber = preferences.getString(KEY_USER_PHONE, defaultUser.phoneNumber) ?: defaultUser.phoneNumber,
        farmName = preferences.getString(KEY_USER_FARM, defaultUser.farmName) ?: defaultUser.farmName
    )
        private set

    var cowProfiles: List<CowProfile> = loadCowProfiles()
        private set

    var selectedCowId: String = (preferences.getString(KEY_SELECTED_COW_ID, cowProfiles.first().id) ?: cowProfiles.first().id)
        .let { storedId -> cowProfiles.firstOrNull { it.id == storedId }?.id ?: cowProfiles.first().id }
        private set

    var dailyCareStatus: Map<String, Set<DailyCareActivity>> = loadDailyCareStatus(todayKey)
        private set

    var feedCompletionStatus: Map<String, Set<String>> = loadFeedCompletionStatus(todayKey)
        private set

    var feedingHistory: Map<String, Float> = loadFullFeedingHistory()
        private set

    var isSuperMixMode: Boolean = preferences.getBoolean(KEY_IS_SUPER_MIX, false)
        private set

    var customMixIngredientIds: Set<String> = preferences.getStringSet(KEY_CUSTOM_MIX_IDS, emptySet()) ?: emptySet()
        private set

    var dismissedReminderIds: Set<String> = preferences.getStringSet(KEY_DISMISSED_REMINDERS, emptySet()) ?: emptySet()
        private set

    val selectedCowProfile: CowProfile
        get() = cowProfiles.firstOrNull { it.id == selectedCowId } ?: cowProfiles.first()

    fun updateFarmerLevel(level: FarmerLevel) {
        farmerLevel = level
        preferences.edit().putString(KEY_FARMER_LEVEL, level.name).apply()
    }

    fun updateLanguage(value: AppLanguage) {
        language = value
        preferences.edit().putString(KEY_LANGUAGE, value.name).apply()
    }

    fun updateUserProfile(profile: UserProfile) {
        userProfile = profile
        preferences.edit()
            .putString(KEY_USER_NAME, profile.farmerName)
            .putString(KEY_USER_VILLAGE, profile.village)
            .putString(KEY_USER_PHONE, profile.phoneNumber)
            .putString(KEY_USER_FARM, profile.farmName)
            .apply()
    }

    fun selectCowProfile(id: String) {
        if (cowProfiles.none { it.id == id }) return
        selectedCowId = id
        preferences.edit().putString(KEY_SELECTED_COW_ID, id).apply()
    }

    fun addCowProfile() {
        val newCow = CowProfile(
            id = UUID.randomUUID().toString(),
            name = "Cow ${cowProfiles.size + 1}",
            avatarStyle = cowProfiles.size % 4
        )
        createCowProfile(newCow)
    }

    fun createCowProfile(profile: CowProfile) {
        val cow = profile.copy(
            id = if (profile.id == CowProfile().id && cowProfiles.any { it.id == profile.id }) {
                UUID.randomUUID().toString()
            } else {
                profile.id
            },
            avatarStyle = profile.avatarStyle.coerceIn(0, 5)
        )
        cowProfiles = cowProfiles + cow
        selectedCowId = cow.id
        saveCowProfiles()
    }

    fun updateCowProfile(profile: CowProfile) {
        cowProfiles = cowProfiles.map { cow ->
            if (cow.id == profile.id) profile else cow
        }
        selectedCowId = profile.id
        saveCowProfiles()
    }

    fun deleteCowProfile(id: String) {
        if (cowProfiles.size <= 1) return // Keep at least one cow for safety or handle empty state in UI
        cowProfiles = cowProfiles.filter { it.id != id }
        if (selectedCowId == id) {
            selectedCowId = cowProfiles.first().id
        }
        saveCowProfiles()
    }

    fun toggleDailyCare(cowId: String, activity: DailyCareActivity) {
        if (cowProfiles.none { it.id == cowId }) return

        val currentActivities = dailyCareStatus[cowId].orEmpty()
        val updatedActivities = if (activity in currentActivities) {
            currentActivities - activity
        } else {
            currentActivities + activity
        }

        dailyCareStatus = dailyCareStatus
            .filterKeys { storedCowId -> cowProfiles.any { it.id == storedCowId } }
            .toMutableMap()
            .apply { put(cowId, updatedActivities) }

        saveDailyCareStatus()
    }

    fun toggleFeedIngredient(cowId: String, ingredientId: String) {
        if (cowProfiles.none { it.id == cowId }) return

        val currentIngredients = feedCompletionStatus[cowId].orEmpty()
        val updatedIngredients = if (ingredientId in currentIngredients) {
            currentIngredients - ingredientId
        } else {
            currentIngredients + ingredientId
        }

        feedCompletionStatus = feedCompletionStatus
            .filterKeys { storedCowId -> cowProfiles.any { it.id == storedCowId } }
            .toMutableMap()
            .apply { put(cowId, updatedIngredients) }

        saveFeedCompletionStatus()
        updateFeedingHistoryForToday()
    }

    private fun updateFeedingHistoryForToday() {
        val totalNeeded = cowProfiles.size * 4 // Assuming 4 items per cow standard
        if (totalNeeded == 0) return
        val totalDone = feedCompletionStatus.values.sumOf { it.size }
        val score = totalDone.toFloat() / totalNeeded
        feedingHistory = feedingHistory + (todayKey to score)
        saveFullFeedingHistory()
    }

    fun setSuperMixMode(enabled: Boolean) {
        isSuperMixMode = enabled
        preferences.edit().putBoolean(KEY_IS_SUPER_MIX, enabled).apply()
    }

    fun updateCustomMix(ingredientIds: Set<String>) {
        customMixIngredientIds = ingredientIds
        preferences.edit().putStringSet(KEY_CUSTOM_MIX_IDS, ingredientIds).apply()
    }

    fun dismissReminder(id: String) {
        dismissedReminderIds = dismissedReminderIds + id
        preferences.edit().putStringSet(KEY_DISMISSED_REMINDERS, dismissedReminderIds).apply()
    }

    private fun loadCowProfiles(): List<CowProfile> {
        val storedProfiles = preferences.getString(KEY_COW_PROFILES, null)
        if (!storedProfiles.isNullOrBlank()) {
            val decoded = runCatching {
                val array = JSONArray(storedProfiles)
                List(array.length()) { index ->
                    array.getJSONObject(index).toCowProfile()
                }
            }.getOrDefault(emptyList())

            if (decoded.isNotEmpty()) return decoded
        }

        return listOf(loadLegacyCowProfile())
    }

    private fun loadLegacyCowProfile(): CowProfile = CowProfile(
        id = preferences.getString(KEY_COW_ID, defaultCow.id) ?: defaultCow.id,
        name = preferences.getString(KEY_COW_NAME, defaultCow.name) ?: defaultCow.name,
        breed = preferences.getString(KEY_COW_BREED, defaultCow.breed) ?: defaultCow.breed,
        weightKg = preferences.getInt(KEY_COW_WEIGHT, defaultCow.weightKg),
        ageMonths = preferences.getInt(KEY_COW_AGE, defaultCow.ageMonths),
        dailyMilkLitres = preferences.getFloat(KEY_COW_MILK, defaultCow.dailyMilkLitres),
        pregnancyMonth = preferences.getInt(KEY_COW_PREGNANCY, defaultCow.pregnancyMonth),
        lactationDay = preferences.getInt(KEY_COW_LACTATION, defaultCow.lactationDay)
    )

    private fun saveCowProfiles() {
        val profilesJson = JSONArray().apply {
            cowProfiles.forEach { profile -> put(profile.toJson()) }
        }.toString()

        preferences.edit()
            .putString(KEY_SELECTED_COW_ID, selectedCowId)
            .putString(KEY_COW_PROFILES, profilesJson)
            .apply()
    }

    private fun loadDailyCareStatus(dateKey: String): Map<String, Set<DailyCareActivity>> {
        val storedStatus = preferences.getString(dailyCareKey(dateKey), null)
        if (storedStatus.isNullOrBlank()) return emptyMap()

        return runCatching {
            val root = JSONObject(storedStatus)
            root.keys().asSequence().associateWith { cowId ->
                val activities = root.getJSONArray(cowId)
                List(activities.length()) { index ->
                    runCatching { DailyCareActivity.valueOf(activities.getString(index)) }.getOrNull()
                }.filterNotNull().toSet()
            }
        }.getOrDefault(emptyMap())
    }

    private fun saveDailyCareStatus() {
        val statusJson = JSONObject().apply {
            dailyCareStatus.forEach { (cowId, activities) ->
                put(
                    cowId,
                    JSONArray().apply {
                        activities.forEach { activity -> put(activity.name) }
                    }
                )
            }
        }.toString()

        preferences.edit()
            .putString(dailyCareKey(todayKey), statusJson)
            .apply()
    }

    private fun loadFeedCompletionStatus(dateKey: String): Map<String, Set<String>> {
        val storedStatus = preferences.getString(feedCompletionKey(dateKey), null)
        if (storedStatus.isNullOrBlank()) return emptyMap()

        return runCatching {
            val root = JSONObject(storedStatus)
            root.keys().asSequence().associateWith { cowId ->
                val ingredients = root.getJSONArray(cowId)
                List(ingredients.length()) { index ->
                    ingredients.getString(index)
                }.toSet()
            }
        }.getOrDefault(emptyMap())
    }

    private fun saveFeedCompletionStatus() {
        val statusJson = JSONObject().apply {
            feedCompletionStatus.forEach { (cowId, ingredients) ->
                put(
                    cowId,
                    JSONArray().apply {
                        ingredients.forEach { put(it) }
                    }
                )
            }
        }.toString()

        preferences.edit()
            .putString(feedCompletionKey(todayKey), statusJson)
            .apply()
    }

    private fun loadFullFeedingHistory(): Map<String, Float> {
        val stored = preferences.getString(KEY_FEEDING_HISTORY, null) ?: return emptyMap()
        return runCatching {
            val root = JSONObject(stored)
            root.keys().asSequence().associateWith { key ->
                root.getDouble(key).toFloat()
            }
        }.getOrDefault(emptyMap())
    }

    private fun saveFullFeedingHistory() {
        val json = JSONObject().apply {
            feedingHistory.forEach { (key, value) -> put(key, value.toDouble()) }
        }.toString()
        preferences.edit().putString(KEY_FEEDING_HISTORY, json).apply()
    }

    private fun dailyCareKey(dateKey: String): String = "${KEY_DAILY_CARE}_$dateKey"
    
    private fun feedCompletionKey(dateKey: String): String = "${KEY_FEED_COMPLETION}_$dateKey"

    private fun CowProfile.toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("breed", breed)
        .put("weightKg", weightKg)
        .put("ageMonths", ageMonths)
        .put("dailyMilkLitres", dailyMilkLitres)
        .put("pregnancyMonth", pregnancyMonth)
        .put("lactationDay", lactationDay)
        .put("lactationStage", lactationStage)
        .put("avatarStyle", avatarStyle)
        .put("bcsScore", bcsScore)
        .put("fmdVaccinated", fmdVaccinated)
        .put("brucellosisVaccinated", brucellosisVaccinated)
        .put("dewormed", dewormed)

    private fun JSONObject.toCowProfile(): CowProfile = CowProfile(
        id = optString("id", UUID.randomUUID().toString()),
        name = optString("name", defaultCow.name),
        breed = optString("breed", defaultCow.breed),
        weightKg = optInt("weightKg", defaultCow.weightKg),
        ageMonths = optInt("ageMonths", defaultCow.ageMonths),
        dailyMilkLitres = optDouble("dailyMilkLitres", defaultCow.dailyMilkLitres.toDouble()).toFloat(),
        pregnancyMonth = optInt("pregnancyMonth", defaultCow.pregnancyMonth),
        lactationDay = optInt("lactationDay", defaultCow.lactationDay),
        lactationStage = optString("lactationStage", defaultCow.lactationStage),
        avatarStyle = optInt("avatarStyle", defaultCow.avatarStyle),
        bcsScore = optInt("bcsScore", defaultCow.bcsScore),
        fmdVaccinated = optBoolean("fmdVaccinated", defaultCow.fmdVaccinated),
        brucellosisVaccinated = optBoolean("brucellosisVaccinated", defaultCow.brucellosisVaccinated),
        dewormed = optBoolean("dewormed", defaultCow.dewormed)
    )

    private companion object {
        const val KEY_FARMER_LEVEL = "farmer_level"
        const val KEY_LANGUAGE = "language"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_VILLAGE = "user_village"
        const val KEY_USER_PHONE = "user_phone"
        const val KEY_USER_FARM = "user_farm"
        const val KEY_COW_PROFILES = "cow_profiles"
        const val KEY_SELECTED_COW_ID = "selected_cow_id"
        const val KEY_COW_ID = "cow_id"
        const val KEY_COW_NAME = "cow_name"
        const val KEY_COW_BREED = "cow_breed"
        const val KEY_COW_WEIGHT = "cow_weight"
        const val KEY_COW_AGE = "cow_age"
        const val KEY_COW_MILK = "cow_milk"
        const val KEY_COW_PREGNANCY = "cow_pregnancy"
        const val KEY_COW_LACTATION = "cow_lactation"
        const val KEY_DAILY_CARE = "daily_care"
        const val KEY_FEED_COMPLETION = "feed_completion"
        const val KEY_IS_SUPER_MIX = "is_super_mix"
        const val KEY_CUSTOM_MIX_IDS = "custom_mix_ids"
        const val KEY_DISMISSED_REMINDERS = "dismissed_reminders"
        const val KEY_FEEDING_HISTORY = "feeding_history"
    }
}
