package com.nathaniel.motus.cavevin.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.databinding.FragmentBottleDetailBinding
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModelFactory
import kotlinx.coroutines.launch

class BottleDetailFragment : Fragment() {
    private var _binding: FragmentBottleDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BottleDetailViewModel by viewModels {
        BottleDetailViewModelFactory(requireActivity().application)
    }

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
    ): View? {
        lifecycle.coroutineScope.launch {
            viewModel.updateBottleDetailViewModel()
        }
        val fragmentBottleDetailBinding =
            FragmentBottleDetailBinding.inflate(inflater, container, false)
        _binding = fragmentBottleDetailBinding
        return fragmentBottleDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        binding.bottleDetailMaterialToolbar.inflateMenu(R.menu.menu_bottle_detail)
        binding.bottleDetailMaterialToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.bottleDetailMaterialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_bottle_detail_edit -> {
                    binding.bottleDetailMaterialToolbar.findNavController()
                        .navigate(BottleDetailFragmentDirections.actionBottleFragmentToBottleEditFragment(viewModel.bottleId))
                    true
                }
                else -> false
            }
        }
    }


    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}