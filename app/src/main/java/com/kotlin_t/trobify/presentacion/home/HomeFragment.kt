package com.kotlin_t.trobify.presentacion.home

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.databinding.FragmentHomeBinding
import com.kotlin_t.trobify.logica.home.HomeViewModel
import com.kotlin_t.trobify.logica.home.HomeViewModelFactory
import com.kotlin_t.trobify.logica.ContextClass


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var contextClass: ContextClass
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        contextClass = ViewModelProvider(requireActivity()).get(ContextClass::class.java)
        val application = requireNotNull(this.activity).application
        database = AppDatabase.getDatabase(application)
        val viewModelFactory = HomeViewModelFactory(database, application, contextClass)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        contextClass = ViewModelProvider(requireActivity()).get(ContextClass::class.java)

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            contextClass.inmuebles.observe(viewLifecycleOwner, {
                recyclerView.adapter = HomeItemAdapter(requireContext(), contextClass.inmuebles.value!!, homeViewModel)
                setAviso()
            })
        }
        contextClass.recuperarSesionActual()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filterButton = view.findViewById<ImageView>(R.id.filter_button)
        val orderButton = view.findViewById<ImageView>(R.id.order_button)
        val locationButton = view.findViewById<ImageView>(R.id.location_button)
        val busquedaButton = view.findViewById<ImageView>(R.id.busqueda_button)



        setAviso()

        filterButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToFiltrarFragment()
            findNavController().navigate(action)
        }

        orderButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToOrdenacionFragment())
        }

        locationButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToMapsFragment())
        }
        busquedaButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToBusquedaFragment())
        }

    }

    private fun setAviso() {
        if (database.inmuebleDAO().getAll().isEmpty()) {
            binding.aviso.text =
                "Espere... Cargando Inmuebles...\nEn unos segundos cargarán los inmuebles"
        } else if (contextClass.inmuebles.value!!.isEmpty()) {
            binding.aviso.text = "No hay inmuebles para los\ncriterios de búsqueda seleccionados"
        } else {
            binding.aviso.text = ""
        }
    }

}