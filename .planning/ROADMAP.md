# SubSaver - ROADMAP.md

## Milestone 1: MVP (v1.0) — 24-Hour Ship Target

---

### Phase 1: Foundation — Data Layer + Theme + Navigation Shell + Supabase Baseline
**Goal**: Establish Room database, entities, DAO, repository, Material 3 theme, navigation skeleton, and Supabase project baseline.
**Estimated effort**: 2-3 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| Define `Subscription` entity, `BillingCycle` enum, `Category` enum | FR-10, FR-13, FR-14 | `model/` package |
| Create `SubscriptionDao` with CRUD + Flow queries | FR-01, FR-10, FR-11, FR-12 | `data/db/SubscriptionDao.kt` |
| Create `SubSaverDatabase` (Room) | FR-01 | `data/db/SubSaverDatabase.kt` |
| Create `SubscriptionRepository` (concrete) | FR-01, FR-02 | `data/repository/SubscriptionRepository.kt` |
| Verify Supabase project connection via MCP and document it | FR-50 | `.planning/PROJECT.md` |
| Create initial Supabase `subscriptions` schema migration | FR-51, FR-53, NFR-10 | Supabase migration |
| Configure Material 3 theme (colors, typography, dynamic color) | NFR-04, NFR-05, NFR-09 | `ui/theme/` |
| Set up Compose Navigation with Screen sealed class | FR-01, FR-10, FR-11 | `navigation/` |
| Create `SubSaverApp` Application class | NFR-01 | `SubSaverApp.kt` |

**Exit criteria**: App compiles, navigates between empty Dashboard and Add screens, Room DB created on first launch, Supabase project baseline is verified and documented.

---

### Phase 2: Dashboard Screen — List + Summary + Empty State
**Goal**: Build the main dashboard with subscription list, monthly spend card, and empty state.
**Estimated effort**: 2-3 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| `DashboardViewModel` with StateFlow (subscriptions, totalMonthly, totalYearly) | FR-01, FR-02, FR-03 | `feature/dashboard/DashboardViewModel.kt` |
| `DashboardScreen` composable (list + summary cards) | FR-01, FR-02, FR-03 | `feature/dashboard/DashboardScreen.kt` |
| `SubscriptionCard` composable (name, amount, cycle, category icon, next date) | FR-01 | `ui/components/SubscriptionCard.kt` |
| `SpendSummaryCard` composable (monthly total, yearly projection) | FR-02, FR-03 | `ui/components/SpendSummaryCard.kt` |
| `EmptyStateView` composable | FR-05 | `ui/components/EmptyStateView.kt` |
| `CategoryChip` composable for filtering | FR-04 | `ui/components/CategoryChip.kt` |
| Swipe-to-delete with confirmation | FR-12 | Integrated in DashboardScreen |

**Exit criteria**: Dashboard shows subscriptions from Room, displays correct monthly total, empty state renders when no data.

---

### Phase 3: Add/Edit Subscription Screen
**Goal**: Complete subscription CRUD with validation.
**Estimated effort**: 1.5-2 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| `AddEditSubscriptionViewModel` (insert/update logic) | FR-10, FR-11, FR-15 | `feature/subscription/AddEditSubscriptionViewModel.kt` |
| `AddEditSubscriptionScreen` composable (form fields, validation) | FR-10, FR-11, FR-13, FR-14, FR-15, FR-16 | `feature/subscription/AddEditSubscriptionScreen.kt` |
| Date picker for next billing date | FR-10 | Integrated in form |
| Billing cycle selector (segmented button) | FR-13 | Integrated in form |
| Category selector (chip group) | FR-14 | Integrated in form |
| Input validation with inline errors | FR-15 | Integrated in form |

**Exit criteria**: User can add, edit subscriptions. Validation prevents bad data. Saved data appears on dashboard.

---

### Phase 4: Local Notifications
**Goal**: Schedule payment reminders 1 day before billing date.
**Estimated effort**: 1.5-2 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| `NotificationScheduler` (AlarmManager scheduling) | FR-20, FR-22 | `notification/NotificationScheduler.kt` |
| `PaymentReminderReceiver` (BroadcastReceiver) | FR-20, FR-23 | `notification/PaymentReminderReceiver.kt` |
| Create notification channel | FR-20 | In `SubSaverApp.kt` |
| Request POST_NOTIFICATIONS permission | FR-21 | In `MainActivity.kt` |
| Reschedule after billing date passes | FR-22 | In repository/scheduler logic |
| Boot receiver to restore alarms | FR-20 | Manifest + receiver |

