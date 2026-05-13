package com.mindmatrix.pashuaahar.domain

enum class DailyCareActivity(
    val title: String,
    val shortLabel: String
) {
    MorningFeed("Morning food", "M"),
    Grazing("Grazing", "G"),
    Hydration("Hydration", "H"),
    EveningNightFeed("Evening / night", "N")
}
