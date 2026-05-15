package com.mindmatrix.pashuaahar.data

import com.mindmatrix.pashuaahar.domain.FeedIngredient

object LocalMarketRepository {
    val energySources = listOf(
        FeedIngredient(
            id = "maize_makka",
            name = "Maize (Makka)",
            category = "Energy Sources",
            crudeProteinPercentage = 9.0,
            energyKcal = 3300,
            costPerKgInINR = 22.0
        ),
        FeedIngredient(
            id = "rice_bran_deoiled",
            name = "Rice Bran (De-oiled)",
            category = "Energy Sources",
            crudeProteinPercentage = 14.0,
            energyKcal = 2400,
            costPerKgInINR = 16.0
        ),
        FeedIngredient(
            id = "wheat_bran_godi_thowda",
            name = "Wheat Bran (Godi Thowda)",
            category = "Energy Sources",
            crudeProteinPercentage = 14.0,
            energyKcal = 2300,
            costPerKgInINR = 20.0
        )
    )

    val proteinSources = listOf(
        FeedIngredient(
            id = "cottonseed_cake_hatti_kaalu",
            name = "Cottonseed Cake (Hatti Kaalu)",
            category = "Protein Sources",
            crudeProteinPercentage = 25.0,
            energyKcal = 2800,
            costPerKgInINR = 32.0
        ),
        FeedIngredient(
            id = "groundnut_cake_kadlekai_peere",
            name = "Groundnut Cake (Kadlekai Peere)",
            category = "Protein Sources",
            crudeProteinPercentage = 42.0,
            energyKcal = 3100,
            costPerKgInINR = 45.0
        ),
        FeedIngredient(
            id = "mustard_cake_sarson_khali",
            name = "Mustard Cake (Sarson Khali)",
            category = "Protein Sources",
            crudeProteinPercentage = 35.0,
            energyKcal = 2900,
            costPerKgInINR = 28.0
        ),
        FeedIngredient(
            id = "soyabean_meal",
            name = "Soyabean Meal",
            category = "Protein Sources",
            crudeProteinPercentage = 46.0,
            energyKcal = 3200,
            costPerKgInINR = 48.0
        )
    )

    val fodder = listOf(
        FeedIngredient(
            id = "super_napier_green",
            name = "Super Napier (Green)",
            category = "Fodder",
            crudeProteinPercentage = 17.0,
            energyKcal = 1800,
            costPerKgInINR = 2.0
        ),
        FeedIngredient(
            id = "lucerne_alfalfa",
            name = "Lucerne/Alfalfa",
            category = "Fodder",
            crudeProteinPercentage = 20.0,
            energyKcal = 2100,
            costPerKgInINR = 6.0
        ),
        FeedIngredient(
            id = "paddy_straw_dry",
            name = "Paddy Straw (Dry)",
            category = "Fodder",
            crudeProteinPercentage = 3.0,
            energyKcal = 1200,
            costPerKgInINR = 7.0
        )
    )

    val commercialReference = FeedIngredient(
        id = "commercial_pellet_kmf",
        name = "Standard Market Pellet (KMF Style)",
        category = "Commercial",
        crudeProteinPercentage = 20.0,
        energyKcal = 2600,
        costPerKgInINR = 28.0
    )

    val allIngredients = energySources + proteinSources + fodder + listOf(commercialReference)
}
