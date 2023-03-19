package com.nathaniel.motus.cavevin.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.cellar_database.Bottle
import com.nathaniel.motus.cavevin.databinding.FragmentBottleEditBinding
import com.nathaniel.motus.cavevin.ui.bottle_edit.BottleEditContent
import com.nathaniel.motus.cavevin.ui.bottle_edit.BottleEditScreen
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.bottleId = it.getInt(BOTTLE_ID)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
        binding.composeView.setContent { BottleEditScreen(viewModel = viewModel) }
        return fragmentBottleEditBinding.root
    }

    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}
