package com.example.core.domain.usecase

import com.example.core.domain.model.NotificationData
import com.example.core.domain.repository.NotificationRepository
import javax.inject.Inject

class HandleNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notification : NotificationData) {
        notificationRepository.handleNotification(notification)
    }
}