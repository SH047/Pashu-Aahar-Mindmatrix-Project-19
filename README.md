# STANDARD OPERATING PROCEDURE (SOP)
## PASHU-AAHAR CATTLE NUTRITION CALCULATOR V3.0 (REVISED)

## Android App Status

This repository now contains a Kotlin Android app scaffold for Pashu-Aahar.

- Android app module: `app/`
- Language: Kotlin
- UI: Jetpack Compose + Material 3
- Minimum SDK: 24
- Target SDK: 34
- Current features: farmer onboarding, language selection, persisted cow profile editor, feed calculator, savings view, grazing library, health meter, pregnancy progress, and vaccination reminders.

### Open in Android Studio

1. Open this folder in Android Studio.
2. Let Gradle sync download the Android Gradle Plugin and AndroidX dependencies.
3. Select the `app` run configuration.
4. Run on an Android emulator or physical device.

The current implementation is intentionally offline-first and seeded with local cattle nutrition data. Room, WorkManager, Hilt, real image assets, and sync APIs can be layered on next without changing the user-facing flow.

**Document Version:** 3.0 (Post-Review Revision)  
**Document Owner:** Shreyas S Rai  
**Platform:** Android (Min SDK 24, Target SDK 34)  
**Last Updated:** may 2026

---

## 1. PROJECT OVERVIEW & STRATEGIC VISION

### 1.1 Project Identity
- **Project Name:** Pashu-Aahar 
- **Translation:** "Cattle Nutrition" in kannada
- **Version:** 3.0.0
- **Platform:** Android (Minimum SDK 24, Target SDK 34)
- **Design Mandate:** **70% Visual / 30% Text** for maximum accessibility

### 1.2 Target Audience Profile

**Primary Users (Segmented by Farmer Level):**

**Beginner Farmers:**
- Herd size: 1-2 cattle
- Age range: 25-45 years
- Digital literacy: Very Low
- Primary need: Step-by-step visual guides for basic feeding
- Language: Primarily vernacular (Hindi/Kannada/Regional)
- Internet access: Rare or non-existent
- Device: Entry-level smartphones (2GB RAM)

**Intermediate Farmers:**
- Herd size: 3-10 cattle
- Age range: 30-55 years
- Digital literacy: Low to Moderate
- Primary need: Cost optimization and yield improvement
- Language: Bilingual (vernacular + basic English)
- Internet access: Intermittent (2-3 times/week)
- Device: Mid-range smartphones (3-4GB RAM)

**Large Scale Farmers:**
- Herd size: 10+ cattle
- Age range: 35-60 years
- Digital literacy: Moderate
- Primary need: Bulk feed planning, herd analytics, inventory management
- Language: Comfortable with English + vernacular
- Internet access: Regular (daily or alternate days)
- Device: Mid to high-end smartphones (4GB+ RAM)

**Secondary Users:**
- Agricultural extension workers
- Village-level dairy cooperatives
- Veterinary field officers
- Cattle feed retailers

### 1.3 Core Problem Statement
Indian dairy farmers face five critical challenges:
1. **Information Gap:** Lack of scientific knowledge about balanced cattle nutrition
2. **Economic Pressure:** High cost of commercial feed compounds (₹25-35/kg)
3. **Resource Misallocation:** Suboptimal milk yield due to improper feeding practices
4. **Health Monitoring:** No systematic tracking of cattle BMI, pregnancy, lactation cycles
5. **Grazing Knowledge Gap:** Limited understanding of nutritional value of different grasses (green vs dry fodder)

### 1.4 Solution Design Philosophy

**Visual-First Principles (70:30 Rule):**
- **70% Visual Elements:**
  - High-quality photographs (WebP format, <100KB each)
  - Illustrative icons (Material Design 3 + custom cattle-specific icons)
  - Color-coded indicators (green = healthy, yellow = caution, red = action needed)
  - Visual sliders with animated feedback
  - Infographic-style data display
- **30% Text Elements:**
  - Large, readable fonts (minimum 16sp body, 24sp headers)
  - Bilingual labels (English + selected regional language)
  - Audio narration support for critical instructions
  - Icon-to-text ratio: minimum 70:30 across all screens
  - Minimum touch target: 48x48 dp (WCAG AAA compliance)
  - Color contrast: Minimum 4.5:1 for all critical UI elements

**Offline-First Architecture:**
- 100% core functionality without internet
- Maximum cold-start time: 2 seconds
- Database size: < 8MB for initial installation (increased to accommodate visual assets)
- Asset optimization: All images < 100KB (WebP format)
- Video tutorials: Compressed to <2MB per clip

**7-Day Data Recycling Strategy:**
- **Day 0-6:** Fully offline operation using cached data
- **Day 7 (Sync Day):** Background sync when internet detected
  - Update market prices for 20+ feed ingredients
  - Refresh nutritional values for seasonal grasses
  - Download new visual tips/tutorials
  - Clear old analytics data (>30 days)
  - Sync vaccination reminders
- **Sync Method:** WorkManager periodic task (flexible 7-day window)

**Localization Strategy:**
- Phase 1: English + Hindi + Kannada (Devanagari script)
- Phase 2: Marathi, Punjabi, Tamil, Telugu, Gujarati
- Phase 3: Bengali, Odia, Malayalam
- RTL support: Reserved for future Urdu implementation
- Numeral system: International (0-9) with optional Devanagari overlay
- Audio support: Text-to-speech in all supported languages

---

## 2. TECHNICAL ARCHITECTURE

### 2.1 Technology Stack

**Frontend Layer:**
```
- UI Framework: Jetpack Compose 1.6.x
- Navigation: Compose Navigation 2.7.x
- State Management: ViewModel + StateFlow
- Dependency Injection: Hilt 2.50
- Image Loading: Coil 2.5.x (with WebP decoder)
- Animations: Compose Animation APIs
- Material Design: Material 3 (Material You theming)
```

**Data Layer:**
```
- Local Database: Room 2.6.x (SQLite wrapper)
- Data Serialization: Kotlinx Serialization 1.6.x
- Preferences: DataStore (Preferences + Proto)
- File Storage: Internal storage for images/videos
- Background Tasks: WorkManager 2.9.x (for 7-day sync)
```

**Logic Layer:**
```
- Language: Kotlin 1.9.x
- Coroutines: Kotlinx Coroutines 1.7.x
- Date/Time: Kotlinx DateTime
- Math Library: Custom nutrition calculation engine
- BMI Calculator: Custom algorithm for cattle health scoring
```

**Analytics & Visualization:**
```
- Charts: MPAndroidChart 3.1.0 (for savings graphs)
- Custom Composables: Visual health meters, pregnancy trackers
- Crash Reporting: Firebase Crashlytics (optional, offline-compatible)
- Usage Analytics: Local logging only (GDPR/privacy compliant)
```

**AI/ML Layer:**
```
- Primary: Gemini Nano (on-device, Android 14+)
- Fallback: Gemini API 1.5 Flash (requires internet)
- Prompt Engineering: Custom templates stored in Room DB
- Response Caching: 7-day cache for repeated queries
- Smart Features: Ingredient explanation, health tips, grazing recommendations
```

### 2.2 Architecture Pattern

**Clean Architecture Implementation:**
```
app/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   ├── CowProfileDao.kt
│   │   │   ├── IngredientDao.kt
│   │   │   ├── RecipeDao.kt
│   │   │   ├── VaccinationDao.kt (NEW)
│   │   │   ├── GrazingDataDao.kt (NEW)
│   │   │   └── SyncCacheDao.kt (NEW)
│   │   ├── entities/
│   │   │   ├── CowProfileEntity.kt (UPDATED)
│   │   │   ├── IngredientEntity.kt
│   │   │   ├── FeedRecipeEntity.kt
│   │   │   ├── VaccinationRecordEntity.kt (NEW)
│   │   │   ├── GrazingGrassEntity.kt (NEW)
│   │   │   ├── FarmerProfileEntity.kt (NEW)
│   │   │   └── SyncMetadataEntity.kt (NEW)
│   │   └── database/
│   │       └── PashuAaharDatabase.kt
│   ├── repository/
│   │   ├── CowRepository.kt
│   │   ├── NutritionRepository.kt
│   │   ├── VaccinationRepository.kt (NEW)
│   │   ├── GrazingRepository.kt (NEW)
│   │   └── SyncRepository.kt (NEW)
│   └── models/
│       ├── FarmerLevel.kt (NEW)
│       ├── CattleBMI.kt (NEW)
│       └── PregnancyStatus.kt (NEW)
├── domain/
│   ├── usecases/
│   │   ├── CalculateFeedRecipeUseCase.kt
│   │   ├── CalculateBMIUseCase.kt (NEW)
│   │   ├── GetGrazingRecommendationUseCase.kt (NEW)
│   │   ├── SyncDataUseCase.kt (NEW)
│   │   └── GetVaccinationScheduleUseCase.kt (NEW)
│   ├── models/
│   │   ├── CowProfile.kt (UPDATED)
│   │   ├── FeedRecipe.kt
│   │   ├── GrazingRecommendation.kt (NEW)
│   │   └── VaccinationSchedule.kt (NEW)
│   └── repository/
│       └── (interfaces for repositories)
├── presentation/
│   ├── screens/
│   │   ├── onboarding/
│   │   │   ├── LanguageSelectionScreen.kt
│   │   │   ├── FarmerLevelSelectionScreen.kt (NEW)
│   │   │   └── OnboardingCarouselScreen.kt
│   │   ├── profile/
│   │   │   ├── CowProfileStepperScreen.kt (UPDATED)
│   │   │   ├── BMIInputScreen.kt (NEW)
│   │   │   ├── PregnancyTrackerScreen.kt (NEW)
│   │   │   └── LactationInputScreen.kt (UPDATED)
│   │   ├── recipe/
│   │   │   ├── FeedRecipeDashboard.kt
│   │   │   ├── GrazingLibraryScreen.kt (NEW)
│   │   │   └── VisualFeedBucketScreen.kt (NEW)
│   │   ├── comparison/
│   │   │   └── SavingsComparisonScreen.kt
│   │   ├── health/
│   │   │   ├── VaccinationReminderScreen.kt (NEW)
│   │   │   ├── HealthDashboardScreen.kt (NEW)
│   │   │   └── BMITrackerScreen.kt (NEW)
│   │   ├── grazing/
│   │   │   ├── GrassIdentificationScreen.kt (NEW)
│   │   │   └── NutritionalValueScreen.kt (NEW)
│   │   └── genai/
│   │       └── AIExplanationBottomSheet.kt
│   ├── components/
│   │   ├── VisualSlider.kt (NEW - animated slider)
│   │   ├── BreedImageCard.kt
│   │   ├── BMIMeter.kt (NEW - visual health indicator)
│   │   ├── PregnancyProgressIndicator.kt (NEW)
│   │   ├── GrassCard.kt (NEW)
│   │   └── VaccinationCard.kt (NEW)
│   ├── theme/
│   │   ├── Color.kt (UPDATED - farmer-friendly palette)
│   │   ├── Type.kt (UPDATED - larger base sizes)
│   │   └── Theme.kt
│   └── navigation/
│       └── NavGraph.kt (UPDATED)
└── di/ 
    ├── DatabaseModule.kt
    ├── RepositoryModule.kt
    ├── UseCaseModule.kt
    └── WorkManagerModule.kt (NEW)
```

### 2.3 Offline-First Strategy

**Data Synchronization Rules:**
```
Priority 1 (Never Sync - 100% Local):
- Cow profiles (breed, age, weight, BMI)
- Pregnancy tracking data
- Custom ingredient prices set by user
- Saved recipes
- Vaccination records

Priority 2 (7-Day Sync - When Online):
- Market prices for pre-defined ingredients
- Nutritional values for seasonal grasses
- Regional grazing recommendations
- GenAI prompt templates
- App version updates

Priority 3 (Optional Sync - User Triggered):
- Veterinary tips library
- Video tutorials
- GenAI conversation history
- Usage statistics (anonymized)

Priority 4 (Cloud Backup - User Opt-in):
- Full app data export (encrypted)
- Cross-device profile transfer
- Multi-farm data (for large scale farmers)
```

**7-Day Sync Implementation:**
```kotlin
// WorkManager Periodic Task
class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            if (isNetworkAvailable()) {
                // 1. Update ingredient prices
                syncIngredientPrices()
                
                // 2. Refresh nutritional database
                syncNutritionalValues()
                
                // 3. Download new grazing recommendations
                syncGrazingData()
                
                // 4. Update vaccination schedules
                syncVaccinationSchedules()
                
                // 5. Clean old cache (>7 days)
                cleanExpiredCache()
                
                // 6. Update last sync timestamp
                updateSyncMetadata()
                
                Result.success()
            } else {
                Result.retry() // Retry when network available
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private suspend fun cleanExpiredCache() {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        database.genAICacheDao().deleteOlderThan(sevenDaysAgo)
        database.analyticsDao().deleteOlderThan(sevenDaysAgo)
    }
}

// Schedule in Application class
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "DataSync7Day",
    ExistingPeriodicWorkPolicy.KEEP,
    PeriodicWorkRequestBuilder<DataSyncWorker>(7, TimeUnit.DAYS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        )
        .build()
)
```

**Offline GenAI Handling:**
```kotlin
suspend fun getIngredientExplanation(
    ingredientId: String,
    cowProfile: CowProfile
): Result<String> {
    return when {
        // Check cache first (7-day validity)
        hasCachedResponse(ingredientId) -> {
            Result.Success(getCachedResponse(ingredientId))
        }
        
        // Try on-device AI (Android 14+)
        isGeminiNanoAvailable() -> {
            geminiNano.generateExplanation(
                ingredient = ingredientId,
                breed = cowProfile.breed,
                lactationStage = cowProfile.lactationStage,
                farmerLevel = userPreferences.farmerLevel
            ).also { response ->
                cacheResponse(ingredientId, response, expiryDays = 7)
            }
        }
        
        // Fallback to cloud AI (if online)
        isNetworkAvailable() -> {
            geminiApi.generateExplanation(ingredientId).also {
                cacheResponse(ingredientId, it, expiryDays = 7)
            }
        }
        
        // Last resort: static explanation
        else -> {
            Result.Success(getStaticExplanation(ingredientId))
        }
    }
}
```

