package com.kekulta.dummyproducts.features.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.ActivityMainBinding
import com.kekulta.dummyproducts.features.list.presentation.ui.ListFragment
import logcat.logcat


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = binding.rootNavFragment.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController

        navController.graph = navController.createGraph(
            startDestination = "list"
        ) {
            fragment<ListFragment>("list") {
                label = "List"
            }
        }

        onBackPressedDispatcher.addCallback { navController.popBackStack() }
    }
}


