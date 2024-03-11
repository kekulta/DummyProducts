package com.kekulta.dummyproducts.features.list.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.FragmentDetailsBinding
import com.kekulta.dummyproducts.features.list.domain.viewmodels.DetailsViewModel
import com.kekulta.dummyproducts.features.list.presentation.recycler.DetailsRecyclerAdapter
import com.kekulta.dummyproducts.features.shared.utils.setNavBarColor
import kotlinx.coroutines.launch
import logcat.logcat
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.R as Rm

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: DetailsViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavBarColor(Rm.attr.colorSurface)

        val productId = arguments?.getInt(PRODUCT_ID_ARG)
        if (productId == null) {
            logcat { "No id was passed!" }
            findNavController().popBackStack()
        } else {
            requireActivity().onBackPressedDispatcher.addCallback { findNavController().popBackStack() }
            val recAdapter = DetailsRecyclerAdapter()

            binding.recommendationsRecycler.apply {
                layoutManager = GridLayoutManager(context, 2).apply {
                    spanSizeLookup = recAdapter.spanSizeLookup
                }
                adapter = recAdapter
            }

            lifecycleScope.launch {

                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getDetails(productId).collect { state ->
                        logcat { "Details: $state" }
                        recAdapter.submitDetails(state)
                    }
                }
            }
        }
    }

    companion object {
        const val PRODUCT_ID_ARG = "details_id_arg"
    }
}