package com.example.aifavs.collections

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.R

data class Option(
    val name: String,
    @DrawableRes val icon: Int? = null,
    val onClick: () -> Unit
)

class MultiOptionModalBottomSheet(private val options: List<Option>): ModalBottomSheet(
    contentLayout = R.layout.layout_multi_option_bottom_sheet
) {
    private lateinit var rvOptions: RecyclerView
    private lateinit var adapter: MultiOptionBottomSheetAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(root: View) {
        rvOptions = root.findViewById(R.id.rv_options)
        rvOptions.layoutManager = LinearLayoutManager(context)
        adapter = MultiOptionBottomSheetAdapter()
        rvOptions.adapter = adapter
        adapter.submitList(options)
        adapter.setOnItemClickListener { a, _, position ->
            val option = a.getItem(position)
            if (option?.onClick != null) {
                option.onClick()
            }
            dismiss()
        }
    }
}

class MultiOptionBottomSheetAdapter: BaseQuickAdapter<Option, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Option?) {
        if (item == null) {
            return
        }
        val (name, icon) = item
        val tvName = holder.getView<TextView>(R.id.tv_name)
        tvName.text = name
        icon?.let {
            val ivIcon = holder.getView<ImageView>(R.id.iv_icon)
            ivIcon.visibility = View.VISIBLE
            ivIcon.setImageResource(it)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_bottom_sheet_option, parent)
    }

}