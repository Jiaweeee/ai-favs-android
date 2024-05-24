package com.example.aifavs.collections

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.R

class CollectionsFragment : Fragment() {
    private lateinit var viewModel: CollectionsViewModel
    private lateinit var mAdapter: FoldersAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    companion object {
        fun newInstance() = CollectionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel = ViewModelProvider(this)[CollectionsViewModel::class.java]
        viewModel.fetchData()
        viewModel.folders.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            mSwipeRefreshLayout.isRefreshing = isLoading
        }

    }

    private fun initViews(root: View) {
        val rvFolders = root.findViewById<RecyclerView>(R.id.rv_folders)
        rvFolders.layoutManager = LinearLayoutManager(context)
        mAdapter = FoldersAdapter()
        rvFolders.adapter = mAdapter

        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh)
        mSwipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }
}

class FoldersAdapter : BaseQuickAdapter<Folder, QuickViewHolder>() {
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