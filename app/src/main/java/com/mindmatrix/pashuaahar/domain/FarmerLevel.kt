package com.mindmatrix.pashuaahar.domain

enum class FarmerLevel(
    val title: String,
    val localTitle: String,
    val herdRange: String,
    val description: String
) {
    Beginner(
        title = "Beginner",
        localTitle = "Small start",
        herdRange = "1-5 cows",
        description = "Step-by-step feeding guidance"
    ),
    Intermediate(
        title = "Intermediate",
        localTitle = "Growing shed",
        herdRange = "6-15 cows",
        description = "Cost and milk yield planning"
    ),
    LargeScale(
        title = "Large scale",
        localTitle = "Modern farm",
        herdRange = "15+ cows",
        description = "Bulk planning and herd view"
    )
}
