package com.example.aifavs

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.aifavs.playback.player.MyMediaController

class App: Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App
        val context: Context get() = instance.applicationContext
    }
    override fun onCreate() {
        super.onCreate()
        initController()
    }


    private fun initController() {
        MyMediaController.getInstance(applicationContext).createControllerAsync()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            /**
             * Destroy the media controller when the app goes to background.
             * Need to recreate it when the app goes back to foreground.
             */
            override fun onStop(owner: LifecycleOwner) {
                MyMediaController.destroyInstance()
            }
        })
    }
}