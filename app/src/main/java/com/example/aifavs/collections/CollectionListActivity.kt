package com.example.aifavs.collections

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.Collection
import com.example.aifavs.R
import com.example.aifavs.base.BaseActivity
import com.example.aifavs.dp2px
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CollectionListActivity : BaseActivity() {
    private lateinit var viewModel: CollectionListViewModel
    private lateinit var mAdapter: ContentListAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var categoryId: String? = null
    private var tagId: String? = null

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_CATEGORY_ID = "key_category_id"
        const val KEY_TAG_ID = "key_tag_id"

        fun navigate(
            context: Context,
            bundle: Bundle = Bundle()
        ) {
            val intent = Intent(context, CollectionListActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)
        initView()
        viewModel = ViewModelProvider(this)[CollectionListViewModel::class.java]
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        tagId = intent.getStringExtra(KEY_TAG_ID)
        viewModel.getContentList(categoryId, tagId)
        viewModel.contentList.observe(this) { list ->
            mAdapter.submitList(list)
            mAdapter.notifyDataSetChanged()
        }
        viewModel.loading.observe(this) { isLoading ->
            mSwipeRefreshLayout.isRefreshing = isLoading
        }
    }

    override fun setPageTitle(title: String) {
        val toolBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolBar.title = title
    }

    private fun initView() {
        val rvContentList = findViewById<RecyclerView>(R.id.rv_content_list)
        mAdapter = ContentListAdapter()
        rvContentList.layoutManager = LinearLayoutManager(this)
        rvContentList.addItemDecoration(BottomMarginDecoration(16f))
        rvContentList.adapter = mAdapter

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        mSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getContentList(categoryId, tagId)
        }

        val title = intent.getStringExtra(KEY_TITLE)
        setPageTitle(title ?: "")
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

class ContentListAdapter: BaseQuickAdapter<Collection, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Collection?) {
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

//        val labelContainer = holder.getView<HorizontalScrollView>(R.id.labels_container)
//        if (item.tags != null) {
//            labelContainer.visibility = View.VISIBLE
//            val labelsView = holder.getView<ChipGroup>(R.id.labels)
//            labelsView.removeAllViews()
//            for (tag in item.tags) {
//                labelsView.addView(createChip(tag.name, context))
//            }
//        } else {
//            labelContainer.visibility = View.GONE
//        }
//
//        val ivMore = holder.getView<ImageView>(R.id.iv_more)
//        if (item.summary == null) {
//            ivMore.visibility = View.GONE
//        } else {
//            ivMore.visibility = View.VISIBLE
//            bindSummary(item.summary, holder)
//            val aiContentBlocks = holder.getView<LinearLayout>(R.id.ai_content_blocks)
//            ivMore.setOnClickListener {
//                if (aiContentBlocks.visibility == View.GONE) {
//                    aiContentBlocks.visibility = View.VISIBLE
//                    ivMore.setImageResource(R.drawable.ic_arrow_down)
//                } else {
//                    aiContentBlocks.visibility = View.GONE
//                    ivMore.setImageResource(R.drawable.ic_arrow_right)
//                }
//            }
//        }
    }

//    private fun createChip(text: String, context: Context): Chip {
//        val chip = Chip(context)
//        chip.text = text
//        return chip
//    }
//
//    private fun bindSummary(summary: String?, holder: QuickViewHolder) {
//        val blockView = holder.getView<LinearLayout>(R.id.block_ai_summary)
//        if (summary == null) {
//            blockView.visibility = View.GONE
//            return
//        } else {
//            blockView.visibility = View.VISIBLE
//        }
//        val tvTitle = blockView.findViewById<TextView>(R.id.tv_title)
//        tvTitle.text = "AI Summary"
//        val tvContent = blockView.findViewById<TextView>(R.id.tv_content)
//        tvContent.text = summary
//    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_content_list_item, parent)
    }
}