package com.example.aifavs.assistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aifavs.base.BaseViewBindingActivity
import com.example.aifavs.databinding.ActivityAssistantBinding

class AssistantActivity : BaseViewBindingActivity<ActivityAssistantBinding>() {
    private lateinit var viewModel: AssistantViewModel
    private lateinit var messageListAdapter: MessageListAdapter

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, AssistantActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AssistantViewModel::class.java]
        initViews()
        viewModel.streamingEvent.observe(this) { event ->
            messageListAdapter.onStreamingEvent(event)
        }
        viewModel.waiting.observe(this) { waiting ->
            with(binding) {
                if (waiting) {
                    editGchatMessage.isEnabled = false
                    buttonSend.visibility = View.GONE
                    buttonStop.visibility = View.VISIBLE
                } else {
                    editGchatMessage.isEnabled = true
                    buttonSend.visibility = View.VISIBLE
                    buttonStop.visibility = View.GONE
                }
            }
        }
    }

    private fun initViews() {
        messageListAdapter = MessageListAdapter()
        with (binding) {
            recyclerGchat.layoutManager = LinearLayoutManager(this@AssistantActivity)
            recyclerGchat.adapter = messageListAdapter
            buttonSend.setOnClickListener {
                val text = editGchatMessage.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    viewModel.startChat(text, messageListAdapter.items)
                    with(messageListAdapter) {
                        appendHumanMessage(text)
                        appendAIMessage()
                    }
                    with (editGchatMessage) {
                        setText("")
                        clearFocus()
                        hideKeyboard()
                    }
                }
            }
            buttonStop.setOnClickListener {
                viewModel.stopChat()
            }
        }
        messageListAdapter.appendAIMessage("Welcome, I'm your assistant. How can I help you?")
    }
}