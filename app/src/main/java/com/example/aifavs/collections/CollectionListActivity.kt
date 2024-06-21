package com.example.aifavs.collections

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.Collection
import com.example.aifavs.MainActivity
import com.example.aifavs.R
import com.example.aifavs.WebViewActivity
import com.example.aifavs.base.BaseMediaControlActivity
import com.example.aifavs.databinding.ActivityCollectionListBinding
import com.example.aifavs.dp2px
import com.example.aifavs.playback.PlayerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CollectionListActivity : BaseMediaControlActivity<ActivityCollectionListBinding>() {
    private lateinit var viewModel: CollectionListViewModel
    private lateinit var mAdapter: ContentListAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var categoryId: String? = null
    private var tagId: String? = null

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_CATEGORY_ID = "key_category_id"
        const val KEY_TAG_ID = "key_tag_id"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setupMiniPlayer(binding.miniPlayer.root)
        viewModel = ViewModelProvider(this)[CollectionListViewModel::class.java]
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        tagId = intent.getStringExtra(KEY_TAG_ID)
        viewModel.getContentList(categoryId, tagId)
        viewModel.collectionList.observe(this) { list ->
            mAdapter.submitList(list)
            mAdapter.notifyDataSetChanged()
        }
        viewModel.refreshingList.observe(this) {
            mSwipeRefreshLayout.isRefreshing = it
        }
        viewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    private fun initView() {
        with (binding) {
            mAdapter = ContentListAdapter()
            mAdapter.setOnItemClickListener {adapter, _, position ->
                val item = adapter.getItem(position)
                item?.url?.let {
                    WebViewActivity.openUrl(adapter.context, it)
                }
            }
            mAdapter.setOnItemLongClickListener { adapter, _, position ->
                val item = adapter.getItem(position)
                item?.let {
                    val bottomSheet = MultiOptionModalBottomSheet(createBottomSheetOptions(it))
                    bottomSheet.show(supportFragmentManager)
                }
                item != null
            }
            rvContentList.layoutManager = LinearLayoutManager(this@CollectionListActivity)
            rvContentList.addItemDecoration(BottomMarginDecoration(16f))
            rvContentList.adapter = mAdapter

            mSwipeRefreshLayout = swipeRefresh
            mSwipeRefreshLayout.setOnRefreshListener {
                viewModel.getContentList(categoryId, tagId)
            }
        }

        val title = intent.getStringExtra(KEY_TITLE)
        binding.topAppBar.title = title
    }

    private fun createBottomSheetOptions(collection: Collection): List<Option> {
        val createPodcastOption = Option(
            name = "Generate Podcast",
            icon = R.drawable.ic_podcast_color_on_surface
        ) {
            viewModel.createPodcast(collection.id) {
                Snackbar.make(binding.root, "Generating Podcast...", Snackbar.LENGTH_LONG)
                    .setAction("View") {
                        val bundle = Bundle()
                        bundle.putInt(MainActivity.KEY_DEST, R.id.tab_podcast)
                        MainActivity.navigate(this@CollectionListActivity, bundle)
                        finish()
                    }
                    .show()
            }
        }
        val playPodcastOption = Option(
            name = "Play Podcast",
            icon = R.drawable.ic_play_circle
        ) {
            val bundle = Bundle()
            collection.apply {
                bundle.putString("audioUrl", podcast?.audioUrl())
                bundle.putString("title", title)
            }
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        val showSummaryOption = Option(
            name = "Show AI Summary",
            icon = R.drawable.ic_ai_star_color_on_surface
        ) {
            showAISummaryDialog(collection.summary!!)
        }
        val options = mutableListOf<Option>()
        collection.apply {
            if (summary != null) {
                options.add(showSummaryOption)
            }
            if (podcast != null) {
                options.add(playPodcastOption)
            } else {
                options.add(createPodcastOption)
            }
        }
        return options
    }

    private fun showAISummaryDialog(content: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("AI Summary")
            .setMessage(content)
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
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
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_content_list_item, parent)
    }
}