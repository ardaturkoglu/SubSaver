package com.ardat.comsubsaverapp

import android.app.Application
import com.ardat.comsubsaverapp.data.db.SubSaverDatabase
import com.ardat.comsubsaverapp.data.repository.SubscriptionRepository

class SubSaverApp : Application() {

    lateinit var database: SubSaverDatabase
        private set

    lateinit var subscriptionRepository: SubscriptionRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = SubSaverDatabase.getInstance(this)
        subscriptionRepository = SubscriptionRepository(database.subscriptionDao())
    }
}
