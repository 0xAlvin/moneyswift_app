package services

import android.util.Log
import com.example.core.domain.model.NotificationData
import com.example.core.domain.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService(){

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var coroutineScope: CoroutineScope

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isEmpty()){
            Log.d("REMOTE_MESSAGE", message.data.toString())
        }

        val notificationData = NotificationData(
            title = message.notification?.title ?: "no title",
            body = message.notification?.body ?: "no description",
            type = message.data["type"] ?: "default",
            data = message.data
        )

        coroutineScope.launch {
            notificationRepository.handleNotification(notificationData)
        }
    }
    override fun onNewToken(token: String){
        notificationRepository.updateToken(token)
    }
}