package com.nathaniel.motus.cavevin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.nathaniel.motus.cavevin.databinding.FragmentBottomBarBinding

class BottomBarFragment(private val fragment: BottomBarParentFragment) : Fragment() {

    private var _binding: FragmentBottomBarBinding? = null
    private val binding get() = _binding
    private val buttonClickListener=View.OnClickListener{
        fragment.notifyButtonClicked(it.tag as String)
    }
    var stillButtonState=true
    var sparklingButtonState=true
    var redButtonState=true
    var whiteButtonState=true
    var pinkButtonState=true
    var emptyButtonState=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding=FragmentBottomBarBinding.inflate(inflater,container,false)
        _binding=fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonTags()
        applyButtonStates()
    }

    private fun setButtonTags(){
        binding?.bottomBarStillButton?.tag= STILL_BUTTON
        binding?.bottomBarStillButton?.setOnClickListener(buttonClickListener)
        binding?.bottomBarSparklingButton?.tag= SPARKLING_BUTTON
        binding?.bottomBarSparklingButton?.setOnClickListener(buttonClickListener)
        binding?.bottomBarRedButton?.tag= RED_BUTTON
        binding?.bottomBarRedButton?.setOnClickListener(buttonClickListener)
        binding?.bottomBarWhiteButton?.tag= WHITE_BUTTON
        binding?.bottomBarWhiteButton?.setOnClickListener(buttonClickListener)
        binding?.bottomBarPinkButton?.tag= PINK_BUTTON
        binding?.bottomBarPinkButton?.setOnClickListener(buttonClickListener)
        binding?.bottomBarEmptyButton?.tag= EMPTY_BUTTON
        binding?.bottomBarEmptyButton?.setOnClickListener(buttonClickListener)
    }

    fun applyButtonStates(){
        applyButtonState(binding?.bottomBarEmptyButton,emptyButtonState)
        applyButtonState(binding?.bottomBarRedButton,redButtonState)
        applyButtonState(binding?.bottomBarPinkButton,pinkButtonState)
        applyButtonState(binding?.bottomBarSparklingButton,sparklingButtonState)
        applyButtonState(binding?.bottomBarStillButton,stillButtonState)
        applyButtonState(binding?.bottomBarWhiteButton,whiteButtonState)
    }

    private fun applyButtonState(imageView: ImageView?, state:Boolean){
        if (state)
            imageView?.alpha=1f
        else imageView?.alpha=0.1f
    }

    interface BottomBarParentFragment{
        fun notifyButtonClicked(buttonTag:String)
    }

    companion object{
        const val STILL_BUTTON="stillButton"
        const val SPARKLING_BUTTON="sparklingButton"
        const val RED_BUTTON="redButton"
        const val WHITE_BUTTON="whiteButton"
        const val PINK_BUTTON="pinkButton"
        const val EMPTY_BUTTON="emptyButton"
    }
}