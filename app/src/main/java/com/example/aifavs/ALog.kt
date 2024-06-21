package com.example.aifavs

import android.util.Log

object ALog {
    private const val TAG_PREFIX = "AIFavs_"
    private var enable = true

    fun init(enable: Boolean) {
        this.enable = enable
    }

    fun v(tag: String = "", msg: String) {
        if (!enable) {
            return
        }
        Log.v(TAG_PREFIX + tag, msg)
    }

    fun d(tag: String = "", msg: String) {
        if (!enable) {
            return
        }
        Log.d(TAG_PREFIX + tag, msg)
    }

    fun i(tag: String = "", msg: String) {
        if (!enable) {
            return
        }
        Log.i(TAG_PREFIX + tag, msg)
    }

    fun w(tag: String = "", msg: String) {
        if (!enable) {
            return
        }
        Log.w(TAG_PREFIX + tag, msg)
    }
    
    fun e(tag: String = "", msg: String) {
        if (!enable) {
            return
        }
        Log.e(TAG_PREFIX + tag, msg)
    }
}