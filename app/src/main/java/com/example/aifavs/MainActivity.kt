package com.example.aifavs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aifavs.base.BaseMediaControlActivity
import com.example.aifavs.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : BaseMediaControlActivity<ActivityMainBinding>() {
    private lateinit var viewModel: MainViewModel
    private lateinit var appBarConfiguration : AppBarConfiguration

    companion object {
        const val KEY_DEST = "destination"

        fun navigate(context: Context, bundle: Bundle = Bundle()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNav()
        setupMiniPlayer(binding.miniPlayer.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val destination = intent?.getIntExtra(KEY_DEST, R.id.tab_collections) ?: return
        val navController = findNavController(R.id.nav_host_fragment)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_main_activity, true)
            .build()
        navController.navigate(destination,null, navOptions)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                showAddCollectionDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initNav() {
        setSupportActionBar(binding.toolBar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = setOf(
            R.id.tab_insights,
            R.id.tab_collections,
            R.id.tab_podcast,
            R.id.tab_more
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)
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