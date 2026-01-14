package com.example.core.domain.model

data class NotificationData (
    val title: String,
    val body: String,
    val type: String,
    val data: Map<String, String>,
)
