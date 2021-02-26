package com.kotlin_t.trobify.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.databinding.FragmentHomeBinding
import com.kotlin_t.trobify.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {
    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textSlideshow.text = getText(R.string.text_home)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}