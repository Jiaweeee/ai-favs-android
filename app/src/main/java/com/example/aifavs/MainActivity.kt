package com.example.aifavs

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aifavs.assistant.AssistantActivity
import com.example.aifavs.base.BaseActivity
import com.example.aifavs.collections.CollectionHomeFragment
import com.example.aifavs.insights.InsightsFragment
import com.example.aifavs.podcast.PodcastFragment
import com.example.aifavs.settings.SettingsFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews()
        viewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    override fun setPageTitle(title: String) {
        val toolBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolBar.title = title
        toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add -> {
                    showAddCollectionDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun initViews() {
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            val selectTab = when(item.itemId) {
                R.id.tab_insights -> {
                    setPageTitle(getString(R.string.insights))
                    fragment = InsightsFragment.newInstance()
                    true
                }
                R.id.tab_collections -> {
                    setPageTitle(getString(R.string.my_collections))
                    fragment = CollectionHomeFragment.newInstance()
                    true
                }
                R.id.tab_ai_assistant -> {
                    false
                }
                R.id.tab_podcast -> {
                    setPageTitle(getString(R.string.podcast))
                    fragment = PodcastFragment.newInstance()
                    true
                }
                R.id.tab_more -> {
                    setPageTitle(getString(R.string.more))
                    fragment = SettingsFragment.newInstance()
                    true
                }
                else -> false
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, it)
                    .commit()
            }
            if (item.itemId == R.id.tab_ai_assistant) {
                AssistantActivity.navigate(this)
            }
            selectTab
        }
        if (supportFragmentManager.fragments.isEmpty()) {
            setPageTitle(getString(R.string.insights))
            supportFragmentManager.beginTransaction()
                .add(R.id.content_container, InsightsFragment.newInstance())
                .commit()

        }
    }

    private fun showAddCollectionDialog() {
        val customView = LayoutInflater.from(this).inflate(R.layout.layout_url_input_dialog, null)
        val editText = customView.findViewById<TextInputEditText>(R.id.edit_text)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Add Collection")
            .setView(customView)
            .setPositiveButton("OK") { _, _ ->
                viewModel.addCollection(editText.text.toString())
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}