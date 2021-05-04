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
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.ordenacion.OrdenacionViewModel
import com.kotlin_t.trobify.logica.ordenacion.OrdenacionViewModelFactory

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
        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = OrdenacionViewModelFactory(datasource, application, model)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrdenacionViewModel::class.java)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding.ordenacionRecyclerview.adapter = OrdenacionItemAdapter(sharedViewModel, Constantes.loadCriterios(),this)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            ordenacionRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)

            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

    }


}