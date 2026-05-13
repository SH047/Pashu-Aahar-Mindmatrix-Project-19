package com.mindmatrix.pashuaahar.domain

enum class AppLanguage(
    val code: String,
    val title: String,
    val greeting: String
) {
    English("en", "English", "Hello"),
    Kannada("kn", "Kannada", "Namaskara")
}
