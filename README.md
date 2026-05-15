# Pashu Aahar

Pashu Aahar is an Android app built for the MindMatrix internship project to help dairy farmers plan cattle nutrition, compare homemade feed with market feed, and manage herd health with a simple offline-friendly interface.

## Live Demo

Try the app in the browser using Appetize:

[https://appetize.io/app/b_ztsk75xhtcg67de6eooeme26vm](https://appetize.io/app/b_ztsk75xhtcg67de6eooeme26vm)

## Problem Statement

Many small dairy farmers rely on rough estimates for cattle feeding. That can lead to:

- unbalanced nutrition
- higher feed costs
- reduced milk yield
- poor visibility into herd health and daily care

This project aims to make basic cattle nutrition planning easier and more practical through a mobile app designed around local feed ingredients and simple workflows.

## Features

- Farmer onboarding with language and farmer-level selection
- Herd management with cow profiles
- Daily nutrition planning based on cow profile data
- Custom feed mix builder using local market ingredients
- Savings dashboard comparing homemade feed with standard market feed
- Daily care tracking and body condition feedback
- Vaccination and reminder-style dashboard cards
- Offline-first local app flow for core features

## Tech Stack

- Kotlin
- Jetpack Compose
- Android ViewModel
- StateFlow / Compose state
- Android DataStore
- Firebase Analytics
- Gradle Kotlin DSL

## Project Structure

```text
app/src/main/java/com/mindmatrix/pashuaahar/
├── data/          # seed data, repositories, local preferences
├── domain/        # business models and nutrition/health logic
└── presentation/  # screens, UI components, theming, view model
```

## Setup Requirements

- Android Studio with Android SDK 34
- JDK 17
- Android device or emulator

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19.git
cd Pashu-Aahar-Mindmatrix-Project-19
```

2. Open the project in Android Studio.

3. Let Gradle sync all dependencies.

4. Run the app on an emulator or physical Android device.

## Command Line Build

```bash
./gradlew test
./gradlew assembleDebug
```

On Windows, use:

```bat
gradlew.bat test
gradlew.bat assembleDebug
```

## Verified Build Status

- Unit tests verified locally with `./gradlew test` on May 15, 2026

## Important Dependencies Included

This repository includes the files typically expected for a complete Android submission:

- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `gradlew` and `gradlew.bat`
- Gradle wrapper files under `gradle/wrapper/`
- Android app module under `app/`

## Originality Notes

This is not a default starter template. The app contains custom:

- cattle nutrition calculation logic
- body condition and health scoring
- local ingredient dataset
- herd and feed workflow screens
- homemade-vs-market savings comparison

## Screens Included

- Onboarding
- Dashboard
- Nutrition Planner
- Custom Feed / Mix workflow
- Savings Dashboard
- Farm Management
- Herd Management
- Health Tips

## Known Notes

- The app currently targets Android API 34.
- Firebase Analytics is configured in the project.
- Core app logic is designed to work without requiring continuous internet access.

## Repository Link

Submission repository:

[https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19](https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19)

## Author

Shreyas S Rai
