### SoulStone — Multilingual Stones Catalog for Retailers

A production-ready Android app for a stones and crystals retailer. It provides a beautiful, fast, and fully offline-capable catalog that organizes stones by benefits, chakras, and zodiac signs (Western and Chinese). The app is built with a modular, testable architecture, supports seven languages out of the box, and is designed to grow into a full admin-backed platform where the business owner can manage inventory, descriptions, and more.

---

### What the app does

- Browse stones with localized names and descriptions in 7 languages
- Explore stones by:
  - Benefits (e.g., sleep, clarity, protection)
  - Seven Chakras
  - Western zodiac monthly birthstones
  - Chinese zodiac birthstones
- See each stone’s associations (benefits, chakras, zodiac signs)
- Fast, offline-first experience thanks to a local database pre-populated on first run
- Change app language instantly from the app bar

Planned (Admin Panel):
- Secure admin area to:
  - Add/edit stones, benefits, chakra properties, and zodiac associations
  - Edit descriptions and media
  - Manage stock/availability and featured items
  - Control which fields are auto-translated
- Automatic multi-language content via Google Translate API: when the admin adds a new stone in English (or any primary language), the app will auto-translate the text into the other 6 languages, while allowing manual overrides per field.

---

### Architecture overview

The project follows a layered, testable architecture with clear separation of concerns:

- UI (Jetpack Compose + ViewModel)
  - Files like `StoneUsesScreen.kt`, `SevenChakraScreen.kt`, `ChineseBirthstonesScreen.kt`, `HomeScreen.kt`
  - State holders: `StoneUsesViewModel`, `SevenChakraViewModel`, `ChineseViewModel`, `ZodiacViewModel`, and `AppBarViewModel`
  - Navigation and interaction are driven by `Flow`-based UI state

- Domain (Models)
  - Domain models mirror business concepts (Stone, Benefit, Chakra, Zodiac)
  - Keeps UI agnostic to persistence details

- Data (Room, DAOs, Repositories)
  - Entities and translation tables: `Stone`, `StoneTranslation`, `Benefit`, `BenefitTranslation`, `Chakra`, `ChakraTranslation`, `ZodiacSign`, `ZodiacSignTranslation`, `ChineseZodiacSign`, `ChineseZodiacSignTranslation`
  - Relation tables: `StoneBenefitCrossRef`, `StoneChakraCrossRef`, `StoneZodiacCrossRef`, `StoneChineseZodiacCrossRef`
  - DAOs return language-aware projections like `TranslatedStone`, `TranslatedBenefit`, etc. using a `LanguageCode` enum
  - Repositories (`StoneRepository`, `BenefitRepository`, `ChakraRepository`, `ZodiacSignRepository`, `ChineseZodiacSignRepository`) abstract DAOs and expose `Flow` for reactive updates

- Dependency Injection (Hilt)
  - `DatabaseModule` wires the `AppDatabase` and DAOs
  - ViewModels are annotated with `@HiltViewModel` for scoped injection

- Database bootstrap and data seeding
  - `AppDatabase` includes a `ZodiacDatabaseCallback` to seed initial data on first launch
  - Seeds include stones, chakras, zodiac signs, benefits, and their cross-refs from JSON in `assets/`
  - Ensures the app is instantly useful offline with full browsing capability

Why this architecture works well:
- Maintainability: small, focused classes with clear layer boundaries
- Testability: DAOs and Repositories are unit/instrumented tested; ViewModels keep logic out of Composables
- Performance: Room queries pre-join translations; `Flow` streams keep UI responsive
- Extensibility: adding admin and translation services fits naturally into the layers (see roadmap)

---

### Internationalization (7 languages)

The app ships with localized content via translation tables and a `LanguageCode` enum (e.g., `ENGLISH`, `SPANISH`, `FRENCH`, `ITALIAN`, `GERMAN`, `POLISH`, `RUSSIAN`). DAOs expose queries like `getAllTranslatedStones(language)` so the UI simply selects the active `LanguageCode` and receives already-localized rows. The language can be switched at runtime from the app bar.

Planned enhancement — Google Translate API integration:
- On create/update in the Admin Panel, the backend or an in-app service will call Google Translate for fields the admin marks as “auto-translate,” writing results into translation tables for all non-primary languages
- Admin can fine-tune or overwrite translations per field and per language

---

### Data model at a glance

- Core tables: `Stone`, `Benefit`, `Chakra`, `ZodiacSign`, `ChineseZodiacSign`
- Translation tables: `StoneTranslation`, `BenefitTranslation`, `ChakraTranslation`, `ZodiacSignTranslation`, `ChineseZodiacSignTranslation` with a `languageCode` discriminator
- Cross-reference tables define many-to-many relations between stones and benefits/chakras/zodiacs
- Seed JSONs in `app/src/main/assets`: `initial_stones.json`, `initial_benefits.json`, `initial_chakras.json`, `initial_zodiac_signs.json`, `initial_chinese_zodiac.json`, and association files for linking

This schema enables:
- Language-aware listing screens
- Detail pages with resolved associations
- Efficient filtering by benefit/chakra/zodiac

---

### Tech stack

- UI: Jetpack Compose
- State: Kotlin coroutines & Flow, AndroidX ViewModel
- DI: Hilt
- Persistence: Room with TypeConverters and pre-population callback
- JSON: Gson for data seeding
- Testing: Instrumented tests for DAOs and repositories

---

### Notable implementation details

- Language-first queries: DAOs project translated fields directly, avoiding costly post-processing and simplifying UI
- Single pass seeding: `ZodiacDatabaseCallback` performs a sequenced population to avoid referential issues, then links cross-refs
- Modular repositories: Repository interfaces define the contract; implementations wrap DAOs, easing substitution for future remote sources
- Runtime language switch: The app bar integrates with `AppBarViewModel` to set the active `LanguageCode` and reactively update screens

---

### Testing and quality

- DAO tests validate CRUD and translation lookups (e.g., `ChakraDaoTest`, `BenefitDaoTest`, `StoneDaoTest`)
- Repository tests verify language-filtered flows and association queries
- This foundation supports safe refactors when adding the Admin Panel and network features
---

### Folder highlights

- `app/src/main/java/com/example/soulstone/data` — Room entities, DAOs, relations, repositories
- `app/src/main/java/com/example/soulstone/ui` — Compose screens, app bar, navigation, ViewModels
- `app/src/main/java/com/example/soulstone/di` — Hilt modules (e.g., `DatabaseModule`)
- `app/src/main/assets` — Seed JSONs

---

### Why this is a great fit for a stones retailer

- Immediate value: Rich, multi-language catalog available offline for customers and staff
- Future-ready: Clean layering makes it straightforward to add admin, inventory, and translation automation
- Performance and UX: Fast local queries, reactive UI, and clear navigation across benefits, chakras, and zodiacs

---

### License and attribution

This project uses open-source Android and Jetpack components. Google Translate API will be used for future auto-translation features according to the service’s terms and billing.

---

### Summary

SoulStone is a polished, multilingual stones catalog app with a robust data model, a clean and testable architecture, and a clear path to evolve into a full admin-managed product with automated translations. It’s tailored for your client’s retail business and ready to scale.
