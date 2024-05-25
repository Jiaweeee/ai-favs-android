package com.example.aifavs

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.aifavs.assistant.AssistantActivity
import com.example.aifavs.base.BaseActivity
import com.example.aifavs.collections.CollectionHomeFragment
import com.example.aifavs.insights.InsightsFragment
import com.example.aifavs.podcast.PodcastFragment
import com.example.aifavs.settings.SettingsFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun setPageTitle(title: String) {
        val toolBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolBar.title = title
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
}