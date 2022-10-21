package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nathaniel.motus.cavevin.databinding.FragmentBottleEditBinding

class BottleEditFragment : Fragment() {
    private var _binding: FragmentBottleEditBinding? = null
    private val binding
        get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBottleEditBinding=FragmentBottleEditBinding.inflate(inflater,container,false)
        _binding=fragmentBottleEditBinding
        return fragmentBottleEditBinding.root
    }
}