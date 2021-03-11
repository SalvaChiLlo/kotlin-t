package com.kotlin_t.trobify.presentacion.ficha

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlin_t.trobify.R

class FichaFragment : Fragment() {

    companion object {
        fun newInstance() = FichaFragment()
    }

    private lateinit var viewModel: FichaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ficha_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FichaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}