---

## 3. SCREEN-BY-SCREEN SPECIFICATIONS (70:30 VISUAL DESIGN)

### 3.1 Screen 1: Onboarding & Farmer Level Selection

**File:** `FarmerLevelSelectionScreen.kt`

**Visual Composition:**
- **70% Visual:** Three large illustrative cards (280dp × 200dp each)
- **30% Text:** Card titles + short descriptions

**AI Builder Prompt:**
```
Create an Android Jetpack Compose screen for farmer level selection with these exact specifications:

LAYOUT:
- Top padding: 32dp
- Centered title: "Welcome to Pashu-Aahar" (28sp, Bold)
  Icon: 🐄 emoji (32dp) next to title
- Subtitle: "What type of farmer are you?" (18sp, Regular, 70% opacity)
- Vertical spacing between cards: 16dp

CARD 1 - BEGINNER FARMER:
- Dimensions: 280dp width × 200dp height
- Background: Gradient (Light Green #C8E6C9 to White)
- Border: 2dp solid #4CAF50 when selected
- Top Section (140dp height):
  - High-quality illustration: Single cow in a small shed
  - Image style: Flat design, warm colors
- Bottom Section (60dp height):
  - Title: "Beginner" / "शुरुआती" (22sp, SemiBold)
  - Icon: 🌱 (24dp)
  - Description: "1-2 cows" (14sp, 60% opacity)
- Tap behavior: Scale animation (0.95x), ripple effect

CARD 2 - INTERMEDIATE FARMER:
- Dimensions: 280dp width × 200dp height
- Background: Gradient (Light Blue #BBDEFB to White)
- Border: 2dp solid #2196F3 when selected
- Top Section:
  - Illustration: 3-4 cows in a medium shed with fodder storage
- Bottom Section:
  - Title: "Intermediate" / "मध्यम" (22sp, SemiBold)
  - Icon: 📊 (24dp)
  - Description: "3-10 cows" (14sp)

CARD 3 - LARGE SCALE FARMER:
- Dimensions: 280dp width × 200dp height
- Background: Gradient (Light Orange #FFE0B2 to White)
- Border: 2dp solid #FF9800 when selected
- Top Section:
  - Illustration: Modern dairy farm with 10+ cows, milking machines
- Bottom Section:
  - Title: "Large Scale" / "बड़े पैमाने" (22sp, SemiBold)
  - Icon: 🏭 (24dp)
  - Description: "10+ cows" (14sp)

BOTTOM NAVIGATION:
- "Next" button: Full width, 56dp height, Primary color
- Disabled state: 40% opacity if no selection
- Enabled state: Pulsing animation

ACCESSIBILITY:
- Content descriptions for all images
- Minimum touch target: 56×56 dp for cards
- Haptic feedback on selection
```

**State Management:**
```kotlin
data class FarmerLevelState(
    val selectedLevel: FarmerLevel? = null,
    val isLoading: Boolean = false
)

enum class FarmerLevel {
    BEGINNER,    // 1-2 cows
    INTERMEDIATE, // 3-10 cows
    LARGE_SCALE   // 10+ cows
}
```

---

### 3.2 Screen 2: Language Selection

**File:** `LanguageSelectionScreen.kt`

**Visual Composition:**
- **70% Visual:** Flag icons + illustrated cards
- **30% Text:** Language names only

**AI Builder Prompt:**
```
Create a Jetpack Compose language selection screen:

LAYOUT:
- Background: Gradient (Sky Blue to White)
- Title with icon: 🌍 "Choose Language" (26sp)

LANGUAGE CARDS (3 large cards, vertically scrollable):
Card 1 - English:
  - Flag: 🇬🇧 (64dp, centered)
  - Text: "English" (24sp, centered below flag)
  - Background: #E3F2FD
  - Border: 3dp solid #2196F3 when selected
  
Card 2 - Hindi:
  - Flag: 🇮🇳 (64dp)
  - Text: "हिंदी" (24sp, Noto Sans Devanagari)
  - Background: #FFF3E0
  - Border: 3dp solid #FF9800 when selected
  
Card 3 - Kannada:
  - Flag: 🇮🇳 (64dp)
  - Text: "ಕನ್ನಡ" (24sp, Noto Sans Kannada)
  - Background: #F3E5F5
  - Border: 3dp solid #9C27B0 when selected

INTERACTION:
- Card dimensions: 320dp × 140dp
- Elevation: 4dp, increases to 8dp when pressed
- Ripple color: Matches border color
- Audio playback: When card is tapped, play "Hello"/"नमस्ते"/"ನಮಸ್ಕಾರ" in that language

PERSISTENCE:
- Save selection to DataStore immediately
- Show checkmark (✓) icon in top-right of selected card
```

---

### 3.3 Screen 3: Cow Profile Builder (Visual Stepper)

**File:** `CowProfileStepperScreen.kt`

**Overall Design:**
- **Progress Bar:** Visual step indicator at top (5 steps)
- **70:30 Rule:** Each step uses visual sliders, images, icons
- **Navigation:** Large "Back" and "Next" buttons (56dp height)

#### **Step 1: Breed Selection**

**Visual Composition:**
- **100% Visual:** High-quality cow breed photographs

**AI Builder Prompt:**
```
Create a breed selection step with these specifications:

HEADER:
- Progress bar: 1/5 steps completed (20%)
- Title: "What breed?" / "कौन सी नस्ल?" (24sp)
- Icon: 🐄 (32dp)

BREED CARDS (2-column grid):
Each card: 160dp × 180dp

Card 1 - Jersey:
  - Photo: Jersey cow (full card background, 140dp height)
  - Overlay (bottom 40dp, semi-transparent black):
    - Text: "Jersey" (20sp, White, Bold)
    - Icon: ✓ if selected

Card 2 - Holstein Friesian (HF):
  - Photo: HF cow
  - Overlay: "HF" / "एचएफ"

Card 3 - Gir:
  - Photo: Gir cow (humped, Indian breed)
  - Overlay: "Gir" / "गिर"

Card 4 - Sahiwal:
  - Photo: Sahiwal cow
  - Overlay: "Sahiwal" / "साहीवाल"

Card 5 - Buffalo:
  - Photo: Murrah buffalo
  - Overlay: "Buffalo" / "भैंस"

INTERACTION:
- Single selection only
- Selected card: 3dp green border, scale 1.05x
- Haptic feedback on selection
```

**Data Model:**
```kotlin
enum class CattleBreed(val localizedName: Map<Language, String>) {
    JERSEY(mapOf(
        Language.ENGLISH to "Jersey",
        Language.HINDI to "जर्सी",
        Language.KANNADA to "ಜರ್ಸಿ"
    )),
    HOLSTEIN_FRIESIAN(mapOf(
        Language.ENGLISH to "Holstein Friesian",
        Language.HINDI to "होल्स्टीन फ्रिजियन"
    )),
    // ... other breeds
}
```

#### **Step 2: Age & Lactation Input**

**Visual Composition:**
- **70% Visual:** Illustrated age wheel + lactation icons
- **30% Text:** Labels only

**AI Builder Prompt:**
```
Create an age and lactation input step:

HEADER:
- Progress: 2/5 steps (40%)
- Title: "How old?" / "कितनी उम्र?" (24sp)
- Icon: 📅

AGE SECTION (Top half of screen):
- Visual: Circular age wheel (200dp diameter)
  - Center: Cow silhouette that "ages" as value changes
    - 6-12 months: Small calf illustration
    - 12-36 months: Young cow
    - 36+ months: Mature cow
  - Outer ring: Segmented circle showing years (1-15 years)
  - Touch-drag or tap to select
- Selected value display: Large text (32sp, Bold)
  - "2 years 6 months" / "2 साल 6 महीने"

LACTATION SECTION (Bottom half):
- Title: "Lactation stage?" / "दुग्धावस्था?" (20sp)
- Four visual cards (horizontal scroll):
  
  Card 1 - Early/Fresh:
    - Icon: 🍼 Full milk bottle (48dp)
    - Text: "Early" / "प्रारंभिक" (18sp)
    - Subtext: "0-100 days" (14sp, 60% opacity)
    - Background: Light green
    
  Card 2 - Mid/Peak:
    - Icon: 🥛 Overflowing milk (48dp)
    - Text: "Peak" / "चरम" (18sp)
    - Subtext: "100-200 days"
    - Background: Light blue
    
  Card 3 - Late:
    - Icon: 🍶 Half-full bottle (48dp)
    - Text: "Late" / "अंतिम" (18sp)
    - Subtext: "200-305 days"
    - Background: Light orange
    
  Card 4 - Dry:
    - Icon: 🚫 Empty bottle with cross (48dp)
    - Text: "Dry" / "सूखी" (18sp)
    - Subtext: "No milk"
    - Background: Light gray

INTERACTION:
- Age wheel: Smooth rotation with haptic ticks
- Lactation cards: Single selection, elevation animation
```

#### **Step 3: BMI & Weight Input (NEW)**

**Visual Composition:**
- **80% Visual:** Animated cow silhouette that changes appearance
- **20% Text:** Weight value only

**AI Builder Prompt:**
```
Create a visual BMI/weight input screen:

HEADER:
- Progress: 3/5 steps (60%)
- Title: "Cow's weight?" / "गाय का वजन?" (24sp)
- Icon: ⚖️

VISUAL WEIGHT METER (Main element, 70% of screen):
- Horizontal slider: 200kg to 800kg
- Visual feedback: Cow silhouette changes in real-time
  
  Weight ranges with visual representation:
  200-300kg: Thin cow (visible ribs, narrow body)
    - Silhouette color: Yellow-orange (caution)
    - Label: "Underweight" / "कम वजन"
    
  300-500kg: Healthy cow (normal proportions)
    - Silhouette color: Green
    - Label: "Healthy" / "स्वस्थ"
    - Animated sparkles around silhouette
    
  500-650kg: Well-fed cow (fuller body)
    - Silhouette color: Light green
    - Label: "Well-fed" / "अच्छी तरह खिलाया"
    
  650-800kg: Overweight cow (wide body)
    - Silhouette color: Orange-red
    - Label: "Overweight" / "अधिक वजन"

SLIDER DESIGN:
- Track height: 12dp
- Thumb: 56dp circle with ⚖️ icon
- Active track color: Matches silhouette color
- Step size: 10kg (for smooth animation)

WEIGHT DISPLAY:
- Large centered text: "450 kg" (40sp, Bold)
- Below slider, updates in real-time
- Optional audio: Speaks weight when thumb is released

BMI INDICATOR (Bottom card):
- Small card (320dp × 80dp)
- Background color: Matches health status
- Text: "BMI: Normal for Jersey cow" / "बीएमआई: सामान्य"
- Icon: ✓ or ⚠️
```

**BMI Calculation Logic:**
```kotlin
data class CattleBMI(
    val weight: Int, // in kg
    val breed: CattleBreed,
    val age: Int, // in months
    val status: BMIStatus
)

enum class BMIStatus {
    UNDERWEIGHT,
    HEALTHY,
    WELL_FED,
    OVERWEIGHT
}

fun calculateBMI(weight: Int, breed: CattleBreed, age: Int): BMIStatus {
    val idealWeight = when (breed) {
        CattleBreed.JERSEY -> 400..500
        CattleBreed.HOLSTEIN_FRIESIAN -> 500..650
        CattleBreed.GIR -> 350..450
        CattleBreed.SAHIWAL -> 400..500
        CattleBreed.BUFFALO -> 500..700
    }
    
    // Adjust for age
    val ageAdjustedIdeal = if (age < 36) {
        idealWeight.first - 50..idealWeight.last - 50
    } else {
        idealWeight
    }
    
    return when {
        weight < ageAdjustedIdeal.first - 50 -> BMIStatus.UNDERWEIGHT
        weight in ageAdjustedIdeal -> BMIStatus.HEALTHY
        weight in ageAdjustedIdeal.last..(ageAdjustedIdeal.last + 100) -> BMIStatus.WELL_FED
        else -> BMIStatus.OVERWEIGHT
    }
}
```

#### **Step 4: Pregnancy Tracking (NEW)**

**Visual Composition:**
- **90% Visual:** Pregnancy progress illustration
- **10% Text:** Month labels

**AI Builder Prompt:**
```
Create a visual pregnancy tracking input:

HEADER:
- Progress: 4/5 steps (80%)
- Title: "Is she pregnant?" / "क्या गर्भवती है?" (24sp)
- Icon: 🤰

MAIN TOGGLE (Top section):
- Two large visual buttons (160dp × 160dp each, side by side):
  
  Button 1 - NOT PREGNANT:
    - Icon: Cow silhouette without calf (80dp)
    - Background: Light gray
    - Text below: "No" / "नहीं" (20sp)
    
  Button 2 - PREGNANT:
    - Icon: Cow silhouette WITH visible calf inside (80dp)
    - Background: Light pink
    - Text below: "Yes" / "हाँ" (20sp)

PREGNANCY PROGRESS (Visible only if "Yes" selected):
- Visual timeline (horizontal, 9 segments):
  - Each segment: Circular icon (48dp) representing a month
  - Icons progressively show calf development:
    - Month 1-2: Small dot
    - Month 3-4: Tiny calf outline
    - Month 5-6: Calf with visible features
    - Month 7-8: Larger calf
    - Month 9: Full-sized calf, almost ready
    
- Selection method: Tap on month circle
- Selected month: Glowing animation, 1.2x scale
- Line connecting circles: Fills progressively (green color)

NUTRITIONAL ALERT (Bottom card):
- Visible when month 7-9 selected:
  - Icon: ⚠️ (32dp)
  - Background: Light yellow
  - Text: "Extra nutrition needed!" / "अतिरिक्त पोषण चाहिए!" (18sp, SemiBold)
  - Subtext: "Feed recipe will adjust automatically"

INTERACTION:
- Smooth reveal animation when "Yes" is selected
- Pregnancy timeline scrolls horizontally if needed
- Haptic feedback when month is selected
```

