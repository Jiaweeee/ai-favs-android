package com.example.aifavs

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val viewModel: ContentViewModel by lazy { ContentViewModel() }
    private lateinit var mAdapter: ContentListAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewModel.getContentList()
        viewModel.contentList.observe(this) { list ->
            mAdapter.submitList(list)
            mAdapter.notifyDataSetChanged()
        }
        viewModel.loading.observe(this) { isLoading ->
            mSwipeRefreshLayout.isRefreshing = isLoading
        }
    }

    private fun initView() {
        val rvContentList = findViewById<RecyclerView>(R.id.rv_content_list)
        mAdapter = ContentListAdapter()
        rvContentList.layoutManager = LinearLayoutManager(this)
        rvContentList.addItemDecoration(BottomMarginDecoration(16f))
        rvContentList.adapter = mAdapter

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        mSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getContentList()
        }
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

        val labelContainer = holder.getView<HorizontalScrollView>(R.id.labels_container)
        if (item.labels != null) {
            labelContainer.visibility = View.VISIBLE
            val labelsView = holder.getView<ChipGroup>(R.id.labels)
            labelsView.removeAllViews()
            for (label in item.labels) {
                labelsView.addView(createChip(label, context))
            }
        } else {
            labelContainer.visibility = View.GONE
        }

        val ivMore = holder.getView<ImageView>(R.id.iv_more)
        if (item.summary == null && item.highlights == null) {
            ivMore.visibility = View.GONE
        } else {
            ivMore.visibility = View.VISIBLE
            bindSummary(item.summary, holder)
            bindHighlights(item.highlights, holder)
            val aiContentBlocks = holder.getView<LinearLayout>(R.id.ai_content_blocks)
            ivMore.setOnClickListener {
                if (aiContentBlocks.visibility == View.GONE) {
                    aiContentBlocks.visibility = View.VISIBLE
                    ivMore.setImageResource(R.drawable.ic_arrow_down)
                } else {
                    aiContentBlocks.visibility = View.GONE
                    ivMore.setImageResource(R.drawable.ic_arrow_right)
                }
            }
        }
    }

    private fun createChip(text: String, context: Context): Chip {
        val chip = Chip(context)
        chip.text = text
        return chip
    }

    private fun bindSummary(summary: String?, holder: QuickViewHolder) {
        val blockView = holder.getView<LinearLayout>(R.id.block_ai_summary)
        if (summary == null) {
            blockView.visibility = View.GONE
            return
        } else {
            blockView.visibility = View.VISIBLE
        }
        val tvTitle = blockView.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "AI Summary"
        val tvContent = blockView.findViewById<TextView>(R.id.tv_content)
        tvContent.text = summary
    }

    private fun bindHighlights(highlights: List<String>?, holder: QuickViewHolder) {
        val blockView = holder.getView<LinearLayout>(R.id.block_ai_highlights)
        if (highlights == null) {
            blockView.visibility = View.GONE
            return
        } else {
            blockView.visibility = View.VISIBLE
        }
        val tvTitle = blockView.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "AI Highlights"
        val tvContent = blockView.findViewById<TextView>(R.id.tv_content)
        tvContent.text = buildHighlightText(highlights)
    }

    private fun buildHighlightText(highlights: List<String>): String {
        val sb = StringBuilder()
        for (text in highlights) {
            sb.append(text).append("\n\n")
        }
        return sb.toString()
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_content_list_item, parent)
    }
}