package com.example.aifavs.assistant

import android.util.Log
import com.example.aifavs.BuildConfig
import com.example.aifavs.ChatRequestBody
import com.example.aifavs.ServiceCreator
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

class ChatStreamHandler {
    private val TAG = "ChatStreamHandler"

    private val client = OkHttpClient()
    private var eventSource: EventSource? = null

    fun startChatStream(input: String, callback: ChatStreamCallback) {
        val url = "${BuildConfig.BASE_URL}/chat/stream"
        val jsonStr = Gson().toJson(ChatRequestBody(input))
        val mediaTypeStr = "application/json; charset=utf-8"
        val body = jsonStr.toRequestBody(mediaTypeStr.toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val listener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                Log.w(TAG, "onOpen")
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                callback.onNewMessage(data)
            }

            override fun onClosed(eventSource: EventSource) {
                Log.w(TAG, "onClosed")
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                callback.onError(t)
            }
        }

        eventSource = EventSources.createFactory(client).newEventSource(request, listener)
    }

    fun stopChatStream() {
        eventSource?.cancel()
    }

    interface ChatStreamCallback {
        fun onNewMessage(message: String)
        fun onError(t: Throwable?)
    }
}