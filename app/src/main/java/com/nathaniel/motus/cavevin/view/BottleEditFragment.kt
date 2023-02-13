package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.databinding.FragmentBottleEditBinding
import com.nathaniel.motus.cavevin.viewmodels.BottleEditViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleEditViewModelFactory
import kotlinx.coroutines.launch

class BottleEditFragment : Fragment() {
    private var _binding: FragmentBottleEditBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: BottleEditViewModel by viewModels {
        BottleEditViewModelFactory(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.bottleID = it.getInt(BOTTLE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.coroutineScope.launch {
            viewModel.updateBottleEditViewModel()
        }
        val fragmentBottleEditBinding =
            FragmentBottleEditBinding.inflate(inflater, container, false)
        _binding = fragmentBottleEditBinding
        return fragmentBottleEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers(){
        viewModel.bottleAppellation.observe(viewLifecycleOwner){
            binding.appellationTextInput.setText(it)
        }
    }

    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}

//todo add image placeholder
//todo implement top bar
