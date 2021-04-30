package com.kotlin_t.trobify.presentacion.ficha

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.mapa.CustomInfoWindowForGoogleMap
import com.kotlin_t.trobify.presentacion.mapa.MapsFragmentDirections
import java.util.*
import kotlin.properties.Delegates


class FichaFragment : Fragment() {
    var inmuebleId by Delegates.notNull<Int>()
    private lateinit var map: GoogleMap
    private lateinit var fichaViewModel: FichaViewModel
    lateinit var datasource: AppDatabase
    private lateinit var sharedViewModel: SharedViewModel

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))

        setMarkers(map)
        //setMarkersListeners(map)
        setZoom(map)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory =  FichaViewModelFactory(datasource, application)
        fichaViewModel = ViewModelProvider(this, viewModelFactory).get(FichaViewModel::class.java)
        arguments?.let {
            inmuebleId = it.getInt("InmuebleID")!!
        }
        fichaViewModel.setHouse(inmuebleId)

        return inflater.inflate(R.layout.ficha_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fichaViewModel = ViewModelProvider(this).get(FichaViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            inmuebleId = it.getInt("InmuebleID")!!
        }

        updateMedia(view)

        view.findViewById<ImageButton>(R.id.mapa).setOnClickListener {
            mapClickHandler()
        }

        view.findViewById<ImageButton>(R.id.llamada).setOnClickListener {
            val phone = view.findViewById<TextView>(R.id.textoTelefono)
            phoneClickHandler(phone.text.toString())
        }

        setFavouriteIcon(view.findViewById<ImageView>(R.id.favImage))

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapLocation) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun mapClickHandler() {
        val gmmIntentUri = "http://maps.google.com/maps?q="+ fichaViewModel.inmueble.latitud  +
                "," + fichaViewModel.inmueble.longitud +"("+ fichaViewModel.inmueble.titulo + ")&iwloc=A&hl=es"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gmmIntentUri))
        intent.setPackage("com.google.android.apps.maps")
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }

    private fun phoneClickHandler(phone : String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(intent)
    }

    fun updateMedia(container: View) {
        setText(container)
        setPhotos(container)
    }

    private fun setText(container: View) {
        container!!.findViewById<MaterialTextView>(R.id.textoTitulo).text = fichaViewModel.inmueble.titulo
        container!!.findViewById<TextView>(R.id.textoPrecio).text =
            if (fichaViewModel.inmueble.operacion == "alquiler")
                getString(R.string.precioMes, fichaViewModel.inmueble.precio)
            else
                getString(R.string.precio, fichaViewModel.inmueble.precio)
        container!!.findViewById<TextView>(R.id.textoCompra).text = fichaViewModel.inmueble.operacion
        container!!.findViewById<TextView>(R.id.textoDescripcion).text = fichaViewModel.inmueble.descripcion
        container!!.findViewById<TextView>(R.id.textoUsuario).text = fichaViewModel.usuario.nombre + " " + fichaViewModel.usuario.apellidos

        setCaracteristicas(container)

        //Direccion
        val geocoder = Geocoder(context, Locale.getDefault())
        var dir = ""
        if (fichaViewModel.inmueble.latitud != null) {
            var direccion: List<Address> = geocoder.getFromLocation(
                fichaViewModel.inmueble.latitud!!,
                fichaViewModel.inmueble.longitud!!,
                1
            )
            if(!direccion.isEmpty()) {
                dir += direccion.get(0).getAddressLine(0)
            } else {
                dir += fichaViewModel.inmueble.direccion
            }
        } else dir = "Dirección desconocida"

        container!!.findViewById<TextView>(R.id.textoCalle).text = dir
        container!!.findViewById<TextView>(R.id.textoTelefono).text = fichaViewModel.usuario.telefono
    }

    private fun setCaracteristicas(container: View) {
        setText(
            container!!.findViewById(R.id.textoEstado),
            fichaViewModel.inmueble.estado!!
        )
        setText(
            container!!.findViewById(R.id.textoNuevo),
            if (fichaViewModel.inmueble.nuevoDesarrollo!!) "Si" else "No"
        )
        setText(
            container!!.findViewById(R.id.textoDimensiones),
            fichaViewModel.inmueble.tamano!!.toString()
        )
        setM2(container!!.findViewById(R.id.textoDimensiones), "")
        setText(
            container!!.findViewById(R.id.textoTipo),
            fichaViewModel.inmueble.tipoDeInmueble!!
        )
        setText(
            container!!.findViewById(R.id.textoAltura),
            fichaViewModel.inmueble.altura!!.toString()
        )
        setText(
            container!!.findViewById(R.id.textoHabitaciones),
            fichaViewModel.inmueble.habitaciones!!.toString()
        )
        setText(
            container!!.findViewById(R.id.textoBanos),
            fichaViewModel.inmueble.banos!!.toString()
        )
        setText(
            container!!.findViewById(R.id.textoAscensor),
            if (fichaViewModel.inmueble.tieneAscensor!!) "Si" else "No"
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoExterior),
            if (fichaViewModel.inmueble.exterior!!) "Si" else "No"
        )
        container!!.findViewById<TextView>(R.id.textoPrecioMetro).text = "- Precio por "
        setM2(container!!.findViewById(R.id.textoPrecioMetro), ": ")
        setText(
            container!!.findViewById(R.id.textoPrecioMetro),
            fichaViewModel.inmueble.precioPorMetro!!.toString() + "€"
        )
    }


    private fun setText(textView: TextView, text: String) {
        textView.append(text)
    }

    private fun setM2(textView: TextView, secondWord: String) {
        val html = "m<sup>2</sup>"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.append(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY))
        }
        else{
            textView.append(Html.fromHtml(html))
        }
        textView.append(secondWord)
    }

    private fun setPhotos(container: View) {
        container!!.findViewById<ImageView>(R.id.fotoPortal).setImageBitmap(fichaViewModel.inmueble.miniatura)

        if (fichaViewModel.usuario.fotoPerfil != null)
            container!!.findViewById<ImageView>(R.id.fotoUsuario).setImageBitmap(fichaViewModel.usuario.fotoPerfil)
    }

    private fun setFavouriteIcon(favoritoIMG: ImageView) {
        var fav:Int
        val dni = if(sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        val favorito = datasource.favoritoDAO().findByIdandDni(inmuebleId, dni)

        if(favorito != null)
            fav = R.drawable.ic_baseline_favorite_24
        else fav = R.drawable.ic_baseline_favorite_border_24
        favoritoIMG.setImageResource(fav)
        favoritoIMG.setOnClickListener{
            if(fav == R.drawable.ic_baseline_favorite_24){
                fichaViewModel.favoritoDatabase.deleteByPK(favorito!!.primaryKey)
                fav = R.drawable.ic_baseline_favorite_border_24
            }
            else{
                fichaViewModel.favoritoDatabase.insertAll(
                    Favorito(
                        fichaViewModel.inmueble.inmuebleId,
                        dni
                    )
                )
                fav = R.drawable.ic_baseline_favorite_24
            }
            favoritoIMG.setImageResource(fav)
        }
    }

    fun setMarkers(map: GoogleMap) {
        val localizacion = LatLng(
            fichaViewModel.inmueble.latitud!!,
            fichaViewModel.inmueble.longitud!!
        )
        map.addMarker(MarkerOptions().position(localizacion))

        map.setOnMarkerClickListener { marker ->
            mapClickHandler()
            true
        }
    }

    fun setZoom(map: GoogleMap) {

        val inmueble = fichaViewModel.inmueble
        val boundsBuilder = LatLngBounds.builder()

        val localizacion : LatLng
        if(inmueble != null) {
            localizacion = LatLng(inmueble.latitud!!, inmueble.longitud!!)
        } else {
            localizacion = LatLng(39.46854170253597, -0.376975419650787) //Valencia Centro
        }
        boundsBuilder.include(localizacion)

        val cu = CameraUpdateFactory.newLatLng(localizacion)
        map.moveCamera(cu)
        map.animateCamera(cu)
        map.setMinZoomPreference(13f)
        map.setMaxZoomPreference(15f)

        map.uiSettings.isScrollGesturesEnabled = false
    }
}