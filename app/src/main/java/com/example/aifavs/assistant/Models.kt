package com.example.aifavs.assistant

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
sealed class ChatMessage(val type: String) {
    data class Human(val content: String): ChatMessage("human")
    data class AI(var content: String): ChatMessage("ai")
}

@Serializable
data class ChatRequestBody(
    val input: String,
    @SerializedName("chat_history") val history: List<ChatMessage>? = null
)