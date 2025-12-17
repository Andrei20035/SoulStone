# 🪨 SoulStone

**SoulStone** is a multilingual, offline‑first Android application built as an end‑to‑end showcase of modern Android development with **Kotlin** and **Jetpack Compose**. The project focuses on clean architecture, reactive data flows, robust internationalization, and efficient image handling—mirroring real‑world production requirements rather than toy examples.

This repository is intentionally structured and documented to be easy to navigate for **recruiters, reviewers, and fellow developers**.

---

## ✨ What this project demonstrates

SoulStone highlights how I design, architect, and ship a feature‑rich Android app from scratch:

* **UI**: Jetpack Compose with state hoisted through `ViewModel`s and exposed via Kotlin `Flow`
* **Architecture**: Clear separation of **data**, **domain**, and **presentation** layers
* **Persistence**: Room with a normalized schema, translation tables, and typed DAO projections
* **Dependency Injection**: Hilt for constructor injection across the entire app
* **Concurrency**: Coroutines + Flows with strict `Dispatchers.IO` confinement for heavy work
* **Internationalization (i18n)**: Hybrid strategy using both Android resources and dynamic DB‑stored translations
* **Networking**: Retrofit integration with Google Translate API for automated content localization
* **Images**: Coil v3 with a smart runtime strategy (drawable resource vs local file)
* **Offline‑first**: Fully usable without network after first launch

---

## 🧱 Architecture overview

The project follows a clean, scalable structure:

```
app/src/main/java/com/example/soulstone/
│
├── ui/           # Compose screens, components, navigation
├── data/         # Room entities, DAOs, repositories, Retrofit services
├── domain/       # Domain models (UI‑friendly, persistence‑agnostic)
├── util/         # Helpers, language handling, composition locals
└── di/           # Hilt modules
```

* **ViewModels** expose immutable UI state and observe repositories via `Flow`
* **Repositories** act as the single source of truth
* **DAOs** return purpose‑built projections (translated POJOs) to minimize UI mapping logic

---

## 🌍 Multilingual foundation (static + dynamic)

### Static localization (Android resources)

UI strings and static labels are localized using standard Android resource qualifiers:

```
res/
├── values/
├── values-es/
├── values-fr/
├── values-it/
├── values-de/
├── values-pl/
└── values-ru/
```

A strongly‑typed `LanguageCode` enum models supported languages and their flags, while a `CompositionLocal` (`LocalLanguage`) provides the currently selected language across Compose.

### Dynamic translations (Room)

Real app content—such as **stones, benefits, chakras, and zodiac signs**—is translated and persisted in Room:

* Dedicated `*Translation` tables
* Uniqueness enforced via `(entityId, languageCode)` indices
* DAOs expose reactive, localized projections like `TranslatedStone`

This ensures:

* Full offline support
* Consistent multilingual data rendering
* Easy future language expansion

---

## 🗄️ Database seeding & offline readiness

On first launch, the database is populated from structured JSON files located in `assets/`:

* Executed on `Dispatchers.IO`
* Entities and their translations are inserted together
* Cross‑reference tables are linked after base inserts

Once seeding is complete, the app is fully usable **without any network connection**.

---

## 🌐 Google Translate API integration

To streamline content creation, adding a new stone automatically generates translations:

* Retrofit service wrapping Google Translate API
* Executed entirely on `Dispatchers.IO`
* HTML entities returned by the API are sanitized
* Graceful fallback to English if the API fails or is unavailable

Result: every new stone is immediately available in all supported languages and stored locally.

---

## 🖼️ Image loading strategy

SoulStone stores **only the image name** in the database. At runtime, the app decides:

* Use a packaged **drawable resource**, or
* Load a **local file from app storage**

This resolution logic runs off the main thread and integrates seamlessly with **Coil v3**:

* Automatic caching & decoding
* Smooth crossfade transitions
* Compose‑friendly API

This approach keeps the database clean while remaining flexible and performant.

---

## 🔧 Tech stack

* **Language**: Kotlin
* **UI**: Jetpack Compose
* **Architecture**: MVVM + Repository pattern
* **DI**: Hilt
* **Persistence**: Room
* **Networking**: Retrofit
* **Concurrency**: Coroutines & Flow
* **Images**: Coil v3
* **Build system**: Gradle (KTS)

---

## ▶️ Getting started

### Requirements

* **Android Studio (latest stable)**
* **JDK 17**
* Android SDK 34+

### Emulator configuration (IMPORTANT)

> ⚠️ **UI DISCLAIMER**
>
> SoulStone’s UI is designed and tested **exclusively for a 24‑inch Full HD (1920×1080) emulator**.
> On smaller screens, layouts may appear compressed or misaligned.

#### Recommended emulator setup

* Device profile: **Custom / Desktop‑like**
* Resolution: **1920 × 1080**
* Density: ~**320 dpi**
* Orientation: **Landscape**

In Android Studio:

1. Open **Device Manager**
2. Create a new virtual device
3. Choose **Custom Hardware Profile**
4. Set resolution to **1920×1080**
5. Run the app on this emulator

### Running the app

```bash
git clone https://github.com/your-username/soulstone.git
cd soulstone
```

* Open the project in Android Studio
* Sync Gradle
* Add your **Google Translate API key** (if testing content creation)
* Run the app on the recommended emulator

> The app works fully offline once initial seeding is complete.

---

## 🧠 Why this project matters

* **Real multilingual architecture** — not just translated UI labels, but persisted, queryable localized content
* **Production‑grade threading** — no heavy work on the main thread
* **Scalable data model** — normalized schema with clean cross‑refs
* **Modern Android stack** — Compose, Hilt, Room, Retrofit, Coroutines
* **Clean, maintainable codebase** — easy to extend and test

---

## 📌 Summary

SoulStone is a modern Android application engineered to be multilingual at its core. It combines platform‑native localization with dynamic Room‑backed translations, automated via Google Translate API. With offline‑first persistence, smart image handling, and a clean Compose‑based UI architecture, the project demonstrates how I approach real‑world Android development—from data modeling to polished UX.

---

If you’re reviewing this as a recruiter or developer: thank you for taking the time to explore the codebase.
