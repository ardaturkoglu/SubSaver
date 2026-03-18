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
| Cloud Backend | Supabase Postgres (project provisioned; used for backend ops and sync-ready schema) |
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
6. **Local-first with sync-ready backend** — app works fully offline, Supabase integration is added incrementally without blocking MVP

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
- No auth flow in MVP (single-device usage first)
- App must remain fully usable offline (Room-first)
- No onboarding flow in MVP — straight to dashboard
- Notification permission must be requested at runtime (Android 13+)

## Supabase Project Context
- Organization: `ardaturkogluu's Org` (`niolszgotbdlgikaqyru`)
- Project name: `SubSaver`
- Project ID / ref: `ebiymaspbliujgfnubkm`
- Region: `ap-northeast-1`
- API URL: `https://ebiymaspbliujgfnubkm.supabase.co`
- Current DB status: healthy, no tables yet, no migrations yet

## Success Metrics (MVP)
- App installs > 100 in first week (organic + ASO)
- Crash-free rate > 99%
- Average session > 30s
- Ad fill rate > 80%