**Data Model:**
```kotlin
data class PregnancyStatus(
    val isPregnant: Boolean,
    val monthOfPregnancy: Int? = null, // 1-9
    val expectedCalvingDate: LocalDate? = null,
    val requiresExtraNutrition: Boolean = false
) {
    init {
        require(monthOfPregnancy in 1..9 || monthOfPregnancy == null)
        requiresExtraNutrition = (monthOfPregnancy ?: 0) >= 7
    }
}
```

#### **Step 5: Milk Yield Input**

**Visual Composition:**
- **70% Visual:** Animated milk bucket filling
- **30% Text:** Liter value

**AI Builder Prompt:**
```
Create a milk yield input with visual feedback:

HEADER:
- Progress: 5/5 steps (100%)
- Title: "Daily milk?" / "दैनिक दूध?" (24sp)
- Icon: 🥛

VISUAL MILK BUCKET (Center of screen):
- Illustration: Steel milk bucket (250dp height)
- Bucket fills with white "milk" as slider moves
- Fill levels:
  - 0L: Empty bucket
  - 1-10L: 25% filled
  - 11-20L: 50% filled (animated ripples)
  - 21-30L: 75% filled
  - 31-40L: 100% filled (overflow drips)
  
- Bucket exterior shows measurement lines (5L increments)

SLIDER (Below bucket):
- Range: 0-40 liters
- Step size: 0.5L (for precision)
- Thumb: Milk droplet icon (48dp)
- Active color: #FFFFFF (white)
- Track: Light blue (milk container color)

YIELD DISPLAY:
- Above bucket: Large text "15.5 L" (48sp, Bold)
- Animated counter (increments smoothly)

BREED-SPECIFIC GUIDANCE (Bottom card):
- Shows typical yield for selected breed:
  - "Jersey cows typically give 15-25 L/day" / "जर्सी गाय आमतौर पर 15-25 लीटर/दिन देती है"
  - Icon: ℹ️ (24dp)
  - Background: Light blue (#E3F2FD)
  
SPECIAL CASE - DRY COWS:
- If lactation stage = "Dry", slider is locked at 0L
- Bucket shows: 🚫 icon with "No milk production" text
- Light gray overlay on bucket

COMPLETION ANIMATION:
- When "Next" is pressed: Confetti animation 🎉
- Success message: "Profile complete!" / "प्रोफ़ाइल पूर्ण!" 
- Auto-navigate to Recipe Dashboard after 2 seconds
```

**Validation Logic:**
```kotlin
fun validateMilkYield(
    yield: Float,
    breed: CattleBreed,
    lactationStage: LactationStage
): ValidationResult {
    // Dry cows cannot produce milk
    if (lactationStage == LactationStage.DRY && yield > 0) {
        return ValidationResult.Error("Dry cows don't produce milk")
    }
    
    // Breed-specific max limits
    val maxYield = when (breed) {
        CattleBreed.JERSEY -> 30f
        CattleBreed.HOLSTEIN_FRIESIAN -> 40f
        CattleBreed.GIR -> 15f
        CattleBreed.SAHIWAL -> 20f
        CattleBreed.BUFFALO -> 18f
    }
    
    if (yield > maxYield) {
        return ValidationResult.Warning("Unusually high for ${breed.name}")
    }
    
    return ValidationResult.Success
}
```

---

### 3.4 Screen 4: Feed Recipe Dashboard (Visual Output)

**File:** `FeedRecipeDashboard.kt`

**Visual Composition:**
- **75% Visual:** Ingredient icons, charts, visual meters
- **25% Text:** Quantities and labels

**AI Builder Prompt:**
```
Create a feed recipe dashboard with heavy visual emphasis:

HEADER (Collapsible):
- Cow profile summary card (expandable):
  - Left: Circular cow photo/avatar (64dp)
  - Center: Name + Breed (20sp, Bold)
  - Right: Edit icon button (48dp)
  - Background: Gradient matching breed color
  - Tap to expand: Shows BMI meter, pregnancy status, lactation stage (all visual)

FEED CATEGORIES (Tab Row):
- Three visual tabs:
  - 🌾 "Green Fodder" / "हरा चारा"
  - 🌿 "Dry Fodder" / "सूखा चारा"
  - 🥜 "Concentrate" / "सांद्र आहार"
- Active tab: Underline + icon glow effect

INGREDIENT GRID (2-column LazyVerticalGrid):
Each card: 160dp × 200dp

Card Design:
- Top 60%: High-quality ingredient photo
  - Examples: Napier grass, wheat straw, maize
- Bottom 40%:
  - Icon badge (32dp) in top-right corner (category indicator)
  - Ingredient name (16sp, 2 lines max)
  - Required quantity: LARGE text "5.2 KG" (24sp, Bold)
  - Price: "₹26" (16sp, green color)
  
- Visual quantity meter:
  - Horizontal bar showing % of total DMI
  - Color-coded: Green (fodder), Brown (dry), Yellow (concentrate)

TAP INTERACTION:
- Opens bottom sheet with:
  - Full-screen ingredient photo (200dp)
  - AI-generated explanation (60 words max, 7th-grade reading level)
  - 3 visual "Nutrition Badges":
    - 🟢 Protein: Visual meter (1-5 dots)
    - 🟡 Energy: Visual meter (1-5 dots)
    - 💰 Cost: Rupee icons (₹ to ₹₹₹₹₹)
  - "Seasonal availability" icon (🌞 Summer, 🌧️ Monsoon, ❄️ Winter)

FOOTER (Always visible):
- Total daily cost card (full width, 80dp height):
  - Icon: 💰 (40dp)
  - Text: "Total: ₹145/day" (28sp, Bold)
  - Subtext: "₹4,350/month" (16sp, 60% opacity)
  - Background: Gradient (green to light green)

LOADING STATES:
- Shimmer effect on ingredient cards while calculating
- Skeleton screens with pulse animation

EMPTY STATE:
- Full-screen illustration: Empty feed bucket (200dp)
- Text: "Add a cow profile to get started" / "आरंभ करने के लिए गाय प्रोफ़ाइल जोड़ें"
- CTA button: "Add Cow" (large, 56dp height)

ERROR STATE:
- Icon: ⚠️ (80dp, animated shake)
- Text: "Couldn't calculate recipe" / "नुस्खा की गणना नहीं हो सकी"
- Retry button with refresh icon
```

**Component Code Structure:**
```kotlin
@Composable
fun FeedRecipeDashboard(
    cowProfile: CowProfile,
    feedRecipe: FeedRecipe,
    onIngredientClick: (Ingredient) -> Unit,
    onEditProfile: () -> Unit,
    viewModel: FeedRecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = { CowProfileHeader(cowProfile, onEditProfile) },
        bottomBar = { TotalCostFooter(feedRecipe.totalCost) }
    ) { padding ->
        when (uiState) {
            is UiState.Loading -> ShimmerIngredientGrid()
            is UiState.Success -> {
                Column {
                    FeedCategoryTabs(...)
                    IngredientGrid(
                        ingredients = uiState.ingredients,
                        onClick = onIngredientClick
                    )
                }
            }
            is UiState.Empty -> EmptyState()
            is UiState.Error -> ErrorState()
        }
    }
}

@Composable
fun IngredientCard(
    ingredient: Ingredient,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Ingredient photo (70% of card height)
            AsyncImage(
                model = ingredient.imageUrl,
                contentDescription = ingredient.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            
            // Quantity and price (30% of card height)
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = ingredient.getLocalizedName(),
                    fontSize = 16.sp,
                    maxLines = 2
                )
                
                Text(
                    text = "${ingredient.requiredQuantity} KG",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("₹${ingredient.cost}", color = Color.Green)
                    Icon(Icons.Default.Info, "Details")
                }
                
                // Visual quantity meter
                LinearProgressIndicator(
                    progress = ingredient.percentOfDMI,
                    modifier = Modifier.fillMaxWidth(),
                    color = ingredient.category.color
                )
            }
        }
    }
}
```

---

### 3.5 Screen 5: Grazing & Grass Library (NEW)

**File:** `GrazingLibraryScreen.kt`

**Visual Composition:**
- **80% Visual:** High-resolution grass photographs + nutritional charts
- **20% Text:** Names and values

**AI Builder Prompt:**
```
Create a visual grass identification and nutrition library: language in kannada english

HEADER:
- Title: "Grazing Guide" / "चराई गाइड" (28sp)
- Icon: 🌾 (32dp)
- Subtitle: "Know your grasses" / "अपनी घास को जानें"

GRASS TYPE SELECTOR (Horizontal chips):
- Chip 1: 🌿 "All Grass" (selected by default)
- Chip 2: 🟢 "Green/Fresh"
- Chip 3: 🟡 "Dry Fodder"
- Chip 4: ⭐ "High Protein"
- Filter animation when selected

GRASS CARDS (Full-width cards, vertical scroll):
Each card: Full width × 240dp height

Card Structure (HORIZONTAL layout):
- Left 50%: Full-height grass photo
  - Examples: 
    - Napier grass (tall, green)
    - Berseem (clover-like)
    - Lucerne (alfalfa)
    - Wheat straw (golden-yellow)
    - Paddy straw (dry, brown)
    
- Right 50%: Information panel
  - Grass name: Bilingual (20sp, Bold)
    - "Napier Grass" / "नेपियर घास"
  - Category badge: "Green Fodder" with 🟢 icon
  
  - VISUAL NUTRITIONAL METERS (3 horizontal bars):
    1. Protein Content:
       - Icon: 💪
       - Visual bar (color: Blue, 0-100%)
       - Value: "8.5% CP"
    
    2. Energy (TDN):
       - Icon: ⚡
       - Visual bar (color: Yellow, 0-100%)
       - Value: "65% TDN"
    
    3. Dry Matter:
       - Icon: 💧
       - Visual bar (color: Brown, 0-100%)
       - Value: "20% DM"
  
  - Seasonal availability:
    - Icons row: 🌧️ ☀️ ❄️ (monsoon, summer, winter)
    - Active seasons: Full color
    - Inactive: 30% opacity
  
  - Market price:
    - Icon: 💰
    - Text: "₹3-5/kg" (18sp, Green)

TAP INTERACTION:
- Expands card to show:
  - "How to identify" visual guide (3-4 photos)
  - "Best for" cow types (visual icons)
  - "Feeding tips" with audio narration
  - "Mix with" suggestions (related grass cards)

FLOATING ACTION BUTTON (Bottom-right):
- Icon: 📷 "Identify Grass"
- Opens camera to take photo
- Uses image recognition (future feature)
  - Fallback: Shows all grass types for manual selection

COMPARISON MODE:
- Long-press on card to enter comparison mode
- Select up to 3 grasses
- Bottom sheet shows side-by-side nutritional comparison (visual charts)
```

**Data Model:**
```kotlin
data class GrazingGrass(
    val id: String,
    val name: Map<Language, String>,
    val category: GrassCategory,
    val imageUrls: List<String>, // Multiple angles
    val nutritionalValue: NutritionalValue,
    val seasonalAvailability: List<Season>,
    val priceRange: PriceRange,
    val idealFor: List<CattleBreed>,
    val mixingRecommendations: List<String> // IDs of complementary grasses
)

enum class GrassCategory(val icon: String, val color: Color) {
    GREEN_FODDER("🟢", Color(0xFF4CAF50)),
    DRY_FODDER("🟡", Color(0xFFFFC107)),
    LEGUME("🔵", Color(0xFF2196F3)),
    CONCENTRATE("🟠", Color(0xFFFF9800))
}

data class NutritionalValue(
    val crudeProtein: Float, // CP percentage
    val totalDigestibleNutrients: Float, // TDN percentage
    val dryMatter: Float, // DM percentage
    val calcium: Float,
    val phosphorus: Float,
    val fiber: Float
)

enum class Season {
    MONSOON, SUMMER, WINTER
}

// Pre-populated grazing data
val COMMON_GRASSES = listOf(
    GrazingGrass(
        id = "napier",
        name = mapOf(
            Language.ENGLISH to "Napier Grass",
            Language.HINDI to "नेपियर घास",
            Language.KANNADA to "ನೇಪಿಯರ್ ಹುಲ್ಲು"
        ),
        category = GrassCategory.GREEN_FODDER,
        nutritionalValue = NutritionalValue(
            crudeProtein = 8.5f,
            totalDigestibleNutrients = 65f,
            dryMatter = 20f,
            calcium = 0.4f,
            phosphorus = 0.25f,
            fiber = 32f
        ),
        seasonalAvailability = listOf(Season.MONSOON, Season.WINTER),
        priceRange = PriceRange(min = 3, max = 5),
        idealFor = listOf(CattleBreed.JERSEY, CattleBreed.HOLSTEIN_FRIESIAN)
    ),
    // ... more grasses
)
```

---

### 3.6 Screen 6: Visual Feed Bucket Result (NEW)

**File:** `VisualFeedBucketScreen.kt`

**Visual Composition:**
- **90% Visual:** Illustrated feed bucket with color-coded portions
- **10% Text:** Quantity labels

