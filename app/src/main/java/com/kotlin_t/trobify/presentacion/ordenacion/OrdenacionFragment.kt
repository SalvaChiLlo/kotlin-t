package com.kotlin_t.trobify.presentacion.ordenacion

import android.app.Application
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
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.logica.ordenacion.OrdenacionViewModel
import com.kotlin_t.trobify.logica.ordenacion.OrdenacionViewModelFactory

class OrdenacionFragment : Fragment() {
    private lateinit var binding: OrdenacionFragmentBinding
    private lateinit var ordenacionViewModel: OrdenacionViewModel
    private lateinit var ordenacionViewModelFactory: OrdenacionViewModelFactory
    private lateinit var contextClass: ContextClass
    private lateinit var database: AppDatabase
    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.ordenacion_fragment, container, false)
        application = requireNotNull(this.activity).application
        database = AppDatabase.getDatabase(application)
        contextClass = ViewModelProvider(requireActivity()).get(ContextClass::class.java)
        ordenacionViewModelFactory = OrdenacionViewModelFactory(database, application, contextClass)
        ordenacionViewModel = ViewModelProvider(this, ordenacionViewModelFactory).get(OrdenacionViewModel::class.java)
        binding.ordenacionRecyclerview.adapter = OrdenacionItemAdapter(ordenacionViewModel, Constantes.loadCriterios(),this)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            ordenacionRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)

            }
        }
        return binding.root
    }


}