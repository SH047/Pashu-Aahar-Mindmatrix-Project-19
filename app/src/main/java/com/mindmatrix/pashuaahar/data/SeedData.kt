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
            proteinPercent = 16f,
            energyMcalPerKg = 2.4f,
            pricePerKg = 4.0f,
            visualHint = "🌿 Hybrid King of Fodders",
            nutrientDensity = 0.7f,
            healthBenefit = "Massive yield & easy digestion"
        ),
        FeedIngredient(
            id = "guinea_grass",
            name = "Guinea Grass",
            category = "Energy Grasses",
            proteinPercent = 10f,
            energyMcalPerKg = 2.0f,
            pricePerKg = 3.5f,
            visualHint = "🍃 Shade-tolerant & leafy",
            nutrientDensity = 0.6f,
            healthBenefit = "Increases milk fat content"
        ),
        FeedIngredient(
            id = "rhodes_grass",
            name = "Rhodes Grass",
            category = "Energy Grasses",
            proteinPercent = 8f,
            energyMcalPerKg = 1.8f,
            pricePerKg = 4.5f,
            visualHint = "🌾 Drought-resistant hay",
            nutrientDensity = 0.5f,
            healthBenefit = "Crucial for rumen function"
        ),

        // 2. Protein-Rich Legumes (The Milk Builders)
        FeedIngredient(
            id = "lucerne",
            name = "Lucerne (Queen)",
            category = "Milk Builders",
            proteinPercent = 20f,
            energyMcalPerKg = 2.4f,
            pricePerKg = 8f,
            visualHint = "🍀 Queen of Forages",
            nutrientDensity = 0.9f,
            healthBenefit = "Packed with protein & calcium"
        ),
        FeedIngredient(
            id = "berseem",
            name = "Berseem (Clover)",
            category = "Milk Builders",
            proteinPercent = 18f,
            energyMcalPerKg = 2.2f,
            pricePerKg = 6f,
            visualHint = "☘️ Winter nutrient powerhouse",
            nutrientDensity = 0.85f,
            healthBenefit = "Increases daily milk yield"
        ),
        FeedIngredient(
            id = "cowpea",
            name = "Cowpea (Lobia)",
            category = "Milk Builders",
            proteinPercent = 16f,
            energyMcalPerKg = 2.1f,
            pricePerKg = 5.5f,
            visualHint = "🌱 Fast-growing legume",
            nutrientDensity = 0.8f,
            healthBenefit = "Highly digestible protein"
        ),

        // 3. Fodder Trees & Shrubs (The Nutrient Boosters)
        FeedIngredient(
            id = "moringa",
            name = "Moringa Leaves",
            category = "Nutrient Boosters",
            proteinPercent = 27f,
            energyMcalPerKg = 2.2f,
            pricePerKg = 10f,
            visualHint = "🍃 Miracle Superfood",
            nutrientDensity = 0.95f,
            healthBenefit = "Boosts immunity & immunity"
        ),
        FeedIngredient(
            id = "subabul",
            name = "Subabul (Leucaena)",
            category = "Nutrient Boosters",
            proteinPercent = 25f,
            energyMcalPerKg = 2.3f,
            pricePerKg = 3f,
            visualHint = "🌳 High protein tree leaves",
            nutrientDensity = 0.8f,
            healthBenefit = "Note: Feed in moderation (<30%)"
        ),

        // 4. Alternative "Super" Supplements
        FeedIngredient(
            id = "azolla",
            name = "Azolla (Water Fern)",
            category = "Modern Supplements",
            proteinPercent = 30f,
            energyMcalPerKg = 2.1f,
            pricePerKg = 2f,
            visualHint = "🦠 Aquatic nutritional powerhouse",
            nutrientDensity = 0.9f,
            healthBenefit = "Rich in Vitamin B12 & minerals"
        ),
        FeedIngredient(
            id = "hydro_maize",
            name = "Hydroponic Maize",
            category = "Modern Supplements",
            proteinPercent = 14f,
            energyMcalPerKg = 3.0f,
            pricePerKg = 7f,
            visualHint = "🧺 Fresh 7-day soil-less fodder",
            nutrientDensity = 0.85f,
            healthBenefit = "Rich in digestive enzymes"
        ),

        // 5. Energy-Rich Brans (The Fillers)
        FeedIngredient(
            id = "rice_bran",
            name = "Rice Bran (Akki Thowda)",
            category = "Market Staples",
            proteinPercent = 12f,
            energyMcalPerKg = 2.2f,
            pricePerKg = 12f,
            visualHint = "🍚 Common energy filler",
            nutrientDensity = 0.4f,
            healthBenefit = "Cheap fatty energy source"
        ),
        FeedIngredient(
            id = "wheat_bran",
            name = "Wheat Bran (Godi Thowda)",
            category = "Market Staples",
            proteinPercent = 15f,
            energyMcalPerKg = 2.0f,
            pricePerKg = 18f,
            visualHint = "🌾 Gold standard for fiber",
            nutrientDensity = 0.5f,
            healthBenefit = "Acts as a mild laxative"
        ),

        // 6. Protein-Heavy Oil Cakes
        FeedIngredient(
            id = "cottonseed_cake_staple",
            name = "Cottonseed Cake (Hatti Kaalu)",
            category = "Market Staples",
            proteinPercent = 25f,
            energyMcalPerKg = 2.4f,
            pricePerKg = 28f,
            visualHint = "🌻 Popular fat booster",
            nutrientDensity = 0.7f,
            healthBenefit = "Makes milk thicker (more fat)"
        ),
        FeedIngredient(
            id = "groundnut_cake_staple",
            name = "Groundnut Cake (Kadlekai)",
            category = "Market Staples",
            proteinPercent = 42f,
            energyMcalPerKg = 2.6f,
            pricePerKg = 35f,
            visualHint = "🥜 Recovery protein",
            nutrientDensity = 0.8f,
            healthBenefit = "Recovery after calving"
        ),

        // 7. Pulse By-products (Chunni)
        FeedIngredient(
            id = "tur_dal_chunni",
            name = "Tur Dal Chunni",
            category = "Market Staples",
            proteinPercent = 18f,
            energyMcalPerKg = 2.1f,
            pricePerKg = 22f,
            visualHint = "🥣 Broken pulse husks",
            nutrientDensity = 0.6f,
            healthBenefit = "Highly digestible protein/fiber"
        ),

        // 8. Balanced Commercial Pellets
        FeedIngredient(
            id = "kmf_pellets",
            name = "KMF Pellets (Nandini)",
            category = "Market Staples",
            proteinPercent = 20f,
            energyMcalPerKg = 2.5f,
            pricePerKg = 26f,
            visualHint = "📦 Scientifically balanced",
            nutrientDensity = 0.8f,
            healthBenefit = "Bypass protein & minerals"
        ),

        FeedIngredient(
            id = "mineral_mix",
            name = "Mineral mixture",
            category = "Supplement",
            proteinPercent = 0f,
            energyMcalPerKg = 0f,
            pricePerKg = 52f,
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
