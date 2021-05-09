package com.kotlin_t.trobify.presentacion.mapa

import android.graphics.Bitmap
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.SharedViewModel

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val zoomLevel = 10f
    private var markers: MutableList<Marker> = ArrayList()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))
        /*
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        setMarkers(map)
        setMarkersListeners(map)
        setZoomToFitMarkers(map)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun setMarkers(map: GoogleMap) {

        // Obtener lista de inmuebles
        val listaInmuebles: List<Inmueble>? = sharedViewModel.inmuebles.value
        if (listaInmuebles != null) {


            var latitud: Double;
            var longitud: Double;
            var localizacion: LatLng;
            var marker: Marker
            var bitmap: Bitmap
            var precio: String

            var iconGenerator = IconGenerator(this.requireContext())

            // Crear un marcador en el mapa por cada inmueble
            for (inmueble in listaInmuebles) {

                if(sharedViewModel.isInmuebleFavorito(inmueble.inmuebleId)) {
                    iconGenerator.setStyle(IconGenerator.STYLE_RED)
                } else { iconGenerator.setStyle(IconGenerator.STYLE_BLUE)}
                latitud = inmueble.latitud!!
                longitud = inmueble.longitud!!
                localizacion = LatLng(latitud, longitud)

                precio = inmueble.precio.toString() + "€"
                bitmap = iconGenerator.makeIcon(precio)

                marker = map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(localizacion))

                // Información adicional para el CustomInfoWindow
                marker.tag = inmueble

                markers.add(marker)

            }
        }

    }

    fun setMarkersListeners(map: GoogleMap) {

        // Cuando se aprieta en el InfoWindow de un Marker, se abre la ficha del inmueble
        map.setOnInfoWindowClickListener { marker ->

            val localizacion = marker.position
            val inmuebleId = sharedViewModel.getInmuebleIdFromLatLng(localizacion)

            // Abrir la ficha del inmueble
            findNavController().navigate(
                MapsFragmentDirections.actionMapsFragmentToFichaFragment(
                    inmuebleId
                )
            )

            true
        }

        // Apretar en el Marker muestra y esconde la información del inmueble en el mapa
        map.setOnMarkerClickListener { marker ->

            if(!marker.isInfoWindowShown) {
                marker.showInfoWindow()
            } else {
                marker.hideInfoWindow()
            }

            true
        }

    }

    fun setZoomToFitMarkers(map: GoogleMap) {

        if(markers.isEmpty()) return

        var boundsBuilder = LatLngBounds.builder()

        markers.forEach{ boundsBuilder.include(it.position) }

        val bounds = boundsBuilder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
        map.moveCamera(cu)
        map.animateCamera(cu)

    }




}