**AI Builder Prompt:**
```
Create a visual feed recommendation display using a bucket metaphor:

HEADER:
- Title: "Today's Feed" / "आज का चारा" (26sp)
- Subtitle: "For [Cow Name]" (18sp, 60% opacity)
- Icon: 🪣 (32dp)

MAIN VISUAL - FEED BUCKET (Center, 80% of screen):
- Illustration: Large steel bucket (300dp width × 350dp height)
- Bucket is divided into 3 horizontal colored sections (stacked):

  SECTION 1 (Bottom, 60% of bucket):
  - Color: Fresh green (#4CAF50)
  - Label overlay: "Green Fodder" / "हरा चारा"
  - Quantity: "18 KG" (28sp, Bold, White text)
  - Icon: 🌾 (40dp, top-right corner)
  - Visual elements: Illustrated grass blades peeking out
  
  SECTION 2 (Middle, 20% of bucket):
  - Color: Golden yellow (#FFC107)
  - Label: "Dry Fodder" / "सूखा चारा"
  - Quantity: "6 KG" (28sp, Bold)
  - Icon: 🌿 (40dp)
  - Visual: Straw texture pattern
  
  SECTION 3 (Top, 20% of bucket):
  - Color: Brown (#795548)
  - Label: "Concentrate" / "सांद्र आहार"
  - Quantity: "6 KG" (28sp, Bold, White text)
  - Icon: 🥜 (40dp)
  - Visual: Grain particles illustration

BUCKET HANDLE:
- Metallic appearance with shine effect
- Shows total weight: "30 KG" tag hanging from handle

FEEDING SCHEDULE (Below bucket):
- Two visual cards (side by side):
  
  Card 1 - Morning:
  - Icon: 🌅 (48dp)
  - Time: "6:00 AM" (20sp)
  - Amount: "15 KG" (24sp, Bold)
  - Visual mini-bucket (50% filled)
  
  Card 2 - Evening:
  - Icon: 🌆 (48dp)
  - Time: "6:00 PM" (20sp)
  - Amount: "15 KG" (24sp, Bold)
  - Visual mini-bucket (50% filled)

BREAKDOWN BUTTON (Bottom):
- Full-width button with icon: 📋 "See Detailed Recipe"
- Tap to navigate to full ingredient grid

SPECIAL INDICATORS:
- If cow is pregnant (month 7-9):
  - Yellow badge on bucket: ⚠️ "Extra nutrition added"
  - Extra minerals section (small, 5% at very top)
  
- If cow is dry:
  - Bucket is smaller (reduced concentrate section)
  - Icon: 🚫 overlayed on milk bottle

AUDIO FEATURE:
- Speaker button in top-right: 🔊
- Plays feeding instructions in selected language
- Example: "Feed 18 kilograms of green fodder, 6 kilograms of dry fodder..."

SHARE FEATURE:
- Screenshot button: 📸
- Generates shareable image with farm logo (if set)
- Text overlay: "Generated by Pashu-Aahar"
```

**Animation Details:**
```kotlin
@Composable
fun VisualFeedBucket(
    feedRecipe: FeedRecipe,
    cowProfile: CowProfile
) {
    var bucketAnimated by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        bucketAnimated = true
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bucket illustration
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(350.dp)
        ) {
            // Green fodder section (animates from bottom)
            AnimatedVisibility(
                visible = bucketAnimated,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                ) + fadeIn()
            ) {
                BucketSection(
                    color = Color(0xFF4CAF50),
                    heightFraction = 0.60f,
                    label = "Green Fodder",
                    quantity = "${feedRecipe.greenFodderKg} KG",
                    icon = "🌾"
                )
            }
            
            // Dry fodder section (delays by 200ms)
            AnimatedVisibility(
                visible = bucketAnimated,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500, delayMillis = 200)
                ) + fadeIn()
            ) {
                BucketSection(
                    color = Color(0xFFFFC107),
                    heightFraction = 0.20f,
                    label = "Dry Fodder",
                    quantity = "${feedRecipe.dryFodderKg} KG",
                    icon = "🌿",
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .offset(y = (-210).dp)
                )
            }
            
            // Concentrate section (delays by 400ms)
            AnimatedVisibility(
                visible = bucketAnimated,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500, delayMillis = 400)
                ) + fadeIn()
            ) {
                BucketSection(
                    color = Color(0xFF795548),
                    heightFraction = 0.20f,
                    label = "Concentrate",
                    quantity = "${feedRecipe.concentrateKg} KG",
                    icon = "🥜",
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            
            // Bucket handle and total weight tag
            BucketHandle(totalWeight = feedRecipe.totalKg)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Feeding schedule cards
        FeedingScheduleCards(...)
    }
}
```

---

### 3.7 Screen 7: Health & Vaccination Tracker (NEW)

**File:** `VaccinationReminderScreen.kt`

**Visual Composition:**
- **70% Visual:** Timeline with syringe icons, status colors
- **30% Text:** Vaccine names and dates

**AI Builder Prompt:**
```
Create a visual vaccination tracking screen:

HEADER:
- Title: "Cattle Health" / "पशु स्वास्थ्य" (26sp)
- Icon: 💉 (32dp)
- Subtitle: "Vaccination schedule" / "टीकाकरण कार्यक्रम"

COW SELECTOR (If multiple cows):
- Horizontal scrollable chips with cow photos
- Shows cow name + last vaccination date

VACCINATION TIMELINE (Vertical scroll):
- Visual timeline with connecting vertical line (4dp, dashed)
- Each vaccine entry:

CARD DESIGN (Full width × 120dp height):
- Left section (80dp):
  - Circular status indicator (64dp):
    - ✅ Green circle: "Done" / "पूर्ण"
    - ⏰ Yellow circle: "Due soon" (within 7 days)
    - ❌ Red circle: "Overdue"
  - Animated pulse effect for overdue vaccines
  
- Center section:
  - Vaccine name: "FMD Vaccine" / "एफएमडी टीका" (20sp, Bold)
  - Type: Visual badge "💉 Injection" or "💊 Oral"
  - Due date: Large text "15 Jan 2025" (18sp)
  - Days remaining: "In 5 days" (14sp, yellow for soon, red for overdue)
  
- Right section:
  - Syringe icon (48dp, color-coded by status)
  - "Mark Done" button (only for upcoming/overdue)

COMMON VACCINATIONS (Pre-populated):
1. FMD (Foot and Mouth Disease):
   - Icon: 💉
   - Frequency: Every 6 months
   - Description: "Protects against hoof disease"
   
2. HS (Hemorrhagic Septicemia):
   - Icon: 💉
   - Frequency: Annually
   
3. BQ (Black Quarter):
   - Icon: 💉
   - Frequency: Annually
   
4. Brucellosis:
   - Icon: 💉
   - Frequency: One-time (heifers)
   
5. Deworming:
   - Icon: 💊
   - Frequency: Every 3 months

ADD CUSTOM REMINDER:
- Floating Action Button: ➕
- Opens dialog:
  - Vaccine name input (text or select from common list)
  - Date picker (visual calendar)
  - Repeat frequency dropdown with icons
  - Notes field (optional)

NOTIFICATIONS:
- Local push notifications:
  - 7 days before: "Upcoming: FMD vaccine on 15 Jan"
  - On due date: "Today: FMD vaccine due for [Cow Name]"
  - 1 day overdue: "Overdue: FMD vaccine was due yesterday"
  
- Notification shows cow photo + vaccine icon

HISTORY VIEW:
- Toggle button: 📅 "View History"
- Shows completed vaccinations in chronological order
- Each entry: Date, vaccine name, batch number (if entered)

OFFLINE OPERATION:
- All reminders stored in Room DB
- AlarmManager schedules notifications
- No internet required
```

**Data Model:**
```kotlin
@Entity(tableName = "vaccinations")
data class VaccinationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cowId: Long,
    val vaccineName: String,
    val vaccineType: VaccineType,
    val dueDate: LocalDate,
    val completedDate: LocalDate? = null,
    val repeatFrequency: RepeatFrequency,
    val notes: String? = null,
    val status: VaccinationStatus,
    val batchNumber: String? = null
)

enum class VaccineType {
    INJECTION,
    ORAL,
    TOPICAL
}

enum class RepeatFrequency {
    ONE_TIME,
    EVERY_3_MONTHS,
    EVERY_6_MONTHS,
    ANNUALLY
}

enum class VaccinationStatus {
    UPCOMING,
    DUE_SOON, // Within 7 days
    OVERDUE,
    COMPLETED
}

// Pre-populated vaccination schedule based on cow's age
fun generateVaccinationSchedule(cow: CowProfile): List<VaccinationRecord> {
    val baseDate = LocalDate.now()
    return listOf(
        VaccinationRecord(
            cowId = cow.id,
            vaccineName = "FMD",
            vaccineType = VaccineType.INJECTION,
            dueDate = baseDate.plusMonths(6),
            repeatFrequency = RepeatFrequency.EVERY_6_MONTHS,
            status = VaccinationStatus.UPCOMING
        ),
        VaccinationRecord(
            cowId = cow.id,
            vaccineName = "HS",
            vaccineType = VaccineType.INJECTION,
            dueDate = baseDate.plusYears(1),
            repeatFrequency = RepeatFrequency.ANNUALLY,
            status = VaccinationStatus.UPCOMING
        ),
        // ... more vaccines
    )
}
```

---

### 3.8 Screen 8: Cost Comparison & Savings Chart

**File:** `SavingsComparisonScreen.kt`

**Visual Composition:**
- **65% Visual:** Charts, graphs, comparison cards
- **35% Text:** Cost values and labels

**AI Builder Prompt:**
```
Create a visual savings comparison dashboard:

HEADER:
- Title: "Your Savings" / "आपकी बचत" (28sp)
- Icon: 💰 (32dp)
- Subtitle: "vs. Market Feed" / "बाजार चारे की तुलना में"

TOP SECTION - COMPARISON CARDS (2 cards, side by side):

Card 1 - Your Recipe:
- Background: Gradient (Light Green to White)
- Icon: ✅ (40dp)
- Label: "Your Cost" / "आपकी लागत" (18sp)
- Amount: "₹145/day" (32sp, Bold, Green)
- Subtext: "Custom recipe" (14sp, 60% opacity)
- Visual: Downward trend arrow 📉

Card 2 - Market Feed:
- Background: Gradient (Light Red to White)
- Icon: 🏪 (40dp)
- Label: "Market Cost" / "बाजार लागत" (18sp)
- Amount: "₹210/day" (32sp, Bold, Red)
- Subtext: "Commercial feed" (14sp)
- Visual: Upward trend arrow 📈

SAVINGS HIGHLIGHT CARD (Full width, gradient background):
- Large icon: 🎉 (56dp)
- Text: "You save" / "आप बचाते हैं" (20sp)
- Amount: "₹65/day" (40sp, Bold, Green)
- Percentage: "31% less" (24sp, Badge with green background)
- Animation: Coin drop effect when screen loads

TIME PERIOD SELECTOR (Segmented control):
- 4 options (visual tabs):
  - 📅 "7 Days"
  - 📅 "30 Days"
  - 📅 "90 Days"
  - 📅 "1 Year"
- Selected tab: Filled background, scale 1.1x
- Shows projected savings for selected period:
  - 7 Days: "₹455"
  - 30 Days: "₹1,950"
  - 90 Days: "₹5,850"
  - 1 Year: "₹23,725"

SAVINGS CHART (MPAndroidChart LineChart):
- X-axis: Time (days/months based on selection)
- Y-axis: Cumulative savings (₹)
- Line color: Gradient (Light Green to Dark Green)
- Fill under line: Semi-transparent green
- Data points: Green circles (tap for exact value)
- Grid lines: Light gray, dashed
- Height: 250dp

CHART INTERACTION:
- Tap on data point: Shows popup tooltip
  - Date: "15 Jan"
  - Savings: "₹2,145"
  - Icon: 💰

BREAKDOWN SECTION (Expandable accordion):
- Title: "Where you save" / "आप कहाँ बचाते हैं" (20sp)
- 3 visual breakdown cards:
  
  Card 1 - Green Fodder:
  - Icon: 🌾 (32dp)
  - Savings: "₹30/day" (20sp, Green)
  - Bar chart: Your cost vs. market cost
  
  Card 2 - Dry Fodder:
  - Icon: 🌿 (32dp)
  - Savings: "₹15/day" (20sp, Green)
  
  Card 3 - Concentrate:
  - Icon: 🥜 (32dp)
  - Savings: "₹20/day" (20sp, Green)

ADDITIONAL BENEFITS (Visual cards):
- Card: "Better nutrition" - Icon: 💪
- Card: "Local ingredients" - Icon: 🏡
- Card: "Seasonal availability" - Icon: 🌱

SHARE FEATURE:
- Button: 📤 "Share Your Success"
- Generates shareable image/infographic:
  - "I save ₹65/day with Pashu-Aahar!"
  - Visual chart
  - App logo and QR code

EXPORT FEATURE:
- Button: 📊 "Export Report"
- Generates PDF with:
  - Month-wise savings breakdown
  - Ingredient-wise cost comparison
  - Visual charts and graphs
```

**Chart Implementation:**
```kotlin
@Composable
fun SavingsChart(
    savingsData: List<SavingsDataPoint>,
    timePeriod: TimePeriod
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                
                // Style
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.textSize = 12f
                
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(true)
                axisLeft.enableGridDashedLine(10f, 10f, 0f)
                axisLeft.textSize = 12f
                
                legend.isEnabled = false
                
                // Data
                val entries = savingsData.mapIndexed { index, data ->
                    Entry(index.toFloat(), data.cumulativeSavings.toFloat())
                }
                
                val dataSet = LineDataSet(entries, "Savings").apply {
                    color = Color.Green.toArgb()
                    setCircleColor(Color.Green.toArgb())
                    lineWidth = 3f
                    circleRadius = 5f
                    setDrawFilled(true)
                    fillColor = Color.Green.copy(alpha = 0.3f).toArgb()
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    
                    // Value text on points
                    valueTextSize = 10f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "₹${value.toInt()}"
                        }
                    }
                }
                
                data = LineData(dataSet)
                
                // Marker (popup on tap)
                marker = SavingsMarkerView(context)
                
                animateX(1000)
            }
        },
        update = { chart ->
            // Update data when period changes
            chart.data = generateLineData(savingsData)
            chart.invalidate()
        }
    )
}

data class SavingsDataPoint(
    val date: LocalDate,
    val dailySavings: Int,
    val cumulativeSavings: Int
)

enum class TimePeriod {
    SEVEN_DAYS,
    THIRTY_DAYS,
    NINETY_DAYS,
    ONE_YEAR
}
```

