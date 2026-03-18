package com.ardat.comsubsaverapp.data.db

import androidx.room.TypeConverter
import com.ardat.comsubsaverapp.data.model.BillingCycle
import com.ardat.comsubsaverapp.data.model.Category
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromBillingCycle(cycle: BillingCycle): String = cycle.name

    @TypeConverter
    fun toBillingCycle(value: String): BillingCycle = BillingCycle.valueOf(value)

    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)
}
