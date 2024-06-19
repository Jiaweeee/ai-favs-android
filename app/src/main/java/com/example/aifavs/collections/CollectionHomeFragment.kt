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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.example.aifavs.R

class CollectionHomeFragment : Fragment() {
    private lateinit var viewModel: CollectionHomeViewModel
    private lateinit var foldersAdapter: Adapter
    private lateinit var tagsAdapter: Adapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var foldersContainer: View
    private lateinit var tagsContainer: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel = ViewModelProvider(this)[CollectionHomeViewModel::class.java]
        viewModel.fetchData()
        viewModel.folders.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                foldersContainer.visibility = View.VISIBLE
            } else {
                foldersContainer.visibility = View.GONE
            }
            foldersAdapter.submitList(it)
        }
        viewModel.tags.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                tagsContainer.visibility = View.VISIBLE
            } else {
                tagsContainer.visibility = View.GONE
            }
            tagsAdapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        }

    }

    private fun initViews(root: View) {
        val rvFolders = root.findViewById<RecyclerView>(R.id.rv_folders)
        rvFolders.layoutManager = LinearLayoutManager(context)
        foldersAdapter = Adapter()
        rvFolders.adapter = foldersAdapter
        foldersAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position)
            item?.let {
                val bundle = Bundle()
                bundle.putString(CollectionListActivity.KEY_TITLE, item.name)
                bundle.putString(CollectionListActivity.KEY_CATEGORY_ID, item.id)
                findNavController().navigate(R.id.collectionListActivity, bundle)
            }
        }

        val rvTags = root.findViewById<RecyclerView>(R.id.rv_tags)
        rvTags.layoutManager = LinearLayoutManager(context)
        tagsAdapter = Adapter()
        rvTags.adapter = tagsAdapter
        tagsAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position)
            item?.let {
                val bundle = Bundle()
                bundle.putString(CollectionListActivity.KEY_TITLE, item.name)
                bundle.putString(CollectionListActivity.KEY_TAG_ID, item.id)
                findNavController().navigate(R.id.collectionListActivity, bundle)
            }
        }

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
        foldersContainer = root.findViewById(R.id.folders_container)
        tagsContainer = root.findViewById(R.id.tags_container)
    }
}

class Adapter : BaseQuickAdapter<DisplayItem, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: DisplayItem?) {
        if (item == null) {
            return
        }
        holder.getView<TextView>(R.id.tv_folder_name).text = item.name
        holder.getView<TextView>(R.id.tv_count).text = item.itemCount.toString()
        val ivIcon = holder.getView<ImageView>(R.id.iv_icon)
        when (item.type) {
            ItemType.FOLDER -> ivIcon.setImageResource(R.drawable.ic_folder)
            ItemType.TAG -> ivIcon.setImageResource(R.drawable.ic_tag)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_collection_home_item, parent)
    }

}