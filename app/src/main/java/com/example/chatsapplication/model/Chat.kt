package com.example.chatsapplication.model

data class Chat(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L  // Menyimpan waktu dalam bentuk epoch time
)


