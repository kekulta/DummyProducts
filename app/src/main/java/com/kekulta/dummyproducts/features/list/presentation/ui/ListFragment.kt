package com.kekulta.dummyproducts.features.list.presentation.ui


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.FragmentListBinding
import com.kekulta.dummyproducts.features.list.domain.viewmodels.ListViewModel
import com.kekulta.dummyproducts.features.list.presentation.recycler.BottomOffsetDecoration
import com.kekulta.dummyproducts.features.list.presentation.recycler.GridSpacingItemDecoration
import com.kekulta.dummyproducts.features.list.presentation.recycler.ProductsRecyclerAdapter
import com.kekulta.dummyproducts.features.shared.utils.dip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment(R.layout.fragment_list) {

    private val binding by viewBinding(FragmentListBinding::bind)
    private val viewModel: ListViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recAdapter = ProductsRecyclerAdapter()

        viewModel.attachUiEventProvider(binding.bottomBar)
        viewModel.attachUiEventProvider(recAdapter)

        binding.productsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recAdapter
            addItemDecoration(GridSpacingItemDecoration(2, dip(8), true))
            addItemDecoration(
                BottomOffsetDecoration(dip(60))
            )
        }


        lifecycleScope.launch {

            viewModel.observeState().collect { state ->
                recAdapter.submitList(state.content)
                binding.bottomBar.bind(state.pagingState)
            }
        }
    }
}