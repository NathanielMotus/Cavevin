package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.cellar_database.Bottle
import com.nathaniel.motus.cavevin.databinding.FragmentBottleEditBinding
import com.nathaniel.motus.cavevin.ui.bottle_edit.BottleEditContent
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModelFactory
import kotlinx.coroutines.launch

class BottleEditFragment : Fragment() {
    private var _binding: FragmentBottleEditBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: BottleDetailViewModel by viewModels {
        BottleDetailViewModelFactory(requireActivity().application)
    }

    private lateinit var bottle:Bottle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.bottleId = it.getInt(BOTTLE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycle.coroutineScope.launch {
            viewModel.updateBottleDetailViewModel()
        }
        val fragmentBottleEditBinding =
            FragmentBottleEditBinding.inflate(inflater, container, false)
        _binding = fragmentBottleEditBinding
        binding.composeView.setContent { BottleEditContent(viewModel=viewModel) }
        return fragmentBottleEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.vintage.observe(viewLifecycleOwner) {
            binding.vintageTextInput.setText(it)
        }
        viewModel.origin.observe(viewLifecycleOwner) {
            binding.originTextInput.setText(it)
        }
        viewModel.comment.observe(viewLifecycleOwner) {
            binding.commentTextInput.setText(it)
        }
        viewModel.agingCapacity.observe(viewLifecycleOwner) {
            binding.agingTextInput.setText(it)
        }
        viewModel.price.observe(viewLifecycleOwner) {
            binding.priceTextInput.setText(it.toString())
        }
    }

    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}

//todo choose wine color and stillness
//todo implement top bar
//todo implement photo picker
//todo implement bottle type list
//todo implement price format
