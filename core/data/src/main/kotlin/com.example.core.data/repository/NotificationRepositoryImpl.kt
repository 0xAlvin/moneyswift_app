package com.example.core.data.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.core.domain.model.NotificationData
import com.example.core.domain.repository.NotificationRepository
import com.example.moneyswift.data.R
import com.google.firebase.messaging.FirebaseMessaging
import com.stripe.android.model.Token
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    private val firebaseMessaging: FirebaseMessaging
    ): NotificationRepository {

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel1 = NotificationChannel(
                    CHANNEL_ID,
                    "Default channel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "default notification channel"
                    enableLights(true)
                    enableVibration(true)
                }
                val channel2 = NotificationChannel(
                    CHANNEL_ID_ORDER,
                    "Default channel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "order notification channel"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannels(listOf(channel1, channel2))
            }
        }

    override suspend fun handleNotification(notification: NotificationData) {
        withContext(Dispatchers.Main){
            when (notification.type) {
                "order" -> showOrderNotification(notification)
                else -> showDefaultNotification(notification)
            }
        }
    }

    override fun updateToken(token: String) {
        Log.d("FCM_TOKEN", Token.toString())
    }

    override suspend fun subscribe(topic: String) {
        try {
            firebaseMessaging.subscribeToTopic(topic)
        }catch (e : Exception){
            e.let { Log.e("NOTIFICATION SUBSCRIPTION", it.toString()) }
        }
    }

    override suspend fun unsubscribe(topic: String) {
        try {
            firebaseMessaging.unsubscribeFromTopic(topic)
        }catch (e : Exception){
            e.let { Log.e("NOTIFICATION SUBSCRIPTION", it.toString()) }
        }
    }
    private fun showOrderNotification(notification: NotificationData){
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_ORDER)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.baseline_shopping_cart_checkout_24)

        notificationManager.notify(notification.hashCode(), notificationBuilder.build())
    }
    private fun showDefaultNotification(notification: NotificationData){
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notification.hashCode(), notificationBuilder.build())
    }
    companion object {
        private const val CHANNEL_ID = "default_channel"
        private const val CHANNEL_ID_ORDER = "order_channel"
    }
}