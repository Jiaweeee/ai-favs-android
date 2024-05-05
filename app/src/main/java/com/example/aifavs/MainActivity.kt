package com.example.aifavs

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

class MainActivity : AppCompatActivity() {
    private val viewModel: ContentViewModel by lazy { ContentViewModel() }
    private lateinit var mAdapter: ContentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewModel.getContentList()
        viewModel.contentList.observe(this) { list ->
            mAdapter.submitList(list)
        }
    }

    private fun initView() {
        val rvContentList = findViewById<RecyclerView>(R.id.rv_content_list)
        mAdapter = ContentListAdapter()
        rvContentList.layoutManager = LinearLayoutManager(this)
        rvContentList.addItemDecoration(BottomMarginDecoration(16f))
        rvContentList.adapter = mAdapter
    }
}

class BottomMarginDecoration(
    private val space: Float // dp
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = parent.context.dp2px(space)
    }
}

class ContentListAdapter: BaseQuickAdapter<ContentItem, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ContentItem?) {
        if (item == null) {
            return
        }
        val tvTitle = holder.getView<TextView>(R.id.tv_title)
        val title = item.title ?: "Webpage: ${item.url}"
        tvTitle.text = title

        val tvDesc = holder.getView<TextView>(R.id.tv_desc)
        val desc = item.description ?: ""
        tvDesc.text = desc

        val ivThumbnail = holder.getView<ImageView>(R.id.iv_thumbnail)
        Glide.with(context)
            .load(item.thumbnail)
            .placeholder(R.drawable.pic_placeholder)
            .error(R.drawable.pic_placeholder)
            .into(ivThumbnail)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_content_list_item, parent)
    }
}