# Phase 1 Discussion: Foundation + UI Design System

## Phase Scope Recap
Phase 1 establishes the **data layer** (Room entities, DAO, Repository), **Material 3 theme**, **navigation shell**, **Supabase project baseline**, and the **Application class**. But before we write code, we need to lock down the visual design so every subsequent phase builds on a consistent foundation.

---

## UI Architecture Decisions

### Screen Map (MVP)
```
[Dashboard] --> [Add Subscription] (FAB)
[Dashboard] --> [Edit Subscription] (tap card)
[Dashboard] --> [Settings] (top-bar icon)
```

### Navigation Pattern
- **Single NavHost** in `MainActivity`
- **Bottom bar**: None (only 2 primary screens; settings via top-bar gear icon)
- **Transitions**: Slide-in from right for Add/Edit, fade for Settings

### State Management Pattern
```
Room DB (SQLite)
    |
    v (Flow<List<Subscription>>)
SubscriptionRepository
    |
    v (.stateIn(viewModelScope))
DashboardViewModel.uiState: StateFlow<DashboardUiState>
    |
    v (collectAsStateWithLifecycle)
DashboardScreen @Composable
```

### DashboardUiState
```kotlin
data class DashboardUiState(
    val subscriptions: List<Subscription> = emptyList(),
    val totalMonthlySpend: Double = 0.0,
    val totalYearlySpend: Double = 0.0,
    val selectedCategory: Category? = null, // null = all
    val isLoading: Boolean = true
)
```

### AddEditUiState
```kotlin
data class AddEditUiState(
    val name: String = "",
    val amount: String = "",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val category: Category = Category.OTHER,
    val nextBillingDate: LocalDate = LocalDate.now(),
    val nameError: String? = null,
    val amountError: String? = null,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false
)
```

---

## Color System (Material 3)

