package com.nathaniel.motus.cavevin.view

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.nathaniel.motus.cavevin.databinding.FragmentBottleBinding
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
        setObservers()
    }

    private fun setObservers() {
        viewModel.appellation.observe(viewLifecycleOwner) {
            binding.appellationTextView.text = it
        }
        viewModel.domain.observe(viewLifecycleOwner) {
            binding.domainTextView.text = it
        }
        viewModel.cuvee.observe(viewLifecycleOwner) {
            binding.cuveeTextView.text = it
        }
        viewModel.vintage.observe(viewLifecycleOwner) {
            binding.vintageTextView.text = it
        }
        viewModel.bottleTypeAndCapacity.observe(viewLifecycleOwner) {
            binding.bottleTypeTextView.text = it
        }
        viewModel.price.observe(viewLifecycleOwner) {
            if (it != "") {
                binding.priceTextView.text = it
                binding.priceLinearLayout.visibility = View.VISIBLE
            } else
                binding.priceLinearLayout.visibility = View.GONE
        }
        viewModel.agingCapacity.observe(viewLifecycleOwner) {
            if (it != "") {
                binding.agingCapacityTextView.text = it
                binding.agingCapacityLinearLayout.visibility = View.VISIBLE
            } else
                binding.agingCapacityLinearLayout.visibility = View.GONE
        }
        viewModel.wineColor.observe(viewLifecycleOwner){
            if (it!=""){
                binding.bottleDetailWineView.wineColor=it
                binding.bottleDetailWineView.visibility=View.VISIBLE
            }else
                binding.bottleDetailWineView.visibility=View.GONE
        }
        viewModel.wineStillness.observe(viewLifecycleOwner){
            if (it!=""){
                binding.bottleDetailWineView.wineStillness=it
                binding.bottleDetailWineView.visibility=View.VISIBLE
            }else
                binding.bottleDetailWineView.visibility=View.GONE
        }
        viewModel.origin.observe(viewLifecycleOwner){
            if (it!=""){
                binding.originTextView.text=it
                binding.originDataCardView.visibility=View.VISIBLE
            }else
                binding.originDataCardView.visibility=View.GONE
        }
        viewModel.comment.observe(viewLifecycleOwner){
            if (it!=""){
                binding.commentTextView.text=it
                binding.commentDataCardView.visibility=View.VISIBLE
            }else
                binding.commentDataCardView.visibility=View.GONE
        }
    }

    //todo : implement rating observer
    //todo : fix localization
    //todo : implement top bar
    //todo : implement photo observer (and collapse if absent)

    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}