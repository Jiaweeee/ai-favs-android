package com.example.aifavs.home

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.R
import com.example.aifavs.content.ContentListActivity

class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by lazy { HomeViewModel() }
    private lateinit var mAdapter: ContentFoldersAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        viewModel.fetchData()
        viewModel.folders.observe(this) {
            mAdapter.submitList(it)
        }
        viewModel.loading.observe(this) { isLoading ->
            mSwipeRefreshLayout.isRefreshing = isLoading
        }
    }

    private fun initViews() {
        val rvFolders = findViewById<RecyclerView>(R.id.rv_folders)
        rvFolders.layoutManager = LinearLayoutManager(this)
        mAdapter = ContentFoldersAdapter()
        rvFolders.adapter = mAdapter

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        mSwipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        findViewById<ImageView>(R.id.iv_add).setOnClickListener {
            Toast.makeText(this, "Not implemented yet :)", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.iv_btn_chat).setOnClickListener {
            Toast.makeText(this, "Not implemented yet :)", Toast.LENGTH_SHORT).show()
        }
    }
}

class ContentFoldersAdapter : BaseQuickAdapter<Folder, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Folder?) {
        if (item == null) {
            return
        }
        holder.getView<TextView>(R.id.tv_folder_name).text = item.name
        holder.getView<TextView>(R.id.tv_count).text = item.itemCount.toString()
        holder.itemView.setOnClickListener {
            ContentListActivity.navigate(context, item.name, item.id)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_folder_list_item, parent)
    }

}
