package com.example.aifavs.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class ModalBottomSheet(@LayoutRes open val contentLayout: Int) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(contentLayout, container, false)

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}