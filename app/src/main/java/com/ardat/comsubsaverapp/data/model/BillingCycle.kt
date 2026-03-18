package com.ardat.comsubsaverapp.data.model

enum class BillingCycle(val label: String, val monthlyMultiplier: Double) {
    WEEKLY("Weekly", 4.33),
    MONTHLY("Monthly", 1.0),
    YEARLY("Yearly", 1.0 / 12.0);

    companion object {
        fun fromLabel(label: String): BillingCycle =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MONTHLY
    }
}
