package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nathaniel.motus.cavevin.databinding.FragmentBottleBinding
import com.nathaniel.motus.cavevin.databinding.FragmentBottleDetailBinding

class BottleDetailFragment:Fragment() {
    private var _binding: FragmentBottleDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBottleDetailBinding=FragmentBottleDetailBinding.inflate(inflater,container,false)
        _binding=fragmentBottleDetailBinding
        return fragmentBottleDetailBinding.root
    }
}