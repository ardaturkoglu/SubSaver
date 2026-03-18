# SubSaver - PROJECT.md

## Vision
A minimalist, ad-supported Android subscription tracker that helps users visualize recurring bills, avoid missed payments, and stay on budget. Designed as a solopreneur passive-income product optimized for 24-hour MVP deployment to Google Play.

## Business Model
- **Monetization**: Google AdMob (Banner on Dashboard, Interstitial on Add/Edit save)
- **Target**: Free app with ads; no premium tier in MVP
- **Revenue goal**: Passive income via ad impressions/clicks at scale

## Target User
Budget-conscious Android users (18-45) who subscribe to multiple streaming, SaaS, or utility services and want a dead-simple way to see "how much am I spending per month?"

## Tech Stack

| Layer | Choice |
|---|---|
| Language | Kotlin 2.x |
| UI | Jetpack Compose, Material 3, Material Icons Extended |
| Architecture | MVVM + StateFlow (single-module, feature-based packages) |
| Local DB | Room (SQLite) |
| DI | Manual (object-level singletons — no Hilt/Koin for MVP speed) |
| Navigation | Compose Navigation (single NavHost) |
| Notifications | AlarmManager + BroadcastReceiver (local only) |
| Ads | Google AdMob SDK (Banner + Interstitial) |
| Analytics | Firebase Analytics |
| Crash Reporting | Firebase Crashlytics |
| Min SDK | 31 (Android 12) |
| Target SDK | 36 |

## Architecture Principles
1. **Single module** — no multi-module overhead for MVP
2. **Feature-based packages** — `dashboard`, `subscription`, `notification`, `ads`, `common`
3. **No over-engineering** — no UseCases layer, no Repository interfaces (concrete only), no dependency injection framework
4. **StateFlow everywhere** — no LiveData, no RxJava
5. **Room as single source of truth** — observe via Flow, expose via StateFlow in ViewModels

## Package Structure
```
com.ardat.comsubsaverapp/
  MainActivity.kt
  SubSaverApp.kt              # Application class (Firebase init, Ad init)
  navigation/
    NavGraph.kt
    Screen.kt                  # Sealed class for routes
  feature/
    dashboard/
      DashboardScreen.kt
      DashboardViewModel.kt
    subscription/
      AddEditSubscriptionScreen.kt
      AddEditSubscriptionViewModel.kt
    settings/
      SettingsScreen.kt
  data/
    db/
      SubSaverDatabase.kt
      SubscriptionDao.kt
    model/
      Subscription.kt          # Room @Entity
      BillingCycle.kt           # Enum: WEEKLY, MONTHLY, YEARLY
      Category.kt               # Enum: Streaming, Music, Cloud, etc.
    repository/
      SubscriptionRepository.kt # Concrete class, no interface
  notification/
    NotificationScheduler.kt
    PaymentReminderReceiver.kt
  ads/
    AdManager.kt
    BannerAdView.kt             # Composable wrapper
  ui/
    theme/
      Theme.kt
      Color.kt
      Type.kt
    components/
      SubscriptionCard.kt
      SpendSummaryCard.kt
      CategoryChip.kt
      EmptyStateView.kt
```

## Constraints
- Must ship to Google Play within 24 hours of starting execution
- No backend / no auth / no cloud sync in MVP
- All data is local-only (Room)
- No onboarding flow in MVP — straight to dashboard
- Notification permission must be requested at runtime (Android 13+)

## Success Metrics (MVP)
- App installs > 100 in first week (organic + ASO)
- Crash-free rate > 99%
- Average session > 30s
- Ad fill rate > 80%
