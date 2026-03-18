package com.ardat.comsubsaverapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ardat.comsubsaverapp.data.model.Subscription

@Database(
    entities = [Subscription::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SubSaverDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: SubSaverDatabase? = null

        fun getInstance(context: Context): SubSaverDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubSaverDatabase::class.java,
                    "subsaver_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
