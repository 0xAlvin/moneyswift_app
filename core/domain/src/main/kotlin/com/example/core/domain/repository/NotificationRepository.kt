package com.example.core.domain.repository

import com.example.core.domain.model.NotificationData

interface NotificationRepository {
    suspend fun handleNotification(notification: NotificationData)
    fun updateToken(token: String)
    suspend fun subscribe(topic : String)
    suspend fun unsubscribe(topic: String)
}
