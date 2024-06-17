package com.example.aifavs.podcast

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.PodcastInfo
import com.example.aifavs.R
import com.example.aifavs.collections.BottomMarginDecoration
import kotlin.Exception

enum class PodcastStatus(val value: Int) {
    GENERATING(1),
    READY(2),
    ERROR(-1);

    companion object {
        fun fromValue(value: Int): PodcastStatus? {
            var result: PodcastStatus? = null
            try {
                result = entries.first { it.value == value }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}

class PodcastFragment : Fragment() {
    private lateinit var viewModel: PodcastViewModel
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PodcastListAdapter

    companion object {
        fun newInstance() = PodcastFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_podcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel = ViewModelProvider(this)[PodcastViewModel::class.java]
        viewModel.fetchData()
        viewModel.loading.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = it
        }
        viewModel.podcasts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initViews(root: View) {
        swipeRefresh = root.findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            viewModel.fetchData()
        }
        recyclerView = root.findViewById(R.id.rv_podcast_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(BottomMarginDecoration(16f))
        adapter = PodcastListAdapter()
        recyclerView.adapter = adapter
    }
}

class PodcastListAdapter: BaseQuickAdapter<PodcastInfo, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: PodcastInfo?) {
        if (item == null) {
            return
        }
        val tvTitle = holder.getView<TextView>(R.id.tv_title)
        tvTitle.text = item.title

        val tvStatus = holder.getView<TextView>(R.id.tv_status)
        val status = PodcastStatus.fromValue(item.status)
        val statusText = when(status) {
            PodcastStatus.GENERATING -> context.getString(R.string.podcast_status_generating)
            PodcastStatus.READY -> context.getString(R.string.podcast_status_ready)
            PodcastStatus.ERROR -> context.getString(R.string.podcast_status_error)
            else -> ""
        }
        tvStatus.text = statusText

        val btnPlay = holder.getView<ImageView>(R.id.btn_play)
        btnPlay.isEnabled = status == PodcastStatus.READY
        btnPlay.setOnClickListener {
            when(status) {
                PodcastStatus.GENERATING -> {

                }
                PodcastStatus.ERROR -> {

                }
                PodcastStatus.READY -> {

                }
                else -> {}
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_podcast_item, parent)
    }

}