package com.mindmatrix.pashuaahar.data

import com.mindmatrix.pashuaahar.domain.FeedIngredient
import com.mindmatrix.pashuaahar.domain.GrazingGrass
import com.mindmatrix.pashuaahar.domain.VaccinationItem

object SeedData {
    val ingredients = listOf(
        // 1. High-Yield Fodder Grasses (The Energy Base)
        FeedIngredient(
            id = "super_napier",
            name = "Super Napier (King)",
            category = "Energy Grasses",
            crudeProteinPercentage = 16.0,
            energyKcal = 2400,
            costPerKgInINR = 4.0,
            visualHint = "🌿 Hybrid King of Fodders",
            nutrientDensity = 0.7f,
            healthBenefit = "Massive yield & easy digestion"
        ),
        FeedIngredient(
            id = "guinea_grass",
            name = "Guinea Grass",
            category = "Energy Grasses",
            crudeProteinPercentage = 10.0,
            energyKcal = 2000,
            costPerKgInINR = 3.5,
            visualHint = "🍃 Shade-tolerant & leafy",
            nutrientDensity = 0.6f,
            healthBenefit = "Increases milk fat content"
        ),
        FeedIngredient(
            id = "rhodes_grass",
            name = "Rhodes Grass",
            category = "Energy Grasses",
            crudeProteinPercentage = 8.0,
            energyKcal = 1800,
            costPerKgInINR = 4.5,
            visualHint = "🌾 Drought-resistant hay",
            nutrientDensity = 0.5f,
            healthBenefit = "Crucial for rumen function"
        ),

        // 2. Protein-Rich Legumes (The Milk Builders)
        FeedIngredient(
            id = "lucerne",
            name = "Lucerne (Queen)",
            category = "Milk Builders",
            crudeProteinPercentage = 20.0,
            energyKcal = 2400,
            costPerKgInINR = 8.0,
            visualHint = "🍀 Queen of Forages",
            nutrientDensity = 0.9f,
            healthBenefit = "Packed with protein & calcium"
        ),
        FeedIngredient(
            id = "berseem",
            name = "Berseem (Clover)",
            category = "Milk Builders",
            crudeProteinPercentage = 18.0,
            energyKcal = 2200,
            costPerKgInINR = 6.0,
            visualHint = "☘️ Winter nutrient powerhouse",
            nutrientDensity = 0.85f,
            healthBenefit = "Increases daily milk yield"
        ),
        FeedIngredient(
            id = "cowpea",
            name = "Cowpea (Lobia)",
            category = "Milk Builders",
            crudeProteinPercentage = 16.0,
            energyKcal = 2100,
            costPerKgInINR = 5.5,
            visualHint = "🌱 Fast-growing legume",
            nutrientDensity = 0.8f,
            healthBenefit = "Highly digestible protein"
        ),

        // 3. Fodder Trees & Shrubs (The Nutrient Boosters)
        FeedIngredient(
            id = "moringa",
            name = "Moringa Leaves",
            category = "Nutrient Boosters",
            crudeProteinPercentage = 27.0,
            energyKcal = 2200,
            costPerKgInINR = 10.0,
            visualHint = "🍃 Miracle Superfood",
            nutrientDensity = 0.95f,
            healthBenefit = "Boosts immunity & immunity"
        ),
        FeedIngredient(
            id = "subabul",
            name = "Subabul (Leucaena)",
            category = "Nutrient Boosters",
            crudeProteinPercentage = 25.0,
            energyKcal = 2300,
            costPerKgInINR = 3.0,
            visualHint = "🌳 High protein tree leaves",
            nutrientDensity = 0.8f,
            healthBenefit = "Note: Feed in moderation (<30%)"
        ),

        // 4. Alternative "Super" Supplements
        FeedIngredient(
            id = "azolla",
            name = "Azolla (Water Fern)",
            category = "Modern Supplements",
            crudeProteinPercentage = 30.0,
            energyKcal = 2100,
            costPerKgInINR = 2.0,
            visualHint = "🦠 Aquatic nutritional powerhouse",
            nutrientDensity = 0.9f,
            healthBenefit = "Rich in Vitamin B12 & minerals"
        ),
        FeedIngredient(
            id = "hydro_maize",
            name = "Hydroponic Maize",
            category = "Modern Supplements",
            crudeProteinPercentage = 14.0,
            energyKcal = 3000,
            costPerKgInINR = 7.0,
            visualHint = "🧺 Fresh 7-day soil-less fodder",
            nutrientDensity = 0.85f,
            healthBenefit = "Rich in digestive enzymes"
        ),

        // 5. Energy-Rich Brans (The Fillers)
        FeedIngredient(
            id = "rice_bran",
            name = "Rice Bran (Akki Thowda)",
            category = "Market Staples",
            crudeProteinPercentage = 12.0,
            energyKcal = 2200,
            costPerKgInINR = 12.0,
            visualHint = "🍚 Common energy filler",
            nutrientDensity = 0.4f,
            healthBenefit = "Cheap fatty energy source"
        ),
        FeedIngredient(
            id = "wheat_bran",
            name = "Wheat Bran (Godi Thowda)",
            category = "Market Staples",
            crudeProteinPercentage = 15.0,
            energyKcal = 2000,
            costPerKgInINR = 18.0,
            visualHint = "🌾 Gold standard for fiber",
            nutrientDensity = 0.5f,
            healthBenefit = "Acts as a mild laxative"
        ),

        // 6. Protein-Heavy Oil Cakes
        FeedIngredient(
            id = "cottonseed_cake_staple",
            name = "Cottonseed Cake (Hatti Kaalu)",
            category = "Market Staples",
            crudeProteinPercentage = 25.0,
            energyKcal = 2400,
            costPerKgInINR = 28.0,
            visualHint = "🌻 Popular fat booster",
            nutrientDensity = 0.7f,
            healthBenefit = "Makes milk thicker (more fat)"
        ),
        FeedIngredient(
            id = "groundnut_cake_staple",
            name = "Groundnut Cake (Kadlekai)",
            category = "Market Staples",
            crudeProteinPercentage = 42.0,
            energyKcal = 2600,
            costPerKgInINR = 35.0,
            visualHint = "🥜 Recovery protein",
            nutrientDensity = 0.8f,
            healthBenefit = "Recovery after calving"
        ),

        // 7. Pulse By-products (Chunni)
        FeedIngredient(
            id = "tur_dal_chunni",
            name = "Tur Dal Chunni",
            category = "Market Staples",
            crudeProteinPercentage = 18.0,
            energyKcal = 2100,
            costPerKgInINR = 22.0,
            visualHint = "🥣 Broken pulse husks",
            nutrientDensity = 0.6f,
            healthBenefit = "Highly digestible protein/fiber"
        ),

        // 8. Balanced Commercial Pellets
        FeedIngredient(
            id = "kmf_pellets",
            name = "KMF Pellets (Nandini)",
            category = "Market Staples",
            crudeProteinPercentage = 20.0,
            energyKcal = 2500,
            costPerKgInINR = 26.0,
            visualHint = "📦 Scientifically balanced",
            nutrientDensity = 0.8f,
            healthBenefit = "Bypass protein & minerals"
        ),

        FeedIngredient(
            id = "mineral_mix",
            name = "Mineral mixture",
            category = "Supplement",
            crudeProteinPercentage = 0.0,
            energyKcal = 0,
            costPerKgInINR = 52.0,
            visualHint = "⚪ Daily mineral support",
            nutrientDensity = 0.9f,
            healthBenefit = "Bone & milk strength"
        )
    )

