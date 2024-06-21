package com.example.aifavs.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

inline fun <T : ViewBinding> View.viewBinding(crossinline bind: (View) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bind(this)
    }