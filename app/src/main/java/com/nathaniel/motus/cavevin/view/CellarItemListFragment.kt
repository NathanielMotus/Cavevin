package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nathaniel.motus.cavevin.databinding.FragmentCellarBinding
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel
import com.nathaniel.motus.cavevin.viewmodels.CellarViewModelFactory
import kotlinx.coroutines.launch
import java.lang.Exception

class CellarItemListFragment : Fragment() {

    private var _binding: FragmentCellarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BottleListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.coroutineScope.launch {
            viewModel.updateBottleListViewModel()
        }

        val fragmentBinding = FragmentCellarBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.fragmentCellarRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        var adapter: CellarItemAdapter? = CellarItemAdapter()
        recyclerView.adapter = adapter
        viewModel.cellarItems.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}