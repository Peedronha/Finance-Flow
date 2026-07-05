# Financeflow

Financeflow is an Android application for personal finance management focused on minimalist design and systematic planning. It implements a high-utility interface to help users track transactions and monitor financial health through the 50/30/20 rule.

## Features

- Transaction Management: Record income and categorized expenses (Fixed, Variable, Investment).
- Financial Dashboard: Real-time visualization of spending distribution and budget limits.
- Automated Goals: Automatic calculation of emergency reserves (6 months of income) and financial independence milestones.
- Persistence: User preferences for dark mode and custom financial goals managed via Jetpack DataStore.
- Design System: Minimalist aesthetic inspired by architectural restraint, utilizing a warm gallery palette and fluid grids.

## Tech Stack

- Language: Kotlin
- Architecture: MVVM (Model-View-ViewModel)
- Database: Room Persistence Library
- Preferences: Jetpack DataStore
- UI: Material 3, View Binding, ConstraintLayout
- Graphics: MPAndroidChart
- Asynchrony: Kotlin Coroutines and Flow

## Design Specifications

The project follows a specific design system defined in documentation/DESIGN.md, characterized by:
- Background: Soft off-white (#FCFCFA) for reduced eye strain.
- Typography: Systematic use of Inter font with specific tracking for financial data.
- Layout: 24px horizontal margins and tonal layering instead of traditional elevation.
- Components: High-radius curvature (24px-29px) for a modern tactile feel.

## Requirements

- Android Studio Koala or higher.
- Android SDK 34.
- Gradle 8.x.