package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nathaniel.motus.cavevin.databinding.FragmentCellarBinding
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModelFactory
import kotlinx.coroutines.launch

class CellarItemListFragment : Fragment(), BottomBarFragment.BottomBarParentFragment {

    private var _binding: FragmentCellarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BottleListViewModel by viewModels{
        BottleListViewModelFactory(requireActivity().application)
    }
    private lateinit var recyclerView: RecyclerView
    private var bottomBarFragment: BottomBarFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycle.coroutineScope.launch {
            viewModel.updateBottleListViewModel()
        }

        val fragmentBinding = FragmentCellarBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()
        configureBottomBar()
        setFilterStatesObserver()
    }

    private fun configureRecyclerView() {
        recyclerView = binding.fragmentCellarRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val adapter = CellarItemAdapter(viewModel)
        recyclerView.adapter = adapter
        viewModel.cellarItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    private fun configureBottomBar() {
        bottomBarFragment =
            childFragmentManager.findFragmentById(binding.fragmentCellarFrameLayout.id) as BottomBarFragment?
        if (bottomBarFragment == null) {
            bottomBarFragment = BottomBarFragment(this)
            childFragmentManager.beginTransaction()
                .add(binding.fragmentCellarFrameLayout.id, bottomBarFragment!!).commit()
        }
    }

    private fun setFilterStatesObserver() {
        viewModel.emptyIsEnable.observe(viewLifecycleOwner) {
            bottomBarFragment?.emptyButtonState = it
            bottomBarFragment?.applyButtonStates()
        }

        viewModel.sparklingIsEnable.observe(viewLifecycleOwner){
            bottomBarFragment?.sparklingButtonState=it
            bottomBarFragment?.applyButtonStates()
        }

        viewModel.pinkIsEnable.observe(viewLifecycleOwner){
            bottomBarFragment?.pinkButtonState=it
            bottomBarFragment?.applyButtonStates()
        }

        viewModel.redIsEnable.observe(viewLifecycleOwner){
            bottomBarFragment?.redButtonState=it
            bottomBarFragment?.applyButtonStates()
        }

        viewModel.whiteIsEnable.observe(viewLifecycleOwner){
            bottomBarFragment?.whiteButtonState=it
            bottomBarFragment?.applyButtonStates()
        }

        viewModel.stillIsEnable.observe(viewLifecycleOwner){
            bottomBarFragment?.stillButtonState=it
            bottomBarFragment?.applyButtonStates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun notifyButtonClicked(buttonTag: String) {
        when (buttonTag) {
            BottomBarFragment.STILL_BUTTON -> viewModel.changeFilter(BottleListViewModel.STILL_FILTER)
            BottomBarFragment.SPARKLING_BUTTON -> viewModel.changeFilter(BottleListViewModel.SPARKLING_FILTER)
            BottomBarFragment.RED_BUTTON -> viewModel.changeFilter(BottleListViewModel.RED_FILTER)
            BottomBarFragment.WHITE_BUTTON -> viewModel.changeFilter(BottleListViewModel.WHITE_FILTER)
            BottomBarFragment.PINK_BUTTON -> viewModel.changeFilter(BottleListViewModel.PINK_FILTER)
            BottomBarFragment.EMPTY_BUTTON -> viewModel.changeFilter(BottleListViewModel.EMPTY_FILTER)
        }
    }
}