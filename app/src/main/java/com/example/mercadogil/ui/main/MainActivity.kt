package com.example.mercadogil.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.mercadogil.R
import com.example.mercadogil.databinding.ActivityMainBinding
import com.example.mercadogil.utils.FragmentConfig
import com.example.mercadogil.utils.FragmentConfig.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val productViewModel: ProductViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationController()
    }

    override fun onResume() {
        super.onResume()
        productViewModel.loadData()
    }

    private fun setupNavigationController() {
        navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration =
            AppBarConfiguration.Builder(setOf(R.id.navigation_home)).build()

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    fun setupLayoutNavigation(fragmentConfig: FragmentConfig) {
        when (fragmentConfig) {
            HOME -> setOptionsMenuVisibility(true)
            ADD_PRODUCT, EDIT_PRODUCT -> setOptionsMenuVisibility(false)
        }
    }

    private fun setOptionsMenuVisibility(isVisible: Boolean) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.setGroupVisible(R.id.menu_group, isVisible)
    }
}