---

## 4. NAVIGATION GRAPH (UPDATED)

```kotlin
sealed class Screen(val route: String) {
    object LanguageSelection : Screen("language_selection")
    object FarmerLevelSelection : Screen("farmer_level_selection")
    object OnboardingCarousel : Screen("onboarding_carousel")
    object CowProfileStepper : Screen("cow_profile_stepper")
    object FeedRecipeDashboard : Screen("feed_recipe/{cowId}") {
        fun createRoute(cowId: Long) = "feed_recipe/$cowId"
    }
    object GrazingLibrary : Screen("grazing_library")
    object VisualFeedBucket : Screen("visual_feed_bucket/{cowId}") {
        fun createRoute(cowId: Long) = "visual_feed_bucket/$cowId"
    }
    object VaccinationReminder : Screen("vaccination_reminder/{cowId}") {
        fun createRoute(cowId: Long) = "vaccination_reminder/$cowId"
    }
    object SavingsComparison : Screen("savings_comparison/{cowId}") {
        fun createRoute(cowId: Long) = "savings_comparison/$cowId"
    }
    object EditProfile : Screen("edit_profile/{cowId}") {
        fun createRoute(cowId: Long) = "edit_profile/$cowId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                onLanguageSelected = {
                    navController.navigate(Screen.FarmerLevelSelection.route)
                }
            )
        }
        
        composable(Screen.FarmerLevelSelection.route) {
            FarmerLevelSelectionScreen(
                onLevelSelected = {
                    navController.navigate(Screen.OnboardingCarousel.route)
                }
            )
        }
        
        composable(Screen.OnboardingCarousel.route) {
            OnboardingCarouselScreen(
                onComplete = {
                    navController.navigate(Screen.CowProfileStepper.route) {
                        popUpTo(Screen.LanguageSelection.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.CowProfileStepper.route) {
            CowProfileStepperScreen(
                onProfileComplete = { cowId ->
                    navController.navigate(
                        Screen.FeedRecipeDashboard.createRoute(cowId)
                    ) {
                        popUpTo(Screen.CowProfileStepper.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.FeedRecipeDashboard.route,
            arguments = listOf(navArgument("cowId") { type = NavType.LongType })
        ) { backStackEntry ->
            val cowId = backStackEntry.arguments?.getLong("cowId") ?: 0
            FeedRecipeDashboard(
                cowId = cowId,
                onNavigateToGrazing = {
                    navController.navigate(Screen.GrazingLibrary.route)
                },
                onNavigateToVisualBucket = {
                    navController.navigate(Screen.VisualFeedBucket.createRoute(cowId))
                },
                onNavigateToHealth = {
                    navController.navigate(Screen.VaccinationReminder.createRoute(cowId))
                },
                onNavigateToSavings = {
                    navController.navigate(Screen.SavingsComparison.createRoute(cowId))
                }
            )
        }
        
        // ... other composable destinations
    }
}

// Deep linking support
val deepLinks = listOf(
    "pashuaahar://recipe/{cowId}",
    "pashuaahar://grazing",
    "pashuaahar://health/{cowId}",
    "pashuaahar://savings/{cowId}"
)
```

---

## 5. NUTRITION CALCULATION ENGINE (UPDATED)

### 5.1 Core Formulas

**DMI Calculation (Dry Matter Intake):**
```kotlin
fun calculateDMI(
    weightKg: Int,
    milkYieldLiters: Float,
    bmiStatus: BMIStatus,
    pregnancyStatus: PregnancyStatus
): Float {
    // Base DMI formula
    var dmi = (0.0185f * weightKg) + (0.305f * milkYieldLiters)
    
    // BMI adjustment
    dmi *= when (bmiStatus) {
        BMIStatus.UNDERWEIGHT -> 1.10f // +10% for underweight cows
        BMIStatus.HEALTHY -> 1.0f
        BMIStatus.WELL_FED -> 0.95f // -5% for well-fed cows
        BMIStatus.OVERWEIGHT -> 0.90f // -10% for overweight cows
    }
    
    // Pregnancy adjustment (last trimester needs extra)
    if (pregnancyStatus.isPregnant && (pregnancyStatus.monthOfPregnancy ?: 0) >= 7) {
        dmi *= 1.15f // +15% for late pregnancy
    }
    
    return dmi
}
```

**TDN Calculation (Total Digestible Nutrients):**
```kotlin
fun calculateTDN(
    weightKg: Int,
    milkYieldLiters: Float,
    milkFatPercent: Float,
    lactationStage: LactationStage,
    pregnancyStatus: PregnancyStatus
): Float {
    // Maintenance requirement
    val maintenanceTDN = 0.035f * weightKg
    
    // Production requirement
    val productionTDN = 0.45f * milkYieldLiters * (milkFatPercent / 4.0f)
    
    // Total TDN
    var totalTDN = maintenanceTDN + productionTDN
    
    // Lactation stage multiplier
    totalTDN *= when (lactationStage) {
        LactationStage.EARLY -> 1.15f // Early lactation needs more
        LactationStage.MID -> 1.0f
        LactationStage.LATE -> 0.9f
        LactationStage.DRY -> 0.6f // Only maintenance
    }
    
    // Pregnancy adjustment
    if (pregnancyStatus.isPregnant && (pregnancyStatus.monthOfPregnancy ?: 0) >= 7) {
        totalTDN += 2.0f // Extra TDN for fetal development
    }
    
    return totalTDN
}
```

**CP Calculation (Crude Protein):**
```kotlin
fun calculateCP(
    weightKg: Int,
    milkYieldLiters: Float,
    lactationStage: LactationStage,
    pregnancyStatus: PregnancyStatus
): Float {
    // Maintenance requirement
    val maintenanceCP = 0.003f * weightKg
    
    // Production requirement
    val productionCP = 0.080f * milkYieldLiters
    
    // Total CP
    var totalCP = maintenanceCP + productionCP
    
    // Lactation adjustment
    totalCP *= when (lactationStage) {
        LactationStage.EARLY -> 1.15f
        LactationStage.MID -> 1.0f
        LactationStage.LATE -> 0.9f
        LactationStage.DRY -> 0.6f
    }
    
    // Pregnancy adjustment
    if (pregnancyStatus.isPregnant && (pregnancyStatus.monthOfPregnancy ?: 0) >= 7) {
        totalCP += 0.5f // Extra protein for fetal growth
    }
    
    return totalCP
}
```

### 5.2 Feed Allocation Algorithm (Updated with Grazing)

```kotlin
data class FeedRecipe(
    val greenFodderKg: Float,
    val greenFodderType: List<GrazingGrass>,
    val dryFodderKg: Float,
    val dryFodderType: List<GrazingGrass>,
    val concentrateKg: Float,
    val concentrateMix: List<ConcentrateIngredient>,
    val mineralMixGrams: Float,
    val saltGrams: Float,
    val totalDMI: Float,
    val totalCostPerDay: Int,
    val nutritionalBreakdown: NutritionalBreakdown
)

fun calculateFeedRecipe(cowProfile: CowProfile): FeedRecipe {
    // Step 1: Calculate total DMI
    val totalDMI = calculateDMI(
        weightKg = cowProfile.weight,
        milkYieldLiters = cowProfile.milkYield,
        bmiStatus = cowProfile.bmiStatus,
        pregnancyStatus = cowProfile.pregnancyStatus
    )
    
    // Step 2: Allocate DMI to feed categories
    val greenFodderDMI = totalDMI * 0.60f // 60% green fodder
    val dryFodderDMI = totalDMI * 0.20f // 20% dry fodder
    val concentrateDMI = totalDMI * 0.20f // 20% concentrate
    
    // Step 3: Select optimal green fodder based on availability
    val greenFodder = selectOptimalGreenFodder(
        requiredDMI = greenFodderDMI,
        season = getCurrentSeason(),
        budget = cowProfile.dailyBudget
    )
    
    // Step 4: Select optimal dry fodder
    val dryFodder = selectOptimalDryFodder(
        requiredDMI = dryFodderDMI,
        season = getCurrentSeason()
    )
    
    // Step 5: Calculate required TDN and CP
    val requiredTDN = calculateTDN(...)
    val requiredCP = calculateCP(...)
    
    // Step 6: Optimize concentrate mix using greedy algorithm
    val concentrateMix = optimizeConcentrateMix(
        requiredDMI = concentrateDMI,
        requiredTDN = requiredTDN,
        requiredCP = requiredCP,
        availableIngredients = getAvailableConcentrates()
    )
    
    // Step 7: Add minerals and salt
    val mineralMixGrams = (cowProfile.weight / 200) * 50 // 50g per 200kg weight
    val saltGrams = 30f // Standard 30g salt
    
    // Step 8: Calculate total cost
    val totalCost = calculateTotalCost(greenFodder, dryFodder, concentrateMix)
    
    return FeedRecipe(
        greenFodderKg = greenFodder.sumOf { it.quantityKg },
        greenFodderType = greenFodder.map { it.grass },
        dryFodderKg = dryFodder.sumOf { it.quantityKg },
        dryFodderType = dryFodder.map { it.grass },
        concentrateKg = concentrateDMI,
        concentrateMix = concentrateMix,
        mineralMixGrams = mineralMixGrams,
        saltGrams = saltGrams,
        totalDMI = totalDMI,
        totalCostPerDay = totalCost
    )
}

// Greedy algorithm for concentrate optimization
fun optimizeConcentrateMix(
    requiredDMI: Float,
    requiredTDN: Float,
    requiredCP: Float,
    availableIngredients: List<Ingredient>
): List<ConcentrateIngredient> {
    val result = mutableListOf<ConcentrateIngredient>()
    
    // Calculate TDN per Rupee and CP per Rupee for each ingredient
    val rankedByValue = availableIngredients
        .map { ingredient ->
            val tdnPerRupee = (ingredient.tdnPercent / 100) / ingredient.pricePerKg
            val cpPerRupee = (ingredient.cpPercent / 100) / ingredient.pricePerKg
            val overallValue = tdnPerRupee + cpPerRupee // Combined value score
            Pair(ingredient, overallValue)
        }
        .sortedByDescending { it.second }
    
    var remainingDMI = requiredDMI
    var remainingTDN = requiredTDN
    var remainingCP = requiredCP
    
    for ((ingredient, _) in rankedByValue) {
        if (remainingDMI <= 0) break
        
        // Calculate how much of this ingredient to add
        val maxQuantity = minOf(
            remainingDMI,
            ingredient.maxDailyLimitKg ?: Float.MAX_VALUE
        )
        
        val quantityKg = minOf(
            maxQuantity,
            remainingTDN / (ingredient.tdnPercent / 100),
            remainingCP / (ingredient.cpPercent / 100)
        )
        
        if (quantityKg > 0.1f) { // Minimum 100g
            result.add(
                ConcentrateIngredient(
                    ingredient = ingredient,
                    quantityKg = quantityKg,
                    cost = quantityKg * ingredient.pricePerKg
                )
            )
            
            remainingDMI -= quantityKg
            remainingTDN -= quantityKg * (ingredient.tdnPercent / 100)
            remainingCP -= quantityKg * (ingredient.cpPercent / 100)
        }
    }
    
    return result
}
```

### 5.3 Grazing Recommendation Engine (NEW)

```kotlin
fun getGrazingRecommendation(
    cowProfile: CowProfile,
    availableGrasses: List<GrazingGrass>
): GrazingRecommendation {
    val currentSeason = getCurrentSeason()
    
    // Filter grasses available in current season
    val seasonalGrasses = availableGrasses.filter {
        currentSeason in it.seasonalAvailability
    }
    
    // Prioritize high-protein grasses for high-yielding cows
    val prioritizedGrasses = if (cowProfile.milkYield > 20) {
        seasonalGrasses.sortedByDescending { it.nutritionalValue.crudeProtein }
    } else {
        seasonalGrasses.sortedBy { it.priceRange.min }
    }
    
    // Calculate grazing time and quantity
    val grazingQuantityKg = calculateDMI(...) * 0.60f // 60% of DMI from grazing
    val grazingDurationHours = when (currentSeason) {
        Season.MONSOON -> 6f // More grazing time, grass is abundant
        Season.SUMMER -> 4f // Less time due to heat
        Season.WINTER -> 5f
    }
    
    return GrazingRecommendation(
        primaryGrass = prioritizedGrasses.firstOrNull(),
        secondaryGrass = prioritizedGrasses.getOrNull(1),
        quantityKg = grazingQuantityKg,
        durationHours = grazingDurationHours,
        timeOfDay = listOf("Early morning (6-9 AM)", "Late afternoon (4-7 PM)"),
        specialInstructions = generateGrazingInstructions(cowProfile, currentSeason)
    )
}

fun generateGrazingInstructions(
    cowProfile: CowProfile,
    season: Season
): List<String> {
    val instructions = mutableListOf<String>()
    
    // Pregnancy-specific instructions
    if (cowProfile.pregnancyStatus.isPregnant) {
        instructions.add("Provide easy access to water while grazing")
        instructions.add("Avoid steep terrain - use flat grazing areas")
    }
    
    // Season-specific instructions
    when (season) {
        Season.SUMMER -> {
            instructions.add("Graze in shaded areas during peak heat")
            instructions.add("Ensure 50L water availability")
        }
        Season.MONSOON -> {
            instructions.add("Avoid waterlogged pastures")
            instructions.add("Check for harmful weeds")
        }
        Season.WINTER -> {
            instructions.add("Graze during warmer afternoon hours")
            instructions.add("Supplement with dry fodder in evening")
        }
    }
    
    // BMI-specific instructions
    when (cowProfile.bmiStatus) {
        BMIStatus.UNDERWEIGHT -> {
            instructions.add("Increase concentrate after grazing")
            instructions.add("Allow extra grazing time (add 1 hour)")
        }
        BMIStatus.OVERWEIGHT -> {
            instructions.add("Limit concentrate, rely more on grazing")
        }
        else -> { /* No special instructions */ }
    }
    
    return instructions
}
```

