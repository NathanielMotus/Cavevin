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
import com.nathaniel.motus.cavevin.databinding.FragmentBottleEditBinding
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
        return fragmentBottleEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.appellation.observe(viewLifecycleOwner) {
            binding.appellationTextInput.setText(it)
        }
        viewModel.domain.observe(viewLifecycleOwner) {
            binding.domainTextInput.setText(it)
        }
        viewModel.cuvee.observe(viewLifecycleOwner) {
            binding.cuveeTextInput.setText(it)
        }
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
        viewModel.rating.observe(viewLifecycleOwner) {
            binding.ratingView.rating = it
            binding.ratingView.displayMode = RatingView.EDITABLE_MODE
        }
        viewModel.imageName.observe(viewLifecycleOwner) {
            if (it != "") {
                binding.bottleImageView.setImageBitmap(
                    CellarStorageUtils.getBitmapFromInternalStorage(
                        binding.root.context.filesDir,
                        binding.root.context.resources.getString(R.string.photo_folder_name), it
                    )
                )
                binding.bottleImageView.visibility = View.VISIBLE
            } else {
                binding.bottleImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.photo_frame
                    )
                )
                binding.bottleImageView.visibility = View.VISIBLE
            }
        }

    }

    companion object {
        const val BOTTLE_ID = "bottleId"
    }
}

//todo implement top bar
//todo implement photo picker
//todo implement bottle type list
//todo implement price format