    val grasses = listOf(
        GrazingGrass(
            name = "Napier grass",
            season = "Monsoon and irrigated plots",
            proteinPercent = 10.5f,
            waterNeed = "Medium",
            recommendation = "Best for cut-and-carry feeding when chopped into small pieces."
        ),
        GrazingGrass(
            name = "Para grass",
            season = "Wet lowland areas",
            proteinPercent = 8.0f,
            waterNeed = "High",
            recommendation = "Useful during wet months; mix with dry fodder to avoid loose dung."
        ),
        GrazingGrass(
            name = "Lucerne",
            season = "Cool season",
            proteinPercent = 18.0f,
            waterNeed = "Medium",
            recommendation = "Excellent protein source; introduce slowly to prevent bloating."
        )
    )

    val vaccinations = listOf(
        VaccinationItem(
            name = "Foot and mouth disease",
            dueWindow = "Every 6 months",
            priority = "High",
            note = "Keep a village-level reminder with the dairy cooperative."
        ),
        VaccinationItem(
            name = "Haemorrhagic septicaemia",
            dueWindow = "Before monsoon",
            priority = "High",
            note = "Important in flood-prone and humid regions."
        ),
        VaccinationItem(
            name = "Black quarter",
            dueWindow = "Yearly",
            priority = "Medium",
            note = "Usually given before monsoon for young and grazing cattle."
        )
    )
}