**Exit criteria**: Adding a subscription schedules a notification. Notification fires at correct time. Tapping notification opens app.

---

### Phase 5: AdMob Integration
**Goal**: Add banner and interstitial ads with proper lifecycle management.
**Estimated effort**: 1-1.5 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| Add AdMob SDK dependency + manifest meta-data | FR-30, FR-31 | `build.gradle.kts`, `AndroidManifest.xml` |
| `AdManager` singleton (init, load interstitial, show interstitial) | FR-31, FR-32 | `ads/AdManager.kt` |
| `BannerAdView` composable wrapper | FR-30 | `ads/BannerAdView.kt` |
| Place banner on Dashboard bottom | FR-30 | `DashboardScreen.kt` |
| Show interstitial after subscription save (rate-limited) | FR-31 | `AddEditSubscriptionViewModel.kt` |
| Initialize MobileAds in Application class | FR-30 | `SubSaverApp.kt` |

**Exit criteria**: Banner ad visible on dashboard. Interstitial shows after save (test ads in debug). No UI jank.

---

### Phase 6: Firebase Analytics + Crashlytics
**Goal**: Wire up event tracking and crash reporting.
**Estimated effort**: 45 min - 1 hour

| Task | Requirement IDs | Deliverable |
|---|---|---|
| Add Firebase BOM + Analytics + Crashlytics dependencies | NFR-07, NFR-02 | `build.gradle.kts` |
| Add `google-services.json` | NFR-07 | `app/` |
| Log custom events (sub_added, sub_deleted, sub_edited, app_open) | NFR-07 | Throughout ViewModels |
| Verify Crashlytics receives test crash | NFR-02 | Manual test |

**Exit criteria**: Events appear in Firebase console. Crashlytics dashboard shows app. No crashes on happy path.

---

### Phase 7: Settings Screen + Polish
**Goal**: Add settings, accessibility, ProGuard, and final UX polish.
**Estimated effort**: 1-1.5 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| `SettingsScreen` (version, rate app, notification toggle) | FR-40, FR-41, FR-42 | `feature/settings/SettingsScreen.kt` |
| Content descriptions on all interactive elements | NFR-08 | Throughout composables |
| Enable R8/ProGuard for release build | NFR-06 | `build.gradle.kts` |
| Dark mode verification | NFR-09 | Manual test |
| Edge-to-edge final check | NFR-05 | `MainActivity.kt` |
| Cold start performance check | NFR-01 | Firebase trace |

**Exit criteria**: Settings functional. Release build minified. Dark mode works. App feels polished.

---

### Phase 8: ASO Optimization + Play Store Readiness
**Goal**: Prepare all store listing assets and publish.
**Estimated effort**: 2-3 hours

| Task | Requirement IDs | Deliverable |
|---|---|---|
| Generate app icon (adaptive + round) | ASO-07 | `res/mipmap-*` |
| Write store title, short description, full description | ASO-01, ASO-02, ASO-03 | Store listing copy |
| Generate feature graphic (1024x500) | ASO-04 | PNG asset |
| Capture screenshots (phone, min 4) | ASO-05 | PNG assets |
| Create privacy policy page | ASO-06 | GitHub Pages / hosted URL |
| Generate signed release APK/AAB | NFR-06 | `app-release.aab` |
| Complete content rating + target audience | ASO-08, ASO-09 | Play Console |
| Submit to Google Play | — | Live listing |

**Exit criteria**: App published on Google Play. Store listing complete with all assets. Privacy policy live.

---

## Phase Summary

| Phase | Name | Hours | Requirements Covered |
|---|---|---|---|
| 1 | Foundation + Supabase Baseline | 2-3h | FR-01,10,11,12,13,14,50,51,53 / NFR-01,04,05,09,10 |
| 2 | Dashboard | 2-3h | FR-01,02,03,04,05,12 |
| 3 | Add/Edit | 1.5-2h | FR-10,11,13,14,15,16 |
| 4 | Notifications | 1.5-2h | FR-20,21,22,23 |
| 5 | AdMob | 1-1.5h | FR-30,31,32 |
| 6 | Firebase | 0.75-1h | NFR-02,07 |
| 7 | Settings + Polish | 1-1.5h | FR-40,41,42,52,54 / NFR-01,05,06,08,09 |
| 8 | ASO + Publish | 2-3h | ASO-01 through ASO-09 |
| **Total** | | **~13-17h** | **All requirements** |
