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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.presentacion.mapa.CustomInfoWindowForGoogleMap
import java.util.*
import kotlin.properties.Delegates


class FichaFragment : Fragment() {
    var inmuebleId by Delegates.notNull<Int>()
    private lateinit var map: GoogleMap
    private val zoomLevel = 10f

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))

        /*
        val sydney = LatLng(fichaViewModel.inmueble.latitud!!, fichaViewModel.inmueble.latitud!!)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        setMarkers(map)
        //setMarkersListeners(map)
        setZoomOnFirstInmueble(map)
    }

    companion object {
        fun newInstance() = FichaFragment()
    }

    private lateinit var fichaViewModel: FichaViewModel
    lateinit var datasource: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
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

        val mapButton : ImageButton = view.findViewById(R.id.mapa)


        mapButton.setOnClickListener {

            mapClickHandler()

        }

        val phoneButton : ImageButton = view.findViewById(R.id.llamada)

        phoneButton.setOnClickListener {
            val phone = view.findViewById<TextView>(R.id.textoTelefono)
            phoneClickHandler(phone.text.toString())
        }

        setFavouriteIcon(view.findViewById<ImageView>(R.id.favImage))
    }

/*
    override fun onMapReady(googleMap: GoogleMap?) {
        //configura el mapa
        googleMap?.apply {
            val mark : LatLng
            if(fichaViewModel.inmueble.latitud != null)
                mark = LatLng(fichaViewModel.inmueble.latitud!!, fichaViewModel.inmueble?.longitud!!)
            else mark = LatLng(0.0, 0.0)
            addMarker(
                MarkerOptions().position(mark).title(fichaViewModel.inmueble.titulo)
            )
            moveCamera(CameraUpdateFactory.newLatLng(mark))
            MapsInitializer.initialize(activity)
        }
    }
    */

    fun updateMedia(container: View) {
        setText(container)
        setPhotos(container)
        setMap(container) //próxima versión
    }

    private fun setText(container: View) {
        //update de todos los textos
        container!!.findViewById<MaterialTextView>(R.id.textoTitulo).text = fichaViewModel.inmueble.titulo
        container!!.findViewById<TextView>(R.id.textoPrecio).text =
            if (fichaViewModel.inmueble.operacion == "alquiler")
                getString(R.string.precioMes, fichaViewModel.inmueble.precio)
            else
                getString(R.string.precio, fichaViewModel.inmueble.precio)
        container!!.findViewById<TextView>(R.id.textoCompra).text = fichaViewModel.inmueble.operacion
        container!!.findViewById<TextView>(R.id.textoDescripcion).text = fichaViewModel.inmueble.descripcion
        container!!.findViewById<TextView>(R.id.textoUsuario).text = fichaViewModel.usuario.nombre + " " + fichaViewModel.usuario.apellidos

        //Direccion
        val geocoder : Geocoder = Geocoder(context, Locale.getDefault())
        var dir : String = ""
        if (fichaViewModel.inmueble.latitud != null) {
            var direccion: List<Address> = geocoder.getFromLocation(
                fichaViewModel.inmueble.latitud!!,
                fichaViewModel.inmueble.longitud!!,
                1
            )
            dir += direccion.get(0).getAddressLine(0)
        } else dir = "Dirección desconocida"

        container!!.findViewById<TextView>(R.id.textoCalle).text = dir

        setCaracteristicas(container)

        container!!.findViewById<TextView>(R.id.textoTelefono).text = fichaViewModel.usuario.telefono

    }

    private fun setPhotos(container: View) {
        //foto de la casa y de foto de perfil
        container!!.findViewById<ImageView>(R.id.fotoPortal).setImageBitmap(fichaViewModel.inmueble.miniatura)
        //container!!.findViewById<ImageView>(R.id.favImage).setImageResource(R.drawable.ic_baseline_favorite_24)
        //Añadir cuando se haga el viewmodel completo

        if (fichaViewModel.usuario.fotoPerfil != null)
            container!!.findViewById<ImageView>(R.id.fotoUsuario).setImageBitmap(fichaViewModel.usuario.fotoPerfil)
    }

    private fun setMap(container: View) {
        //inicializamos el mapa en esta sección
        //mapView = container!!.findViewById<MapView>(R.id.mapView)
        //mapView?.getMapAsync(this)


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

    private fun setCaracteristicas(container: View) {
        var addText : String = "Desconocido"
        setText(
            container!!.findViewById<TextView>(R.id.textoEstado),
            fichaViewModel.inmueble.estado!!
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoNuevo),
            if (fichaViewModel.inmueble.nuevoDesarrollo!!) "Si" else "No"
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoDimensiones),
            fichaViewModel.inmueble.tamano!!.toString()
        )
        setM2(container!!.findViewById<TextView>(R.id.textoDimensiones), "")
        setText(
            container!!.findViewById<TextView>(R.id.textoTipo),
            fichaViewModel.inmueble.tipoDeInmueble!!
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoAltura),
            fichaViewModel.inmueble.altura!!.toString()
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoHabitaciones),
            fichaViewModel.inmueble.habitaciones!!.toString()
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoBanos),
            fichaViewModel.inmueble.banos!!.toString()
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoAscensor),
            if (fichaViewModel.inmueble.tieneAscensor!!) "Si" else "No"
        )
        setText(
            container!!.findViewById<TextView>(R.id.textoExterior),
            if (fichaViewModel.inmueble.exterior!!) "Si" else "No"
        )
        container!!.findViewById<TextView>(R.id.textoPrecioMetro).text = "- Precio por "
        setM2(container!!.findViewById<TextView>(R.id.textoPrecioMetro), ": ")
        //container!!.findViewById<TextView>(R.id.textoPrecioMetro).text = "- Precio por m"
        setText(
            container!!.findViewById<TextView>(R.id.textoPrecioMetro),
            fichaViewModel.inmueble.precioPorMetro!!.toString() + "€"
        )
    }

    private fun setText(textView: TextView, text: String) {
        textView.append(text)
    }

    private fun setFavouriteIcon(favorito: ImageView) {
        var fav:Int
        if(fichaViewModel.favoritoDatabase.findById(fichaViewModel.inmueble.inmuebleId) != null)
            fav = R.drawable.ic_baseline_favorite_24
        else fav = R.drawable.ic_baseline_favorite_border_24
        favorito.setImageResource(fav)
        favorito.setOnClickListener{
            if(fav == R.drawable.ic_baseline_favorite_24){
                fichaViewModel.favoritoDatabase.deleteById(fichaViewModel.inmueble.inmuebleId)
                fav = R.drawable.ic_baseline_favorite_border_24
            }
            else{
                fichaViewModel.favoritoDatabase.insertAll(
                    Favorito(
                        fichaViewModel.inmueble.inmuebleId,
                        null
                    )
                )
                fav = R.drawable.ic_baseline_favorite_24
            }
            favorito.setImageResource(fav)
        }
    }

    private fun setM2(textView: TextView, secondWord: String) {
        /*val superscriptSpan = SuperscriptSpan()
        val builder = SpannableStringBuilder(text)
        builder.setSpan(
            superscriptSpan,
            text.indexOf("2"),
            text.indexOf("2") + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(firstWord)
        textView.append(builder)
        textView.append(secondWord)*/

        val html = "m<sup>2</sup>"
        //textView.setText(firstWord)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.append(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY))
        }
        else{
            textView.append(Html.fromHtml(html))
        }
        textView.append(secondWord)
    }

    fun setMarkers(map: GoogleMap) {
        /*
        // Obtener lista de inmuebles
        val listaInmuebles: List<Inmueble>? = sharedViewModel.inmuebles.value
        if (listaInmuebles != null) {


            var latitud: Double;
            var longitud: Double;
            var localizacion: LatLng;
            var marker: Marker

            // Crear un marcador en el mapa por cada inmueble
            for (inmueble in listaInmuebles) {

                latitud = inmueble.latitud!!
                longitud = inmueble.longitud!!
                localizacion = LatLng(latitud, longitud)

                marker = map.addMarker(MarkerOptions().position(localizacion))

                // Información adicional para el CustomInfoWindow
                marker.tag = inmueble

            }
        }*/
        val localizacion = LatLng(
            fichaViewModel.inmueble.latitud!!,
            fichaViewModel.inmueble.longitud!!
        )
        val marker = map.addMarker(MarkerOptions().position(localizacion))

    }

    fun setZoomOnFirstInmueble(map: GoogleMap) {

        val inmueble = fichaViewModel.inmueble
        val localizacion: LatLng
        if(inmueble != null) {
            localizacion = LatLng(inmueble.latitud!!, inmueble.longitud!!)
        } else {
            localizacion = LatLng(39.46854170253597, -0.376975419650787) // Valencia Centro
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, zoomLevel))

    }
}