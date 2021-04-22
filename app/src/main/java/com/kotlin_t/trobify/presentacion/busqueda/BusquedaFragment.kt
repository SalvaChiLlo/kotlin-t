package com.kotlin_t.trobify.presentacion.busqueda

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.filtrar.FiltrarViewModel
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Busqueda.BusquedaCriteria
import java.util.*

class BusquedaFragment : Fragment() {
    lateinit var binding: FragmentBusquedaBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var busquedaViewModel: BusquedaViewModel
    lateinit var filtrarViewModel: FiltrarViewModel

    lateinit var datasource: AppDatabase
    lateinit var sharedModel: SharedViewModel

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
        sharedModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = BusquedaViewModelFactory(datasource, application,sharedModel, viewLifecycleOwner)
        busquedaViewModel =
            ViewModelProvider(this, viewModelFactory).get(BusquedaViewModel::class.java)
        binding.viewModel = busquedaViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        busquedaViewModel.search("")

        recyclerView = binding.historialBusquedas

        recyclerView.adapter = BusquedaAdapter(
            requireContext(), busquedaViewModel.listaBusquedas.reversed(), busquedaViewModel
        )

        binding.searchButton.setOnClickListener {
            busquedaViewModel.search(binding.campoBusqueda.text.toString())
            val action = BusquedaFragmentDirections.actionBusquedaFragmentToNavHome()
            findNavController().navigate(action)
        }

        binding.campoBusqueda.doAfterTextChanged {
            if (binding.campoBusqueda.text.toString() == "") {
                recyclerView.adapter = BusquedaAdapter(
                    requireContext(), busquedaViewModel.listaBusquedas.reversed(), busquedaViewModel
                )
            } else {
                recyclerView.adapter = BusquedaAdapter(
                    requireContext(),
                    BusquedaCriteria(binding.campoBusqueda.text.toString()).meetCriteriaBusqueda(
                        busquedaViewModel.listaMunicipios.toList()
                    ),
                    busquedaViewModel
                )
            }
        }

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

