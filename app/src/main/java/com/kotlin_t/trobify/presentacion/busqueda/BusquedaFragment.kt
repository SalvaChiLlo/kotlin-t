package com.kotlin_t.trobify.presentacion.busqueda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentBusquedaBinding
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModelFactory
import com.kotlin_t.trobify.presentacion.filtrar.FiltrarViewModel

class BusquedaFragment : Fragment() {
    lateinit var binding: FragmentBusquedaBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var busquedaViewModel: BusquedaViewModel
    lateinit var filtrarViewModel: FiltrarViewModel

    lateinit var datasource: AppDatabase
    lateinit var sharedModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_busqueda, container, false)
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        sharedModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = BusquedaViewModelFactory(datasource, application, sharedModel)
        busquedaViewModel = ViewModelProvider(this, viewModelFactory).get(BusquedaViewModel::class.java)
        binding.viewModel = busquedaViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.historialBusquedas

        recyclerView.adapter = BusquedaAdapter(
            requireContext(), busquedaViewModel.listaBusquedas.reversed(), busquedaViewModel
        )

        binding.searchButton.setOnClickListener {
            busquedaViewModel.search(binding.campoBusqueda.text.toString())
            val action = BusquedaFragmentDirections.actionBusquedaFragmentToNavHome()
            findNavController().navigate(action)
        }
    }
}