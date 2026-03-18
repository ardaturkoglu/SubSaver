# SubSaver - REQUIREMENTS.md

## Functional Requirements

### Dashboard
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-01 | Display list of all active subscriptions | P0 | User sees all subscriptions sorted by next billing date |
| FR-02 | Show total monthly spend summary card | P0 | Sum of all subscriptions normalized to monthly cost is displayed |
| FR-03 | Show total yearly spend summary | P1 | Yearly projection visible on dashboard |
| FR-04 | Filter/group subscriptions by category | P1 | User can tap category chips to filter the list |
| FR-05 | Empty state when no subscriptions exist | P0 | Friendly illustration + CTA to add first subscription |
| FR-06 | Pull-to-refresh subscription list | P2 | Swipe down refreshes data from Room |
| FR-07 | Animated dashboard transitions and list interactions | P1 | Summary card/chips/list items animate in smoothly; delete uses animated removal |
| FR-08 | Animated spend value updates | P2 | Monthly/yearly totals animate value changes when data updates |

### Subscription Management
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-10 | Add a new subscription | P0 | User fills name, amount, cycle, category, next billing date and saves |
| FR-11 | Edit an existing subscription | P0 | User taps a subscription, modifies fields, and saves |
| FR-12 | Delete a subscription | P0 | Swipe-to-delete or long-press menu with confirmation dialog |
| FR-13 | Select billing cycle (Weekly/Monthly/Yearly) | P0 | Dropdown or segmented control for cycle selection |
| FR-14 | Select category from predefined list | P1 | Chips or dropdown: Streaming, Music, Cloud, Gaming, Fitness, News, Utilities, Other |
| FR-15 | Input validation on amount and name | P0 | Empty name or zero/negative amount shows inline error |
| FR-16 | Currency display (USD default) | P1 | Amounts formatted as $X.XX |
| FR-17 | Animated add/edit form feedback | P1 | Field error/help states, save loading state, and content entrance transitions are animated |

### Notifications
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-20 | Schedule local notification 1 day before billing date | P0 | Notification fires ~24h before next billing date |
| FR-21 | Request POST_NOTIFICATIONS permission (Android 13+) | P0 | Runtime permission dialog shown; graceful degradation if denied |
| FR-22 | Recurring notification rescheduling after payment date passes | P1 | After billing date, auto-advance to next cycle date and reschedule |
| FR-23 | Notification tap opens app to dashboard | P0 | PendingIntent launches MainActivity |

### Ads & Monetization
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-30 | Banner ad on Dashboard screen (bottom) | P0 | AdMob banner loads and displays; graceful fallback on no-fill |
| FR-31 | Interstitial ad after saving a subscription | P1 | Interstitial shown after add/edit save; max 1 per 3 minutes |
| FR-32 | Ad loading does not block UI | P0 | Ads load async; UI remains responsive |

### Settings
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-40 | App version display | P2 | Shows versionName from BuildConfig |
| FR-41 | Rate app link | P2 | Opens Play Store listing |
| FR-42 | Notification toggle (enable/disable all) | P1 | Master toggle cancels or re-schedules all alarms |

### Home Screen Widget (Jetpack Glance)
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-60 | Add a compact home screen widget showing monthly spend | P1 | Widget shows current monthly total and subscription count |
| FR-61 | Support widget refresh on subscription changes | P1 | Add/edit/delete updates widget state without manual refresh |
| FR-62 | Widget tap deep-links into dashboard | P1 | Tapping widget opens app dashboard screen |

### Supabase (Backend Ops + Sync Readiness)
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-50 | Supabase project is linked in project docs/config | P0 | Project ID, URL, region, and usage rules are documented and verified via MCP |
| FR-51 | Create `subscriptions` table schema in Supabase | P1 | Table exists with core fields matching Room entity and timestamp columns |
| FR-52 | Keep app local-first while Supabase is introduced | P0 | Core CRUD and dashboard work with no network connectivity |
| FR-53 | Add sync metadata fields for future cloud sync | P1 | Local model + remote schema include `updated_at` and `sync_state`-ready support |
| FR-54 | Secrets are never committed to git | P0 | Supabase keys are stored via local config, and git history stays clean |

## Non-Functional Requirements

| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| NFR-01 | App cold start < 2 seconds | P0 | Measured via Firebase trace |
| NFR-02 | Crash-free rate > 99% | P0 | Firebase Crashlytics dashboard |
| NFR-03 | Works offline (100% local) | P0 | No network dependency for core features |
| NFR-04 | Material 3 dynamic color support | P1 | Uses MaterialTheme with dynamic color on Android 12+ |
| NFR-05 | Edge-to-edge display | P0 | Status bar and nav bar transparency |
| NFR-06 | ProGuard/R8 enabled for release | P0 | Minified release APK |
| NFR-07 | Firebase Analytics event logging | P1 | Log: app_open, sub_added, sub_deleted, sub_edited |
| NFR-08 | Accessibility (content descriptions) | P1 | All interactive elements have contentDescription |
| NFR-09 | Dark mode support | P1 | Follows system theme via Material 3 |
| NFR-10 | Supabase schema changes use tracked migrations | P1 | All DDL for Supabase is applied through migration files/commands, not ad hoc |
| NFR-11 | Motion performance remains smooth | P1 | UI animations target smooth rendering on mid-range devices (no visible jank) |

## Play Store / ASO Requirements

| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| ASO-01 | App title optimized for search | P0 | "SubSaver - Subscription Tracker & Bill Manager" |
| ASO-02 | Short description (80 chars) | P0 | Keyword-rich, benefit-focused |
| ASO-03 | Full description (4000 chars) | P0 | Features, benefits, keywords structured |
| ASO-04 | Feature graphic (1024x500) | P0 | AI-generated, brand-consistent |
| ASO-05 | Screenshots (min 4, phone + tablet) | P0 | Real app screenshots with device frames |
| ASO-06 | Privacy policy URL | P0 | Hosted page covering data practices |
| ASO-07 | App icon (adaptive, round) | P0 | Distinct, recognizable at small sizes |
| ASO-08 | Content rating questionnaire completed | P0 | ESRB / PEGI rated |
| ASO-09 | Target audience declaration (not children) | P0 | Ads compliance for non-child audience |
