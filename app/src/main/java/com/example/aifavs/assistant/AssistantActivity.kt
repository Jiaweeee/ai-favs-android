package com.example.aifavs.assistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.aifavs.R
import com.example.aifavs.base.BaseActivity

class AssistantActivity : BaseActivity() {
    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, AssistantActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)
    }
}