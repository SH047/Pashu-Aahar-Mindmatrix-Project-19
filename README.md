# Pashu Aahar

Pashu Aahar is an Android app built for the MindMatrix internship project to help dairy farmers plan cattle nutrition, compare homemade feed with market feed, and manage herd health with a simple offline-friendly interface.

## Demo

Try the app in the browser using Appetize:

[https://appetize.io/app/b_ztsk75xhtcg67de6eooeme26vm](https://appetize.io/app/b_ztsk75xhtcg67de6eooeme26vm)

## Screenshots / Preview

- Interactive demo: [Appetize live emulator](https://appetize.io/app/b_ztsk75xhtcg67de6eooeme26vm)
- Repository: [GitHub project link](https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19)

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

## Usage Flow

1. Open the app and finish onboarding.
2. Select your preferred language and farmer level.
3. Add or manage cattle profiles in the herd section.
4. Review the daily nutrition planner for the selected cow.
5. Customize the ration using grazing hours and local ingredients.
6. Check the savings dashboard to compare homemade feed with market feed.
7. Use the health screen for body condition guidance and daily care follow-up.

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

## Key Modules

- `data/`: local repository, onboarding preferences, ingredient datasets
- `domain/`: nutrition calculator, health calculator, savings engine, models
- `presentation/screens/`: onboarding, dashboard, herd, nutrition, health, savings
- `presentation/components/`: reusable UI components and visual building blocks

## Source Code

The complete source code is included in this repository.

- Main Android source: `app/src/main/java/com/mindmatrix/pashuaahar/`
- Resources and UI strings: `app/src/main/res/`
- Unit tests: `app/src/test/java/com/mindmatrix/pashuaahar/`
- Gradle configuration: `build.gradle.kts`, `settings.gradle.kts`, `app/build.gradle.kts`

## Setup Requirements

- Android Studio with Android SDK 34
- JDK 17
- Android device or emulator

## Installation

1. Clone the repository:

```bash
git clone https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19.git
cd Pashu-Aahar-Mindmatrix-Project-19
```

2. Open the project in Android Studio.

3. Let Gradle sync all dependencies.

## Setup and Run

1. Start an Android emulator or connect a physical Android device.
2. Open the `app` run configuration in Android Studio.
3. Click `Run` to install and launch the application.

## Command Line Build

```bash
./gradlew test
./gradlew assembleDebug
```

For a clean verification run:

```bash
./gradlew clean test assembleDebug
```

On Windows, use:

```bat
gradlew.bat test
gradlew.bat assembleDebug
```

## Verified Build Status

- Unit tests verified locally with `./gradlew test` on May 15, 2026
- Debug build verified locally with `./gradlew assembleDebug` on May 15, 2026

## Important Dependencies Included

This repository includes the files typically expected for a complete Android submission:

- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `gradlew` and `gradlew.bat`
- Gradle wrapper files under `gradle/wrapper/`
- Android app module under `app/`

## Why This Project is Custom

This repository contains project-specific implementation for a real dairy farming use case:

- nutrition calculation based on cattle profile and milk yield
- local feed ingredient catalogue with cost and protein data
- custom feed mixing workflow
- homemade versus market feed cost comparison
- herd profile management
- health scoring and daily care tracking

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

## Future Improvements

- Add more Indian language options
- Store and compare past feed plans over time
- Add richer analytics for milk yield trends
- Add export and sharing options for farmers and cooperatives
- Support more region-specific ingredient pricing

## Known Notes

- The app currently targets Android API 34.
- Firebase Analytics is configured in the project.
- Core app logic is designed to work without requiring continuous internet access.

## Repository Link

Submission repository:

[https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19](https://github.com/SH047/Pashu-Aahar-Mindmatrix-Project-19)

## Author

Shreyas S Rai
USN: 1RR22RA023
