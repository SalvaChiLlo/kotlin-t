package com.kotlin_t.trobify.presentacion.ordenacion

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.OrdenacionFragmentBinding
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.home.HomeViewModel
import com.kotlin_t.trobify.presentacion.home.HomeViewModelFactory

class OrdenacionFragment : Fragment() {
    private lateinit var binding: OrdenacionFragmentBinding
    private lateinit var viewModel: OrdenacionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.ordenacion_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = OrdenacionViewModelFactory(datasource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrdenacionViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            ordenacionRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = OrdenacionItemAdapter(requireContext(), Constantes.loadCriterios())
            }
        }
        return binding.root
    }



}