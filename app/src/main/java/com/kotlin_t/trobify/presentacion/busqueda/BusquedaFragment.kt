package com.kotlin_t.trobify.presentacion.busqueda

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentBusquedaBinding
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.logica.busqueda.BusquedaViewModel
import com.kotlin_t.trobify.logica.busqueda.BusquedaViewModelFactory
import com.kotlin_t.trobify.logica.filtrar.FiltrarViewModel
import com.kotlin_t.trobify.logica.filtrar.criteria.Busqueda.BusquedaCriteria
import com.kotlin_t.trobify.persistencia.Busqueda
import java.util.*

class BusquedaFragment : Fragment() {
    lateinit var binding: FragmentBusquedaBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var busquedaViewModel: BusquedaViewModel
    lateinit var filtrarViewModel: FiltrarViewModel

    lateinit var datasource: AppDatabase
    lateinit var sharedModel: com.kotlin_t.trobify.logica.ContextClass

    lateinit var locationManager: LocationManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_busqueda, container, false)
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        sharedModel = ViewModelProvider(requireActivity()).get(ContextClass::class.java)
        val viewModelFactory =
            BusquedaViewModelFactory(datasource, application, sharedModel, viewLifecycleOwner)
        busquedaViewModel =
            ViewModelProvider(this, viewModelFactory).get(BusquedaViewModel::class.java)
        binding.viewModel = busquedaViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        busquedaViewModel.search("")
        setRecyclerView(busquedaViewModel.listaBusquedas.reversed())
        setSearchButton()
        setCampoBusqueda()
        setMiUbicacionButton()
    }

    private fun setMiUbicacionButton() {
        binding.miUbicacionButton.setOnClickListener {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    101
                )
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.toFloat(),
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0.toFloat(),
                locationListener
            )
        }
    }

    private fun setCampoBusqueda() {
        binding.campoBusqueda.doAfterTextChanged {
            if (binding.campoBusqueda.text.toString() == "") {
                setRecyclerView(busquedaViewModel.listaBusquedas.reversed())
            } else {
                var busquedas =
                    BusquedaCriteria(binding.campoBusqueda.text.toString()).meetCriteriaBusqueda(
                        busquedaViewModel.listaMunicipios.toList()
                    )
                setRecyclerView(busquedas)
            }
        }
    }

    private fun setSearchButton() {
        binding.searchButton.setOnClickListener {
            busquedaViewModel.search(binding.campoBusqueda.text.toString())
            val action = BusquedaFragmentDirections.actionBusquedaFragmentToNavHome()
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView(busquedas: List<Busqueda>) {
        recyclerView = binding.historialBusquedas

        recyclerView.adapter = BusquedaAdapter(
            requireContext(), busquedas, busquedaViewModel
        )
    }

    var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            setDireccion(location.latitude, location.longitude)
            locationManager.removeUpdates(this)
        }
    }

    fun setDireccion(latitud: Double, longitud: Double) {
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
        var direccion: List<Address> = geocoder.getFromLocation(
            latitud,
            longitud,
            1
        )
        binding.campoBusqueda.setText(direccion[0].locality.toString())
    }
}

