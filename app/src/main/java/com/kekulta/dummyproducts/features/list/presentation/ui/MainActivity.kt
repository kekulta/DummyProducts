package com.kekulta.dummyproducts.features.list.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.ActivityMainBinding
import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage
import com.kekulta.dummyproducts.features.list.domain.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        lifecycleScope.launch {
            viewModel.observeMessages().collect { mes ->
                showMessage(mes)
            }
        }
    }

    private fun showMessage(mes: AlertMessage) {
        MaterialAlertDialogBuilder(this)
            .setTitle(mes.title)
            .setMessage(mes.text)
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
            .show()
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

            fragment<DetailsFragment>("product/{${DetailsFragment.PRODUCT_ID_ARG}}") {
                label = "Details"
                argument(DetailsFragment.PRODUCT_ID_ARG) {
                    type = NavType.IntType
                }
            }
        }
    }
}


