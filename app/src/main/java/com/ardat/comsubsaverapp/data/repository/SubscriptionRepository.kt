package com.ardat.comsubsaverapp.data.repository

import com.ardat.comsubsaverapp.data.db.SubscriptionDao
import com.ardat.comsubsaverapp.data.model.Subscription
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(private val dao: SubscriptionDao) {

    fun getAllSubscriptions(): Flow<List<Subscription>> = dao.getAllSubscriptions()

    suspend fun getById(id: Long): Subscription? = dao.getSubscriptionById(id)

    suspend fun insert(subscription: Subscription): Long = dao.insert(subscription)

    suspend fun update(subscription: Subscription) = dao.update(subscription)

    suspend fun delete(subscription: Subscription) = dao.delete(subscription)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
