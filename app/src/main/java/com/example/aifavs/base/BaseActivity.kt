package com.example.aifavs.base

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.aifavs.R

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImmersiveSystemBar()
    }

    open fun setImmersiveSystemBar(@ColorRes colorRes: Int = R.color.color_surface) {
        window.statusBarColor = getColor(colorRes)
        window.navigationBarColor = getColor(colorRes)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = true
    }
}