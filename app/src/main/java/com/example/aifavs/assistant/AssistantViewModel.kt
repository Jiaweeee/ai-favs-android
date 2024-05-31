package com.example.aifavs.assistant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
sealed class StreamingEvent(val event: String) {
    data class Start(
        private val content: String?
    ): StreamingEvent("start")

    data class ToolUseStart(
        @SerializedName("tool_name") val toolName: String,
        @SerializedName("tool_description") val toolDesc: String? = null
    ): StreamingEvent("tool_start")

    data class ToolUseEnd(
        @SerializedName("tool_name") val toolName: String,
        @SerializedName("tool_description") val toolDesc: String? = null
    ): StreamingEvent("tool_end")

    data class LLMStreaming(
        val content: String
    ): StreamingEvent("llm_streaming")

    data class End(
        val output: String
    ) : StreamingEvent("end")

    data class Stop(val msg: String? = null): StreamingEvent("stop")

    data class Error(
        val msg: String? = null
    ): StreamingEvent("error")
}

class AssistantViewModel: ViewModel() {
    private val TAG = "AssistantViewModel"

    private val _streamingEvent: MutableLiveData<StreamingEvent> = MutableLiveData()
    val streamingEvent: LiveData<StreamingEvent> get() = _streamingEvent

    private val _waiting: MutableLiveData<Boolean> = MutableLiveData(false)
    val waiting: LiveData<Boolean> get() = _waiting

    private val chatStreamHandler = ChatStreamHandler()
    private val gson = Gson()

    fun startChat(input: String, messages: List<ChatMessage> = emptyList()) {
        chatStreamHandler.startChatStream(input, messages, object : ChatStreamHandler.ChatStreamCallback {
            override fun onNewMessage(message: String) {
                Log.w(TAG, message)
                val event: StreamingEvent? = parseEvent(message)
                event?.let {
                    postEvent(it)
                    val waiting = when (it) {
                        is StreamingEvent.Start -> true
                        is StreamingEvent.ToolUseStart -> true
                        is StreamingEvent.ToolUseEnd -> true
                        is StreamingEvent.LLMStreaming -> true
                        else -> false
                    }
                    _waiting.postValue(waiting)
                }
            }

            override fun onError(t: Throwable?) {
                t?.printStackTrace()
                postEvent(StreamingEvent.Error(msg = "Network Error"))
                _waiting.postValue(false)
            }
        })
    }

    fun stopChat() {
        chatStreamHandler.stopChatStream()
        _waiting.postValue(false)
        postEvent(StreamingEvent.Stop())
    }

    private fun parseEvent(json: String): StreamingEvent? {
        val obj = JSONObject(json)
        return when (obj.getString("event")) {
            "start" -> {
                gson.fromJson(json, StreamingEvent.Start::class.java)
            }
            "end" -> {
                gson.fromJson(json, StreamingEvent.End::class.java)
            }
            "tool_start" -> {
                gson.fromJson(json, StreamingEvent.ToolUseStart::class.java)
            }
            "tool_end" -> {
                gson.fromJson(json, StreamingEvent.ToolUseEnd::class.java)
            }
            "llm_streaming" -> {
                gson.fromJson(json, StreamingEvent.LLMStreaming::class.java)
            }
            else -> {
                null
            }
        }
    }

    private fun postEvent(event: StreamingEvent) {
        _streamingEvent.postValue(event)
    }
}