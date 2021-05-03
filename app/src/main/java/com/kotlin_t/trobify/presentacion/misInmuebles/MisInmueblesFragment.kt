package com.kotlin_t.trobify.presentacion.misInmuebles

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.databinding.MisInmueblesFragmentBinding
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.presentacion.misInmuebles.MisInmueblesAdapter
import com.kotlin_t.trobify.presentacion.misInmuebles.MisInmueblesViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModelFactory
import com.kotlin_t.trobify.presentacion.misInmuebles.MisInmueblesViewModelFactory


class MisInmueblesFragment : androidx.fragment.app.Fragment() {

    lateinit var binding: MisInmueblesFragmentBinding
    lateinit var misInmueblesViewModel: MisInmueblesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.mis_inmuebles_fragment, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = MisInmueblesViewModelFactory(datasource, application, sharedViewModel)
        misInmueblesViewModel =
            ViewModelProvider(this, viewModelFactory).get(MisInmueblesViewModel::class.java)
        binding.viewModel = misInmueblesViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.misInmueblesRecyclerView

        recyclerView.adapter = MisInmueblesAdapter(
            requireContext(), misInmueblesViewModel.getMisInmuebles(), misInmueblesViewModel, misInmueblesViewModel.database
        )
    }

}