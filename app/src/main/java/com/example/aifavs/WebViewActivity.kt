package com.example.aifavs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.aifavs.base.BaseViewBindingActivity
import com.example.aifavs.databinding.ActivityWebViewBinding

class WebViewActivity: BaseViewBindingActivity<ActivityWebViewBinding>() {
    private lateinit var webView: WebView

    companion object {
        private const val KEY_URL = "key_url"

        fun openUrl(context: Context, url: String) {
            val intent  = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = binding.webView
        val progressBar = binding.progressBar
        with(webView) {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        view?.loadUrl(it)
                    }
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.progress = newProgress
                }
            }
        }
        loadUrl()
    }

    private fun loadUrl() {
        val url = intent.getStringExtra(KEY_URL)
        if (url != null) {
            webView.loadUrl(url)
        }
    }

}