---

## 6. DATABASE ARCHITECTURE (UPDATED)

### 6.1 Room Database Entities

```kotlin
@Database(
    entities = [
        FarmerProfileEntity::class,
        CowProfileEntity::class,
        IngredientEntity::class,
        GrazingGrassEntity::class,
        FeedRecipeEntity::class,
        RecipeIngredientCrossRef::class,
        VaccinationRecordEntity::class,
        GenAICacheEntity::class,
        SyncMetadataEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class PashuAaharDatabase : RoomDatabase() {
    abstract fun farmerProfileDao(): FarmerProfileDao
    abstract fun cowProfileDao(): CowProfileDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun grazingGrassDao(): GrazingGrassDao
    abstract fun feedRecipeDao(): FeedRecipeDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun genAICacheDao(): GenAICacheDao
    abstract fun syncMetadataDao(): SyncMetadataDao
}
```

**Entity: FarmerProfileEntity (NEW)**
```kotlin
@Entity(tableName = "farmer_profile")
data class FarmerProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val farmerLevel: String, // BEGINNER, INTERMEDIATE, LARGE_SCALE
    val preferredLanguage: String, // ENGLISH, HINDI, KANNADA
    val totalCattleCount: Int,
    val farmLocation: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastSyncDate: Long? = null
)
```

**Entity: CowProfileEntity (UPDATED)**
```kotlin
@Entity(tableName = "cow_profiles")
data class CowProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val farmerId: Long,
    val name: String? = null,
    val breed: String, // JERSEY, HOLSTEIN_FRIESIAN, GIR, SAHIWAL, BUFFALO
    val ageMonths: Int,
    val weightKg: Int,
    val bmiStatus: String, // UNDERWEIGHT, HEALTHY, WELL_FED, OVERWEIGHT
    val lactationStage: String, // EARLY, MID, LATE, DRY
    val milkYieldLiters: Float,
    val milkFatPercent: Float = 4.0f,
    
    // NEW FIELDS
    val isPregnant: Boolean = false,
    val pregnancyMonth: Int? = null, // 1-9
    val expectedCalvingDate: Long? = null,
    
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

**Entity: GrazingGrassEntity (NEW)**
```kotlin
@Entity(tableName = "grazing_grasses")
data class GrazingGrassEntity(
    @PrimaryKey
    val id: String, // e.g., "napier", "berseem"
    val nameEnglish: String,
    val nameHindi: String,
    val nameKannada: String,
    val category: String, // GREEN_FODDER, DRY_FODDER, LEGUME
    val imagePath: String, // Local asset path
    
    // Nutritional values
    val crudeProteinPercent: Float,
    val tdnPercent: Float,
    val dryMatterPercent: Float,
    val calciumPercent: Float,
    val phosphorusPercent: Float,
    val fiberPercent: Float,
    
    // Availability
    val availableInMonsoon: Boolean,
    val availableInSummer: Boolean,
    val availableInWinter: Boolean,
    
    // Pricing
    val pricePerKgMin: Int,
    val pricePerKgMax: Int,
    
    // Additional info
    val idealForBreeds: String, // Comma-separated list
    val mixingRecommendations: String? = null,
    
    val lastUpdated: Long = System.currentTimeMillis()
)

