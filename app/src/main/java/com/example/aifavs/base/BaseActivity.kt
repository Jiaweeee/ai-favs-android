package com.example.aifavs.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.aifavs.R

abstract class BaseActivity : AppCompatActivity() {
    private var loadingView: View? = null

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

    private fun addLoadingView(): View {
        val view = layoutInflater.inflate(R.layout.layout_loading_indicator, null)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.CENTER
        }
        view.layoutParams = layoutParams
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        val frameLayout = FrameLayout(this)
        frameLayout.addView(view)
        rootView.addView(frameLayout)
        return view
    }

    fun showLoading(show: Boolean) {
        if (loadingView == null) {
            loadingView = addLoadingView()
        }
        loadingView?.let {
            if (show) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
    }
}