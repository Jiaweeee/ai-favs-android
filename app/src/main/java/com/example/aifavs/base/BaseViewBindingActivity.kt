package com.example.aifavs.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseViewBindingActivity<B: ViewBinding> : BaseActivity() {
    private var _binding: B? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = createViewBinding()
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewBinding(): B {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>
        val method = type.getMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as B
    }
}