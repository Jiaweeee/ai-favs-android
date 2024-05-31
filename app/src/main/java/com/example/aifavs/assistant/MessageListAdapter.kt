package com.example.aifavs.assistant

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.R



class MessageListAdapter: BaseQuickAdapter<ChatMessage, MessageViewHolder>() {
    companion object {
        private const val VIEW_TYPE_HUMAN_MSG = 1
        private const val VIEW_TYPE_AI_MSG = 2
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, item: ChatMessage?) {
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return if (viewType == VIEW_TYPE_HUMAN_MSG) {
            HumanMessageViewHolder(parent)
        } else {
            AIMessageViewHolder(parent)
        }
    }

    override fun getItemViewType(position: Int, list: List<ChatMessage>): Int {
        val message = list[position]
        return if (message is ChatMessage.Human) {
            VIEW_TYPE_HUMAN_MSG
        } else {
            VIEW_TYPE_AI_MSG
        }
    }

    fun appendHumanMessage(text: String) {
        add(ChatMessage.Human(text))
        scrollToBottom()
    }

    fun appendAIMessage(text: String = "") {
        add(ChatMessage.AI(text))
        scrollToBottom()
    }

    fun onStreamingEvent(event: StreamingEvent) {
        val position = itemCount - 1
        val latestMessage = getItem(position)
        if (latestMessage is ChatMessage.AI) {
            when (event) {
                is StreamingEvent.LLMStreaming -> {
                    latestMessage.content += event.content
                }
                is StreamingEvent.End -> {
                    latestMessage.content = event.output
                }
                is StreamingEvent.Error -> {
                    latestMessage.content = event.msg ?: "error"
                }
                is StreamingEvent.Stop -> {
                    latestMessage.content = event.msg ?: latestMessage.content
                }
                else -> {
                    latestMessage.content = "..."
                }
            }
            notifyItemChanged(position)
            scrollToBottom()
        }
    }

    private fun scrollToBottom() {
        with(recyclerView) {
            post {
                scrollToPosition(itemCount - 1)
            }
        }
    }
}

abstract class MessageViewHolder(@LayoutRes resId: Int, parent: ViewGroup): QuickViewHolder(resId, parent) {
    abstract fun bind(message: ChatMessage)
}

class HumanMessageViewHolder(parent: ViewGroup): MessageViewHolder(
    resId = R.layout.layout_chat_msg_sent,
    parent = parent
) {
    override fun bind(message: ChatMessage) {
        if (message is ChatMessage.Human) {
            getView<TextView>(R.id.text_gchat_message_me).text = message.content
        }
    }
}

class AIMessageViewHolder(parent: ViewGroup): MessageViewHolder(
    resId = R.layout.layout_chat_msg_received,
    parent = parent
) {
    override fun bind(message: ChatMessage) {
        if (message is ChatMessage.AI) {
            getView<TextView>(R.id.text_gchat_user_other).text = "Assistant"
            getView<TextView>(R.id.text_gchat_message_other).text = message.content
        }
    }
}