### Brand Identity
- **Primary**: Deep Indigo (#4F46E5) — trust, finance, reliability
- **Secondary**: Emerald Green (#10B981) — savings, positive money
- **Tertiary**: Amber (#F59E0B) — alerts, upcoming payments
- **Error**: Red (#EF4444) — overdue, validation errors
- **Background**: Off-white (#FAFAFA) light / Near-black (#121212) dark
- **Surface**: White (#FFFFFF) / Dark gray (#1E1E1E)

### Dynamic Color
- Use `dynamicDarkColorScheme()` / `dynamicLightColorScheme()` on Android 12+
- Fallback to brand colors above on older devices (but minSdk=31, so dynamic color always available)

---

## Typography
- **Display**: Dashboard total spend number
- **Headline**: Screen titles
- **Title**: Card headers, subscription names
- **Body**: Descriptions, amounts
- **Label**: Chips, buttons, secondary text

Using Material 3 default type scale with `fontFamily = FontFamily.Default` (system font for speed).

---

## Screen-by-Screen Layout Specification

### 1. Dashboard Screen

```
+------------------------------------------+
|  [=] SubSaver              [gear icon]   |  <- TopAppBar
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  |  Monthly Spend        Yearly Est.  |  |
|  |  $142.97              $1,715.64    |  |  <- SpendSummaryCard
|  +------------------------------------+  |
|                                          |
|  [All] [Streaming] [Music] [Cloud] ...   |  <- Category filter chips (horizontal scroll)
|                                          |
|  +------------------------------------+  |
|  | Netflix          $15.99/mo         |  |
|  | Next: Mar 22     [Streaming icon]  |  |  <- SubscriptionCard
|  +------------------------------------+  |
|  +------------------------------------+  |
|  | Spotify          $10.99/mo         |  |
|  | Next: Mar 25     [Music icon]      |  |
|  +------------------------------------+  |
|  +------------------------------------+  |
|  | iCloud           $2.99/mo          |  |
|  | Next: Apr 01     [Cloud icon]      |  |
|  +------------------------------------+  |
|                                          |
|  ... (LazyColumn)                        |
|                                          |
|                           [+ FAB]        |  <- Floating Action Button
+------------------------------------------+
|  [===== AdMob Banner Ad =====]           |  <- Phase 5
+------------------------------------------+
```

**Empty State** (when no subscriptions):
```
+------------------------------------------+
|  [=] SubSaver              [gear icon]   |
+------------------------------------------+
|                                          |
|           [illustration]                 |
|                                          |
|      No subscriptions yet!               |
|   Tap + to add your first bill           |
|                                          |
|                           [+ FAB]        |
+------------------------------------------+
```

### 2. Add/Edit Subscription Screen

```
+------------------------------------------+
|  [<-] Add Subscription                   |  <- TopAppBar with back arrow
+------------------------------------------+
|                                          |
|  Subscription Name                       |
|  +------------------------------------+  |
|  | Netflix                             |  |  <- OutlinedTextField
|  +------------------------------------+  |
|  * Name is required                      |  <- Inline error (red)
|                                          |
|  Amount                                  |
|  +------------------------------------+  |
|  | $ | 15.99                           |  |  <- OutlinedTextField, prefix $
|  +------------------------------------+  |
|                                          |
|  Billing Cycle                           |
|  [Weekly] [Monthly*] [Yearly]            |  <- SegmentedButton (Monthly default)
|                                          |
|  Category                                |
|  [Streaming*] [Music] [Cloud]            |
|  [Gaming] [Fitness] [News]               |  <- FlowRow of FilterChips
|  [Utilities] [Other]                     |
|                                          |
|  Next Billing Date                       |
|  +------------------------------------+  |
|  | Mar 22, 2026          [calendar]   |  |  <- DatePicker trigger
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |          Save Subscription          |  |  <- FilledButton, full width
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
```

### 3. Settings Screen

```
+------------------------------------------+
|  [<-] Settings                           |
+------------------------------------------+
|                                          |
|  Notifications                           |
|  +------------------------------------+  |
|  | Payment Reminders          [toggle]|  |
|  +------------------------------------+  |
|                                          |
|  About                                   |
|  +------------------------------------+  |
|  | Rate SubSaver                   >  |  |
|  +------------------------------------+  |
|  +------------------------------------+  |
|  | Version                      1.0   |  |
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
```

---

## AI Design Prompt Library

Use these prompts with **Midjourney**, **DALL-E 3**, **Ideogram**, or **Figma AI** to generate the visual assets.

### Prompt 1: App Icon
```
Design a modern minimalist app icon for "SubSaver", a subscription tracking app.
The icon should feature a stylized piggy bank or wallet combined with a recurring
payment symbol (circular arrows). Use a deep indigo (#4F46E5) to emerald green
(#10B981) gradient background. The icon symbol should be white or light colored.
Clean, flat design with subtle depth/shadow. No text on the icon. Square format
with rounded corners suitable for Android adaptive icon. Professional fintech
aesthetic.
```

### Prompt 2: Empty State Illustration
```
Create a friendly, minimal vector-style illustration for an empty state in a
subscription tracking app. Show a relaxed character sitting next to an empty
clipboard or open wallet with no bills. Soft pastel colors with indigo (#4F46E5)
and green (#10B981) accents. Flat illustration style, no heavy gradients. The mood
should be inviting and encouraging - "no subscriptions yet, let's add one!"
Transparent or white background. Mobile-friendly aspect ratio (roughly 1:1 or 3:4).
```

### Prompt 3: Feature Graphic (Google Play Store - 1024x500)
```
Design a Google Play Store feature graphic (1024x500 pixels) for "SubSaver -
Subscription Tracker". Show a clean phone mockup displaying the app dashboard with
subscription cards (Netflix, Spotify style). Background: deep indigo (#4F46E5) to
dark blue gradient. Text overlay: "Track Every Subscription. Save Every Dollar."
in clean white sans-serif font. Include subtle floating dollar signs or recurring
payment icons as decorative elements. Professional, trustworthy, fintech aesthetic.
Modern and minimal. No busy patterns.
```

### Prompt 4: Store Screenshots Mockup (Phone Frame)
```
Create a set of 4 Android phone screenshot mockups for a Google Play Store listing.
App name: "SubSaver". Each screen in a modern phone frame (Pixel-style) on a clean
gradient background (indigo to blue). 

Screen 1: Dashboard showing 5 subscription cards (Netflix, Spotify, iCloud, etc.)
with a summary card at top showing "$142.97/month". Material 3 design, cards with
rounded corners.

Screen 2: Add Subscription form with fields filled in (name: "Disney+", amount:
"$13.99", Monthly selected, Streaming category highlighted). Clean Material 3 form.

Screen 3: Dashboard with category filter chips active, filtering to show only
"Streaming" subscriptions. 

Screen 4: Notification preview showing "Netflix payment tomorrow - $15.99" in
Android notification style.

Each mockup should have a short tagline below the phone: "Track subscriptions",
"Add in seconds", "Filter by category", "Never miss a payment".
```

### Prompt 5: Dashboard UI Concept (High-Fidelity)
```
Design a high-fidelity mobile UI mockup for a subscription tracker dashboard screen.
Android app using Material 3 / Material You design language. 

Top: App bar with "SubSaver" title and gear icon.
Below app bar: Summary card with "Monthly: $142.97" and "Yearly: $1,715.64" in
large bold typography. Card has subtle indigo (#4F46E5) accent.

Below summary: Horizontal scrollable category filter chips (All, Streaming, Music,
Cloud, Gaming).

Main content: Vertical list of subscription cards. Each card shows:
- Service name (bold, left-aligned)
- Monthly cost (right-aligned, green for low, amber for medium, red for high)
- Next billing date (subtitle, gray text)
- Category icon (small, left of name)

Cards are white with rounded corners and subtle elevation.
FAB (Floating Action Button) bottom-right, indigo colored, with + icon.
Bottom: Banner ad placeholder strip.

Light mode. Clean, modern, minimal whitespace. 1080x2400 resolution.
```

### Prompt 6: Add Subscription Form UI Concept
```
Design a high-fidelity mobile UI mockup for an "Add Subscription" form screen.
Android Material 3 / Material You design.

Top: App bar with back arrow and "Add Subscription" title.

Form fields (vertical scroll):
1. "Subscription Name" - Material 3 OutlinedTextField with label
2. "Amount" - OutlinedTextField with "$" prefix
3. "Billing Cycle" - Material 3 SegmentedButton row: [Weekly] [Monthly*] [Yearly]
4. "Category" - FlowRow of Material 3 FilterChips in 2 rows:
   Row 1: Streaming, Music, Cloud, Gaming
   Row 2: Fitness, News, Utilities, Other
5. "Next Billing Date" - Date field with calendar icon
6. "Save Subscription" - Full-width FilledButton, indigo (#4F46E5)

Light mode. Generous spacing between fields. Clean and professional.
1080x2400 resolution.
```

### Prompt 7: Dark Mode Dashboard Variant
```
Same as Prompt 5 but in DARK MODE:
Design a high-fidelity mobile UI mockup for a subscription tracker dashboard.
Android Material 3 dark theme. Background: near-black (#121212).
Cards: dark surface (#1E1E1E) with subtle border.
Text: white and light gray.
Summary card accent: muted indigo. FAB: lighter indigo.
Category chips: outlined style with white text.
Subscription amounts: green (#10B981) for costs.
Same layout as light mode but fully dark-themed.
1080x2400 resolution.
```

---

## Phase 1 Execution Plan (Atomic Waves)

### Wave 1: Data Models (no dependencies)
- [ ] `BillingCycle.kt` enum
- [ ] `Category.kt` enum  
- [ ] `Subscription.kt` Room entity

### Wave 2: Database Layer (depends on Wave 1)
- [ ] `SubscriptionDao.kt`
- [ ] `SubSaverDatabase.kt`

### Wave 3: Repository (depends on Wave 2)
- [ ] `SubscriptionRepository.kt`

### Wave 4: Theme + Navigation (no data dependency)
- [ ] `Color.kt` (brand colors)
- [ ] `Type.kt` (typography scale)
- [ ] `Theme.kt` (light/dark/dynamic)
- [ ] `Screen.kt` (sealed class for routes)
- [ ] `NavGraph.kt` (navigation skeleton)

### Wave 5: App Shell (depends on Wave 2, 4)
- [ ] `SubSaverApp.kt` (Application class)
- [ ] Update `MainActivity.kt` with NavHost
- [ ] Update `AndroidManifest.xml` with Application class

### Wave 6: Supabase Baseline (can run in parallel with Waves 4-5)
- [ ] Verify Supabase project health via MCP
- [ ] Create initial `subscriptions` table migration (sync-ready fields)
- [ ] Document Supabase environment usage in `.planning/PROJECT.md`

### Verification
- App compiles and runs
- Navigate from Dashboard placeholder to Add placeholder and back
- Room DB is created (verify with App Inspection)
- Theme colors render correctly in light and dark

---

## Questions Resolved (Auto-answered for speed)

| Question | Decision | Rationale |
|---|---|---|
| Bottom nav or no? | **No bottom nav** | Only 2 primary screens; FAB + gear icon sufficient |
| Hilt/Koin for DI? | **No** | Manual singletons for MVP speed |
| Currency selector? | **USD only for MVP** | Simplicity; can add later |
| Onboarding flow? | **None** | Straight to dashboard; empty state is the onboarding |
| Date library? | **java.time (LocalDate)** | minSdk 31, no need for ThreeTenABP |
| Multiple currencies? | **Post-MVP** | Would require exchange rates, unnecessary complexity |