// Pre-populated data migration
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new tables
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS grazing_grasses (
                id TEXT PRIMARY KEY NOT NULL,
                nameEnglish TEXT NOT NULL,
                nameHindi TEXT NOT NULL,
                nameKannada TEXT NOT NULL,
                category TEXT NOT NULL,
                imagePath TEXT NOT NULL,
                crudeProteinPercent REAL NOT NULL,
                tdnPercent REAL NOT NULL,
                dryMatterPercent REAL NOT NULL,
                calciumPercent REAL NOT NULL,
                phosphorusPercent REAL NOT NULL,
                fiberPercent REAL NOT NULL,
                availableInMonsoon INTEGER NOT NULL,
                availableInSummer INTEGER NOT NULL,
                availableInWinter INTEGER NOT NULL,
                pricePerKgMin INTEGER NOT NULL,
                pricePerKgMax INTEGER NOT NULL,
                idealForBreeds TEXT NOT NULL,
                mixingRecommendations TEXT,
                lastUpdated INTEGER NOT NULL
            )
        """)
        
        // Insert pre-populated grasses
        database.execSQL("""
            INSERT INTO grazing_grasses VALUES 
            ('napier', 'Napier Grass', 'नेपियर घास', 'ನೇಪಿಯರ್ ಹುಲ್ಲು', 
             'GREEN_FODDER', 'assets/grasses/napier.webp',
             8.5, 65.0, 20.0, 0.4, 0.25, 32.0,
             1, 0, 1, 3, 5, 'JERSEY,HOLSTEIN_FRIESIAN', 'berseem,maize',
             ${System.currentTimeMillis()})
        """)
        
        // ... insert more grasses
        
        // Add new columns to cow_profiles
        database.execSQL("ALTER TABLE cow_profiles ADD COLUMN isPregnant INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE cow_profiles ADD COLUMN pregnancyMonth INTEGER")
        database.execSQL("ALTER TABLE cow_profiles ADD COLUMN expectedCalvingDate INTEGER")
        database.execSQL("ALTER TABLE cow_profiles ADD COLUMN bmiStatus TEXT NOT NULL DEFAULT 'HEALTHY'")
    }
}
```

**Entity: VaccinationRecordEntity (NEW)**
```kotlin
@Entity(
    tableName = "vaccination_records",
    foreignKeys = [
        ForeignKey(
            entity = CowProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["cowId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("cowId"), Index("dueDate")]
)
data class VaccinationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cowId: Long,
    val vaccineName: String,
    val vaccineType: String, // INJECTION, ORAL, TOPICAL
    val dueDate: Long, // Timestamp
    val completedDate: Long? = null,
    val repeatFrequency: String, // ONE_TIME, EVERY_3_MONTHS, EVERY_6_MONTHS, ANNUALLY
    val status: String, // UPCOMING, DUE_SOON, OVERDUE, COMPLETED
    val notes: String? = null,
    val batchNumber: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
```

**Entity: SyncMetadataEntity (NEW)**
```kotlin
@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey
    val key: String, // e.g., "ingredient_prices", "grazing_data", "vaccination_schedules"
    val lastSyncTimestamp: Long,
    val syncStatus: String, // SUCCESS, FAILED, PENDING
    val dataVersion: Int, // Increments with each successful sync
    val errorMessage: String? = null
)
```

### 6.2 DAOs (Data Access Objects)

**GrazingGrassDao (NEW)**
```kotlin
@Dao
interface GrazingGrassDao {
    @Query("SELECT * FROM grazing_grasses")
    fun getAllGrasses(): Flow<List<GrazingGrassEntity>>
    
    @Query("""
        SELECT * FROM grazing_grasses 
        WHERE (availableInMonsoon = 1 AND :season = 'MONSOON')
           OR (availableInSummer = 1 AND :season = 'SUMMER')
           OR (availableInWinter = 1 AND :season = 'WINTER')
    """)
    fun getSeasonalGrasses(season: String): Flow<List<GrazingGrassEntity>>
    
    @Query("SELECT * FROM grazing_grasses WHERE category = :category")
    fun getGrassesByCategory(category: String): Flow<List<GrazingGrassEntity>>
    
    @Query("SELECT * FROM grazing_grasses WHERE id = :grassId")
    suspend fun getGrassById(grassId: String): GrazingGrassEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(grasses: List<GrazingGrassEntity>)
    
    @Update
    suspend fun updateGrass(grass: GrazingGrassEntity)
    
    @Query("UPDATE grazing_grasses SET pricePerKgMin = :minPrice, pricePerKgMax = :maxPrice WHERE id = :grassId")
    suspend fun updatePricing(grassId: String, minPrice: Int, maxPrice: Int)
}
```

**VaccinationDao (NEW)**
```kotlin
@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccination_records WHERE cowId = :cowId ORDER BY dueDate ASC")
    fun getVaccinationsForCow(cowId: Long): Flow<List<VaccinationRecordEntity>>
    
    @Query("""
        SELECT * FROM vaccination_records 
        WHERE status IN ('UPCOMING', 'DUE_SOON', 'OVERDUE') 
        ORDER BY dueDate ASC
    """)
    fun getUpcomingVaccinations(): Flow<List<VaccinationRecordEntity>>
    
    @Query("""
        SELECT * FROM vaccination_records 
        WHERE dueDate <= :timestamp AND status = 'UPCOMING'
    """)
    suspend fun getVaccinationsDueBy(timestamp: Long): List<VaccinationRecordEntity>
    
    @Insert
    suspend fun insertVaccination(vaccination: VaccinationRecordEntity): Long
    
    @Update
    suspend fun updateVaccination(vaccination: VaccinationRecordEntity)
    
    @Query("UPDATE vaccination_records SET status = 'COMPLETED', completedDate = :completedDate WHERE id = :vaccinationId")
    suspend fun markAsCompleted(vaccinationId: Long, completedDate: Long)
    
    @Query("DELETE FROM vaccination_records WHERE id = :vaccinationId")
    suspend fun deleteVaccination(vaccinationId: Long)
    
    // Update status based on current date (called daily via WorkManager)
    @Query("""
        UPDATE vaccination_records 
        SET status = CASE 
            WHEN dueDate < :currentTime THEN 'OVERDUE'
            WHEN dueDate <= :sevenDaysFromNow THEN 'DUE_SOON'
            ELSE 'UPCOMING'
        END
        WHERE status != 'COMPLETED'
    """)
    suspend fun updateVaccinationStatuses(currentTime: Long, sevenDaysFromNow: Long)
}
```

---

## 7. GenAI INTEGRATION LAYER (UPDATED)

### 7.1 Prompt Templates for Different Use Cases

```kotlin
object GenAIPrompts {
    
    // Ingredient explanation prompt
    fun ingredientExplanation(
        ingredientName: String,
        cowBreed: String,
        lactationStage: String,
        recommendedKg: Float,
        farmerLevel: FarmerLevel
    ): String {
        val complexity = when (farmerLevel) {
            FarmerLevel.BEGINNER -> "very simple, 5th-grade level"
            FarmerLevel.INTERMEDIATE -> "moderate, 7th-grade level"
            FarmerLevel.LARGE_SCALE -> "detailed, technical"
        }
        
        return """
            You are a friendly veterinary nutrition expert helping an Indian dairy farmer.
            
            Explain why we're recommending $recommendedKg kg of $ingredientName for a $cowBreed cow in $lactationStage lactation.
            
            Guidelines:
            - Use $complexity language
            - Maximum 60 words
            - Avoid jargon like "crude protein", "TDN" - use simple terms like "strength", "energy"
            - Mention if it's seasonally available
            - Be encouraging and friendly
            - Include ONE practical tip
            
            Example: "Napier grass is full of energy and vitamins! Fresh and green, it helps your cow make more milk. Best in monsoon season. Tip: Cut it when 3-4 feet tall for maximum nutrition."
        """.trimIndent()
    }
    
    // Grazing recommendation prompt (NEW)
    fun grazingRecommendation(
        availableGrasses: List<String>,
        cowProfile: CowProfile,
        season: Season
    ): String {
        return """
            You are an expert in cattle grazing management.
            
            Available grasses: ${availableGrasses.joinToString(", ")}
            Cow details: ${cowProfile.breed}, ${cowProfile.milkYield}L/day, ${cowProfile.lactationStage} lactation
            Season: $season
            
            Provide:
            1. Best grazing schedule (timing and duration)
            2. Which grass to prioritize and why
            3. One safety tip
            
            Keep it under 80 words, farmer-friendly language.
        """.trimIndent()
    }
    
    // Health alert prompt (NEW)
    fun healthAlert(
        cowProfile: CowProfile,
        symptom: String
    ): String {
        return """
            You are a veterinary health assistant.
            
            Cow: ${cowProfile.breed}, ${cowProfile.ageMonths} months old
            Symptom reported: $symptom
            
            Provide:
            1. Possible causes (2-3, simple language)
            2. Immediate action farmer should take
            3. When to call a vet
            
            Maximum 100 words, very clear and actionable.
        """.trimIndent()
    }
}
```

### 7.2 Offline GenAI Implementation

```kotlin
class GenAIRepository(
    private val geminiNano: GeminiNano?,
    private val geminiApi: GeminiApiClient,
    private val cacheDao: GenAICacheDao,
    private val networkMonitor: NetworkMonitor
) {
    
    suspend fun getExplanation(
        promptType: PromptType,
        params: Map<String, Any>
    ): Result<String> {
        // Step 1: Check cache (valid for 7 days)
        val cacheKey = generateCacheKey(promptType, params)
        val cachedResponse = cacheDao.getCachedResponse(cacheKey)
        
        if (cachedResponse != null && !cachedResponse.isExpired()) {
            return Result.Success(cachedResponse.response)
        }
        
        // Step 2: Try Gemini Nano (on-device)
        geminiNano?.let {
            try {
                val prompt = generatePrompt(promptType, params)
                val response = it.generateContent(prompt)
                
                // Cache the response
                cacheDao.insert(GenAICacheEntity(
                    key = cacheKey,
                    response = response,
                    expiresAt = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000),
                    createdAt = System.currentTimeMillis()
                ))
                
                return Result.Success(response)
            } catch (e: Exception) {
                Log.e("GenAI", "Gemini Nano failed", e)
                // Fall through to API
            }
        }
        
        // Step 3: Try Gemini API (if online)
        if (networkMonitor.isOnline()) {
            try {
                val prompt = generatePrompt(promptType, params)
                val response = geminiApi.generateContent(prompt)
                
                // Cache the response
                cacheDao.insert(GenAICacheEntity(
                    key = cacheKey,
                    response = response,
                    expiresAt = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000),
                    createdAt = System.currentTimeMillis()
                ))
                
                return Result.Success(response)
            } catch (e: Exception) {
                Log.e("GenAI", "Gemini API failed", e)
                // Fall through to static
            }
        }
        
        // Step 4: Return static fallback
        return Result.Success(getStaticFallback(promptType, params))
    }
    
    private fun generatePrompt(promptType: PromptType, params: Map<String, Any>): String {
        return when (promptType) {
            PromptType.INGREDIENT_EXPLANATION -> GenAIPrompts.ingredientExplanation(
                ingredientName = params["ingredientName"] as String,
                cowBreed = params["cowBreed"] as String,
                lactationStage = params["lactationStage"] as String,
                recommendedKg = params["recommendedKg"] as Float,
                farmerLevel = params["farmerLevel"] as FarmerLevel
            )
            PromptType.GRAZING_RECOMMENDATION -> GenAIPrompts.grazingRecommendation(
                availableGrasses = params["availableGrasses"] as List<String>,
                cowProfile = params["cowProfile"] as CowProfile,
                season = params["season"] as Season
            )
            PromptType.HEALTH_ALERT -> GenAIPrompts.healthAlert(
                cowProfile = params["cowProfile"] as CowProfile,
                symptom = params["symptom"] as String
            )
        }
    }
    
    private fun getStaticFallback(promptType: PromptType, params: Map<String, Any>): String {
        return when (promptType) {
            PromptType.INGREDIENT_EXPLANATION -> {
                val ingredientName = params["ingredientName"] as String
                "This ingredient provides essential nutrition for your cow. Feed as recommended for best results."
            }
            PromptType.GRAZING_RECOMMENDATION -> {
                "Graze your cow in the early morning and late afternoon. Choose fresh, green grass when available."
            }
            PromptType.HEALTH_ALERT -> {
                "If your cow shows unusual symptoms, please consult a veterinarian immediately."
            }
        }
    }
    
    private fun generateCacheKey(promptType: PromptType, params: Map<String, Any>): String {
        return "${promptType.name}_${params.hashCode()}"
    }
}

enum class PromptType {
    INGREDIENT_EXPLANATION,
    GRAZING_RECOMMENDATION,
    HEALTH_ALERT
}
```

---

## 8. 7-DAY DATA SYNC IMPLEMENTATION

### 8.1 WorkManager Configuration

```kotlin
class DataSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val syncRepository: SyncRepository,
    private val ingredientDao: IngredientDao,
    private val grazingGrassDao: GrazingGrassDao,
    private val vaccinationDao: VaccinationDao,
    private val genAICacheDao: GenAICacheDao,
    private val syncMetadataDao: SyncMetadataDao
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            Log.d("DataSync", "Starting 7-day sync...")
            
            // Step 1: Update ingredient market prices
            val ingredientSyncSuccess = syncIngredientPrices()
            
            // Step 2: Update grazing grass nutritional data
            val grazingSyncSuccess = syncGrazingData()
            
            // Step 3: Update vaccination schedules
            val vaccinationSyncSuccess = syncVaccinationSchedules()
            
            // Step 4: Clean expired cache
            cleanExpiredCache()
            
            // Step 5: Update vaccination statuses
            updateVaccinationStatuses()
            
            // Step 6: Update sync metadata
            updateSyncMetadata(
                ingredientSyncSuccess,
                grazingSyncSuccess,
                vaccinationSyncSuccess
            )
            
            Log.d("DataSync", "7-day sync completed successfully")
            Result.success()
            
        } catch (e: Exception) {
            Log.e("DataSync", "Sync failed", e)
            if (runAttemptCount < 3) {
                Result.retry() // Retry up to 3 times
            } else {
                Result.failure()
            }
        }
    }
    
    private suspend fun syncIngredientPrices(): Boolean {
        return try {
            val updatedPrices = syncRepository.fetchIngredientPrices()
            updatedPrices.forEach { priceUpdate ->
                ingredientDao.updatePrice(
                    ingredientId = priceUpdate.ingredientId,
                    newPrice = priceUpdate.pricePerKg
                )
            }
            true
        } catch (e: Exception) {
            Log.e("DataSync", "Ingredient price sync failed", e)
            false
        }
    }
    
    private suspend fun syncGrazingData(): Boolean {
        return try {
            val updatedGrasses = syncRepository.fetchGrazingGrassData()
            updatedGrasses.forEach { grassUpdate ->
                grazingGrassDao.updatePricing(
                    grassId = grassUpdate.id,
                    minPrice = grassUpdate.pricePerKgMin,
                    maxPrice = grassUpdate.pricePerKgMax
                )
                // Also update nutritional values if server provides new data
            }
            true
        } catch (e: Exception) {
            Log.e("DataSync", "Grazing data sync failed", e)
            false
        }
    }
    
    private suspend fun syncVaccinationSchedules(): Boolean {
        return try {
            val newSchedules = syncRepository.fetchVaccinationSchedules()
            // Update local database with any new vaccination recommendations
            true
        } catch (e: Exception) {
            Log.e("DataSync", "Vaccination sync failed", e)
            false
        }
    }
    
    private suspend fun cleanExpiredCache() {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        
        // Delete expired GenAI responses
        genAICacheDao.deleteExpired(sevenDaysAgo)
        
        // Delete old analytics logs (>30 days)
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
        // analyticsDao.deleteOlderThan(thirtyDaysAgo)
        
        Log.d("DataSync", "Cleaned expired cache")
    }
    
    private suspend fun updateVaccinationStatuses() {
        val currentTime = System.currentTimeMillis()
        val sevenDaysFromNow = currentTime + (7 * 24 * 60 * 60 * 1000)
        
        vaccinationDao.updateVaccinationStatuses(currentTime, sevenDaysFromNow)
        
        // Schedule notifications for upcoming vaccinations
        val upcomingVaccinations = vaccinationDao.getVaccinationsDueBy(sevenDaysFromNow)
        upcomingVaccinations.forEach { vaccination ->
            scheduleVaccinationNotification(vaccination)
        }
    }
    
    private fun scheduleVaccinationNotification(vaccination: VaccinationRecordEntity) {
        val notificationTime = vaccination.dueDate - (24 * 60 * 60 * 1000) // 1 day before
        
        // Use AlarmManager or WorkManager to schedule notification
        val notificationWorkRequest = OneTimeWorkRequestBuilder<VaccinationNotificationWorker>()
            .setInitialDelay(
                notificationTime - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(
                    "vaccinationId" to vaccination.id,
                    "cowId" to vaccination.cowId,
                    "vaccineName" to vaccination.vaccineName
                )
            )
            .build()
        
        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                "vaccination_${vaccination.id}",
                ExistingWorkPolicy.REPLACE,
                notificationWorkRequest
            )
    }
    
    private suspend fun updateSyncMetadata(
        ingredientSyncSuccess: Boolean,
        grazingSyncSuccess: Boolean,
        vaccinationSyncSuccess: Boolean
    ) {
        val currentTime = System.currentTimeMillis()
        
        syncMetadataDao.upsert(SyncMetadataEntity(
            key = "ingredient_prices",
            lastSyncTimestamp = currentTime,
            syncStatus = if (ingredientSyncSuccess) "SUCCESS" else "FAILED",
            dataVersion = if (ingredientSyncSuccess) 
                (syncMetadataDao.getMetadata("ingredient_prices")?.dataVersion ?: 0) + 1 
                else (syncMetadataDao.getMetadata("ingredient_prices")?.dataVersion ?: 0)
        ))
        
        syncMetadataDao.upsert(SyncMetadataEntity(
            key = "grazing_data",
            lastSyncTimestamp = currentTime,
            syncStatus = if (grazingSyncSuccess) "SUCCESS" else "FAILED",
            dataVersion = if (grazingSyncSuccess) 
                (syncMetadataDao.getMetadata("grazing_data")?.dataVersion ?: 0) + 1 
                else (syncMetadataDao.getMetadata("grazing_data")?.dataVersion ?: 0)
        ))
    }
}

// Schedule the 7-day periodic sync in Application class
class PashuAaharApplication : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        
        // Schedule 7-day sync
        schedule7DaySync()
    }
    
    private fun schedule7DaySync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS,
            flexTimeInterval = 1, // 1-day flex window
            flexTimeIntervalUnit = TimeUnit.DAYS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
        
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DataSync7Day",
                ExistingPeriodicWorkPolicy.KEEP, // Don't replace if already scheduled
                syncWorkRequest
            )
        
        Log.d("PashuAahar", "7-day sync scheduled")
    }
    
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(DaggerWorkerFactory()) // Inject Hilt dependencies
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }
}
```

### 8.2 Vaccination Notification Worker

```kotlin
class VaccinationNotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val vaccinationDao: VaccinationDao,
    private val cowProfileDao: CowProfileDao
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        val vaccinationId = inputData.getLong("vaccinationId", 0)
        val cowId = inputData.getLong("cowId", 0)
        val vaccineName = inputData.getString("vaccineName") ?: ""
        
        // Fetch cow details for personalized notification
        val cow = cowProfileDao.getCowById(cowId)
        
        // Create notification
        val notification = NotificationCompat.Builder(applicationContext, "vaccination_channel")
            .setSmallIcon(R.drawable.ic_vaccine)
            .setContentTitle("Vaccination Reminder")
            .setContentText("${cow?.name ?: "Your cow"} needs $vaccineName vaccine tomorrow")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent(cowId))
            .build()
        
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(vaccinationId.toInt(), notification)
        
        return Result.success()
    }
    
    private fun createPendingIntent(cowId: Long): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            data = Uri.parse("pashuaahar://health/$cowId")
        }
        return PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
```

---

## 9. UI/UX DESIGN GUIDELINES

### 9.1 Color Palette (Farmer-Friendly)

```kotlin
// theme/Color.kt
val FarmGreen = Color(0xFF4CAF50) // Primary - represents crops, health
val EarthBrown = Color(0xFF795548) // Secondary - soil, grounding
val SkyBlue = Color(0xFF2196F3) // Accent - water, freshness
val SunYellow = Color(0xFFFFC107) // Highlight - energy, optimism

val HealthyGreen = Color(0xFF81C784) // BMI healthy status
val CautionYellow = Color(0xFFFFB74D) // Due soon, attention needed
val AlertRed = Color(0xFFE57373) // Overdue, underweight

val BackgroundLight = Color(0xFFF5F5F5) // Light gray for backgrounds
val SurfaceWhite = Color(0xFFFFFFFF) // Card backgrounds
val TextPrimary = Color(0xFF212121) // Dark gray for primary text
val TextSecondary = Color(0xFF757575) // Medium gray for secondary text
```

### 9.2 Typography (Large, Readable)

```kotlin
// theme/Type.kt
val PashuAaharTypography = Typography(
    // Display (for main titles)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    
    // Headings
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    
    // Body (LARGER than default for farmer accessibility)
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp, // Increased from default 16sp
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp, // Increased from default 14sp
        lineHeight = 24.sp
    ),
    
    // Labels
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
)
```

### 9.3 Component Specifications

**Large Touch Targets:**
```kotlin
// All buttons must be minimum 56dp height
@Composable
fun PashuAaharButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp), // WCAG AAA compliance
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = FarmGreen,
            contentColor = Color.White
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
```

**Visual Sliders with Animated Feedback:**
```kotlin
@Composable
fun VisualSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    label: String,
    visualFeedback: @Composable (Float) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Visual feedback component (e.g., changing cow silhouette)
        visualFeedback(value)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Label
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        // Large value display
        Text(
            text = "${value.toInt()}",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = FarmGreen
        )
        
        // Slider with custom thumb
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            thumb = {
                // Custom large thumb (56dp for easy touch)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(FarmGreen, CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Slider thumb",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            track = { sliderPositions ->
                // Custom thick track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderPositions.activeRange.endInclusive)
                            .fillMaxHeight()
                            .background(FarmGreen, RoundedCornerShape(6.dp))
                    )
                }
            }
        )
    }
}
```

**Animated Status Indicators:**
```kotlin
@Composable
fun StatusIndicator(
    status: VaccinationStatus,
    size: Dp = 64.dp
) {
    val (color, icon, label) = when (status) {
        VaccinationStatus.COMPLETED -> Triple(HealthyGreen, Icons.Default.CheckCircle, "Done")
        VaccinationStatus.UPCOMING -> Triple(SkyBlue, Icons.Default.Schedule, "Upcoming")
        VaccinationStatus.DUE_SOON -> Triple(CautionYellow, Icons.Default.Warning, "Due Soon")
        VaccinationStatus.OVERDUE -> Triple(AlertRed, Icons.Default.Error, "Overdue")
    }
    
    // Pulsing animation for overdue
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (status == VaccinationStatus.OVERDUE) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}
```

---

## 10. TESTING & QA

### 10.1 Unit Tests

```kotlin
class NutritionCalculatorTest {
    
    @Test
    fun `DMI calculation for standard Jersey cow`() {
        val dmi = calculateDMI(
            weightKg = 400,
            milkYieldLiters = 20f,
            bmiStatus = BMIStatus.HEALTHY,
            pregnancyStatus = PregnancyStatus(isPregnant = false)
        )
        
        // Expected: (0.0185 × 400) + (0.305 × 20) = 7.4 + 6.1 = 13.5 kg
        assertEquals(13.5f, dmi, 0.1f)
    }
    
    @Test
    fun `DMI increases for underweight cow`() {
        val normalDMI = calculateDMI(400, 20f, BMIStatus.HEALTHY, PregnancyStatus(false))
        val underweightDMI = calculateDMI(400, 20f, BMIStatus.UNDERWEIGHT, PregnancyStatus(false))
        
        assertTrue(underweightDMI > normalDMI)
        assertEquals(normalDMI * 1.10f, underweightDMI, 0.01f)
    }
    
    @Test
    fun `DMI increases for pregnant cow in last trimester`() {
        val normalDMI = calculateDMI(
            400, 20f, BMIStatus.HEALTHY,
            PregnancyStatus(isPregnant = false)
        )
        val pregnantDMI = calculateDMI(
            400, 20f, BMIStatus.HEALTHY,
            PregnancyStatus(isPregnant = true, monthOfPregnancy = 8)
        )
        
        assertTrue(pregnantDMI > normalDMI)
        assertEquals(normalDMI * 1.15f, pregnantDMI, 0.01f)
    }
    
    @Test
    fun `TDN calculation for dry cow is maintenance only`() {
        val tdn = calculateTDN(
            weightKg = 400,
            milkYieldLiters = 0f,
            milkFatPercent = 4.0f,
            lactationStage = LactationStage.DRY,
            pregnancyStatus = PregnancyStatus(false)
        )
        
        // Expected: (0.035 × 400) × 0.6 = 8.4 kg
        assertEquals(8.4f, tdn, 0.1f)
    }
    
    @Test
    fun `concentrate optimization prioritizes cost-effective ingredients`() {
        val ingredients = listOf(
            createMockIngredient("Maize", tdnPercent = 70f, cpPercent = 9f, pricePerKg = 20),
            createMockIngredient("Soybean", tdnPercent = 75f, cpPercent = 44f, pricePerKg = 50),
            createMockIngredient("Wheat Bran", tdnPercent = 65f, cpPercent = 15f, pricePerKg = 15)
        )
        
        val result = optimizeConcentrateMix(
            requiredDMI = 6f,
            requiredTDN = 4.5f,
            requiredCP = 1.2f,
            availableIngredients = ingredients
        )
        
        // Wheat bran should be prioritized due to best value per rupee
        assertTrue(result.any { it.ingredient.name == "Wheat Bran" })
        
        // Total cost should be minimized
        val totalCost = result.sumOf { it.cost.toDouble() }
        assertTrue(totalCost < 150) // Should be under ₹150 for 6kg concentrate
    }
}
```

### 10.2 Integration Tests

```kotlin
@RunWith(AndroidJUnit4::class)
class CowProfileDaoTest {
    
    private lateinit var database: PashuAaharDatabase
    private lateinit var cowProfileDao: CowProfileDao
    
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PashuAaharDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cowProfileDao = database.cowProfileDao()
    }
    
    @After
    fun teardown() {
        database.close()
    }
    
    @Test
    fun insertAndRetrieveCowProfile() = runTest {
        val cowProfile = CowProfileEntity(
            farmerId = 1,
            name = "Ganga",
            breed = "JERSEY",
            ageMonths = 48,
            weightKg = 400,
            bmiStatus = "HEALTHY",
            lactationStage = "MID",
            milkYieldLiters = 20f,
            isPregnant = false
        )
        
        val id = cowProfileDao.insertCowProfile(cowProfile)
        val retrieved = cowProfileDao.getCowById(id)
        
        assertNotNull(retrieved)
        assertEquals("Ganga", retrieved?.name)
        assertEquals(400, retrieved?.weightKg)
    }
    
    @Test
    fun updateCowWeight() = runTest {
        val cowProfile = CowProfileEntity(
            farmerId = 1,
            breed = "JERSEY",
            ageMonths = 48,
            weightKg = 400,
            bmiStatus = "HEALTHY",
            lactationStage = "MID",
            milkYieldLiters = 20f
        )
        
        val id = cowProfileDao.insertCowProfile(cowProfile)
        cowProfileDao.updateWeight(id, 420, "WELL_FED")
        
        val updated = cowProfileDao.getCowById(id)
        assertEquals(420, updated?.weightKg)
        assertEquals("WELL_FED", updated?.bmiStatus)
    }
    
    @Test
    fun updatePregnancyStatus() = runTest {
        val cowProfile = CowProfileEntity(
            farmerId = 1,
            breed = "JERSEY",
            ageMonths = 48,
            weightKg = 400,
            bmiStatus = "HEALTHY",
            lactationStage = "MID",
            milkYieldLiters = 20f,
            isPregnant = false
        )
        
        val id = cowProfileDao.insertCowProfile(cowProfile)
        
        val expectedCalvingDate = System.currentTimeMillis() + (270 * 24 * 60 * 60 * 1000L)
        cowProfileDao.updatePregnancy(id, isPregnant = true, month = 1, expectedCalvingDate)
        
        val updated = cowProfileDao.getCowById(id)
        assertTrue(updated!!.isPregnant)
        assertEquals(1, updated.pregnancyMonth)
    }
}
```

### 10.3 UI Tests (Compose)

```kotlin
@RunWith(AndroidJUnit4::class)
class CowProfileStepperTest {
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Test
    fun completeProfileCreationFlow() {
        // Step 1: Select breed
        composeTestRule.onNodeWithText("Jersey").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        
        // Step 2: Select age (drag slider to 48 months / 4 years)
        composeTestRule.onNodeWithContentDescription("Age slider")
            .performTouchInput {
                swipeRight(endX = center.x + 200f)
            }
        composeTestRule.onNodeWithText("Next").performClick()
        
        // Step 3: Select weight
        composeTestRule.onNodeWithContentDescription("Weight slider")
            .performTouchInput {
                swipeRight(endX = center.x + 150f)
            }
        // Verify BMI status is shown
        composeTestRule.onNodeWithText("Healthy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next").performClick()
        
        // Step 4: Select lactation stage
        composeTestRule.onNodeWithText("Peak").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        
        // Step 5: Set milk yield
        composeTestRule.onNodeWithContentDescription("Milk yield slider")
            .performTouchInput {
                swipeRight(endX = center.x + 180f)
            }
        composeTestRule.onNodeWithText("Complete").performClick()
        
        // Verify navigation to recipe dashboard
        composeTestRule.onNodeWithText("Feed Recipe").assertIsDisplayed()
    }
    
    @Test
    fun visualFeedbackOnWeightChange() {
        composeTestRule.setContent {
            var weight by remember { mutableStateOf(300) }
            
            VisualSlider(
                value = weight.toFloat(),
                onValueChange = { weight = it.toInt() },
                valueRange = 200f..800f,
                label = "Weight"
            ) { currentWeight ->
                // Visual cow silhouette should change
                when {
                    currentWeight < 350 -> {
                        // Should show "thin" cow
                        composeTestRule.onNodeWithContentDescription("Underweight cow")
                            .assertIsDisplayed()
                    }
                    currentWeight in 350f..600f -> {
                        composeTestRule.onNodeWithContentDescription("Healthy cow")
                            .assertIsDisplayed()
                    }
                    else -> {
                        composeTestRule.onNodeWithContentDescription("Overweight cow")
                            .assertIsDisplayed()
                    }
                }
            }
        }
        
        // Drag slider to underweight range
        composeTestRule.onNodeWithContentDescription("Weight slider")
            .performTouchInput {
                swipeLeft(endX = center.x - 200f)
            }
        
        // Verify visual changes
        composeTestRule.onNodeWithText("Underweight").assertIsDisplayed()
    }
}
```

### 10.4 Performance Tests

```kotlin
@Test
fun `recipe calculation completes within 500ms`() {
    val cowProfile = CowProfile(
        breed = CattleBreed.JERSEY,
        ageMonths = 48,
        weightKg = 400,
        bmiStatus = BMIStatus.HEALTHY,
        lactationStage = LactationStage.MID,
        milkYieldLiters = 20f,
        pregnancyStatus = PregnancyStatus(false)
    )
    
    val startTime = System.currentTimeMillis()
    val recipe = calculateFeedRecipe(cowProfile)
    val endTime = System.currentTimeMillis()
    
    val duration = endTime - startTime
    assertTrue("Recipe calculation took $duration ms", duration < 500)
}

@Test
fun `database cold start under 2 seconds`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    
    val startTime = System.currentTimeMillis()
    val database = Room.databaseBuilder(
        context,
        PashuAaharDatabase::class.java,
        "pashu_aahar_test.db"
    ).build()
    
    // Force database initialization
    database.openHelper.writableDatabase
    
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    
    assertTrue("Database cold start took $duration ms", duration < 2000)
    database.close()
}
```

---

## 11. DEPLOYMENT & MAINTENANCE

### 11.1 Build Configuration

```gradle
// app/build.gradle.kts
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.pashuaahar.nutrition"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "3.0.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Room schema export
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            // Signing config
            signingConfig = signingConfigs.getByName("release")
        }
        
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }
    
    // Enable Jetpack Compose
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    
    // Asset optimization
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
```

**ProGuard Rules:**
```proguard
# proguard-rules.pro

# Keep Room entities
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# Keep Hilt dependencies
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keep,includedescriptorclasses class com.pashuaahar.**$$serializer { *; }

# Keep Gemini AI SDK
-keep class com.google.ai.client.generativeai.** { *; }

# Keep data classes used for navigation arguments
-keepclassmembers class com.pashuaahar.domain.models.** {
    <init>(...);
    <fields>;
}
```

### 11.2 Release Checklist

**Pre-Release:**
- [ ] All unit tests passing (>80% coverage)
- [ ] All UI tests passing
- [ ] Performance benchmarks met (cold start <2s, recipe calc <500ms)
- [ ] Accessibility audit completed (WCAG AA minimum)
- [ ] Localization verified for Hindi + Kannada
- [ ] Database migration tested (v2 → v3)
- [ ] ProGuard obfuscation tested (no crashes)
- [ ] APK size < 15MB
- [ ] 7-day sync tested in staging environment
- [ ] Vaccination notifications tested
- [ ] Offline functionality verified (airplane mode test)

**Release Process:**
1. **Alpha Track** (Internal testing): 10 team members, 1 week
2. **Closed Beta**: 50 farmers from 3 states, 2 weeks
3. **Open Beta**: 500 users, 4 weeks
4. **Production Rollout**: 10% → 50% → 100% over 2 weeks

### 11.3 Analytics & Monitoring

**Privacy-First Local Analytics:**
```kotlin
@Entity(tableName = "usage_analytics")
data class UsageAnalyticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventType: String, // SCREEN_VIEW, RECIPE_GENERATED, PROFILE_CREATED
    val timestamp: Long,
    val metadata: String? = null, // JSON with non-PII data
    val retentionDays: Int = 30 // Auto-delete after 30 days
)

class AnalyticsRepository(
    private val analyticsDao: AnalyticsDao
) {
    suspend fun logEvent(eventType: String, metadata: Map<String, Any> = emptyMap()) {
        // Never log PII (cow names, farmer details, locations)
        val safeMetadata = metadata.filterKeys { key ->
            key !in listOf("name", "location", "phone", "email")
        }
        
        analyticsDao.insert(UsageAnalyticsEntity(
            eventType = eventType,
            timestamp = System.currentTimeMillis(),
            metadata = Json.encodeToString(safeMetadata)
        ))
    }
    
    suspend fun getMonthlyReport(): AnalyticsReport {
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
        val events = analyticsDao.getEventsSince(thirtyDaysAgo)
        
        return AnalyticsReport(
            totalProfilesCreated = events.count { it.eventType == "PROFILE_CREATED" },
            totalRecipesGenerated = events.count { it.eventType == "RECIPE_GENERATED" },
            averageDailySessions = events.count { it.eventType == "SCREEN_VIEW" } / 30,
            mostViewedScreen = events
                .filter { it.eventType == "SCREEN_VIEW" }
                .groupingBy { it.metadata }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key
        )
    }
}
```

**Crashlytics (Opt-in):**
```kotlin
// Only enable if user opts in during onboarding
if (userPreferences.crashReportingEnabled) {
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
}
```

---

## 12. FUTURE ROADMAP (PHASE 2+)

### 12.1 Phase 2 Features (6-12 months)
- **Multi-language Expansion:** Marathi, Punjabi, Tamil, Telugu, Gujarati
- **Voice Input:** Record cow details via voice (speech-to-text)
- **Veterinary Consultation:** In-app chat with vets (text + image sharing)
- **Weather Integration:** Adjust grazing recommendations based on local weather
- **Community Forum:** Farmer-to-farmer advice exchange (moderated)
- **Milk Production Tracker:** Graph milk yield trends over time

### 12.2 Phase 3 Features (12-24 months)
- **Multi-Cow Dashboard:** Manage 10+ cows on one screen (for large-scale farmers)
- **Bulk Feed Ordering:** Direct integration with local feed suppliers
- **AI Health Diagnosis:** Upload cow photos to detect diseases (mastitis, FMD symptoms)
- **Cloud Sync:** Cross-device backup (opt-in, encrypted)
- **Financial Tracking:** Track income (milk sales) vs. expenses (feed costs)
- **Insurance Integration:** Connect with livestock insurance providers

### 12.3 Phase 4 Features (24+ months)
- **IoT Integration:** Sync with smart cattle collars (activity tracking, heat detection)
- **Breed-Specific Tips:** Customized advice for 20+ Indian cattle breeds
- **AR Grass Identification:** Point camera at grass to identify species
- **Offline Video Library:** 100+ "How-to" videos (downloadable on WiFi)
- **Government Scheme Integration:** Auto-apply for subsidies based on herd data

---

## 13. CONCLUSION

This comprehensive SOP outlines the development of **Pashu-Aahar v3.0**, incorporating all feedback from the second review:

✅ **Farmer Level Segmentation**: Beginner, Intermediate, Large Scale  
✅ **Detailed Cattle Bio-data**: BMI, Weight, Pregnancy, Lactation tracking  
✅ **70:30 Visual-to-Text Ratio**: High-quality images, minimal text  
✅ **Offline-First Architecture**: 100% functionality without internet  
✅ **7-Day Data Recycling**: Auto-update prices and nutritional data  
✅ **Grazing Module**: Grass identification and nutritional values  
✅ **Vaccination Reminders**: Local push notifications  
✅ **Beautiful, Easy Navigation**: Large touch targets, intuitive UI

**Next Steps:**
1. **UI/UX Review**: Validate wireframes with 10 farmers
2. **Technical Prototype**: Build core navigation + one complete flow
3. **User Testing**: Conduct usability tests with target audience
4. **Iterate & Refine**: Based on real farmer feedback

---

**Document Owner:** Shreyas S Rai  
**Last Updated:** may 2026 
**Version:** 3.0 (Post-Review Revision)
