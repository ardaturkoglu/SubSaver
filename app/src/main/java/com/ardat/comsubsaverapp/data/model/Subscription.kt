package com.ardat.comsubsaverapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val billingCycle: BillingCycle,
    val category: Category,
    val nextBillingDate: LocalDate,
    val createdAt: LocalDate = LocalDate.now()
) {
    /** Normalize any cycle to a monthly cost. */
    val monthlyAmount: Double
        get() = amount * billingCycle.monthlyMultiplier

    /** Project to a yearly cost. */
    val yearlyAmount: Double
        get() = monthlyAmount * 12.0
}
