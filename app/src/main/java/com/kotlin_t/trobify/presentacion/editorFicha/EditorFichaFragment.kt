package com.kotlin_t.trobify.presentacion.editorFicha

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import java.util.*

class EditorFichaFragment : Fragment() {
    lateinit var binding: FragmentEditorFichaBinding
    lateinit var editorFichaViewModel: EditorFichaViewModel
    lateinit var datasource: AppDatabase
    lateinit var sharedModel: SharedViewModel
    lateinit var imagesRecyclerView: RecyclerView
    lateinit var locationManager: LocationManager
    val args: EditorFichaFragmentArgs by navArgs()

    private lateinit var dniPropietario: String
    private lateinit var direccion: String
    private var nuevoDesarrollo: Boolean = false
    private var miniatura: Bitmap? = null
    private lateinit var URLminiatura: String
    private var altura: Int = -1
    private var precio: Int = -1
    private lateinit var tipoDeInmueble: String
    private lateinit var operacion: String
    private var tamano: Int = -1
    private var exterior: Boolean = false
    private var habitaciones: Int = -1
    private var banos: Int = -1
    private lateinit var provinciaInmueble: String
    private lateinit var municipioInmueble: String
    private lateinit var barrio: String
    private lateinit var pais: String
    private var latitud: Double = -1.0
    private var longitud: Double = -1.0
    private lateinit var estadoInmueble: String
    private var tieneAscensor: Boolean = false
    private var precioPorMetro: Int = -1
    private lateinit var titulo: String
    private lateinit var subtitulo: String
    private lateinit var descripcion: String
    private var codigoPostalInm: Int = -1

    companion object {
        const val PICK_IMAGE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_editor_ficha, container, false)
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        sharedModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = EditorFichaViewModelFactory(datasource, application, sharedModel)
        editorFichaViewModel =
            ViewModelProvider(this, viewModelFactory).get(EditorFichaViewModel::class.java)
        binding.viewModel = editorFichaViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.inmuebleID == -1) {
            editorFichaViewModel.inmueble = null
        } else {
            editorFichaViewModel.inmueble =
                datasource.inmuebleDAO().findById(args.inmuebleID.toString())
        }
        if (editorFichaViewModel.inmueble == null) {
            (activity as AppCompatActivity).supportActionBar?.title = "Crear Inmueble"
            binding.radioVenta.isChecked = true
            binding.radioAtico.isChecked = true
            binding.radioNuevo.isChecked = true
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Editar Inmueble"
            rellenarCamposEditables()
        }
        imagesRecyclerView = binding.imagesRecyclerView
        binding.anadirImagen.setOnClickListener {
            selectImages()
        }

        editorFichaViewModel.imagesList.observe(viewLifecycleOwner, {
            binding.imagesRecyclerView.adapter = ImageAdapter(
                requireContext(),
                it,
                editorFichaViewModel
            )
        })

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

        binding.descartar.setOnClickListener {
            val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavHome()
            findNavController().navigate(action)
        }

        binding.guardarInmueble.setOnClickListener {
            verificarDatos()
        }
    }

    private fun rellenarCamposEditables() {
        val inmueble = editorFichaViewModel.inmueble!!
        dniPropietario = inmueble.dniPropietario
        direccion = inmueble.direccion!!
        nuevoDesarrollo = inmueble.nuevoDesarrollo!!
        miniatura = inmueble.miniatura
        URLminiatura = ""
        altura = inmueble.altura!!
        precio = inmueble.precio!!
        tipoDeInmueble = inmueble.tipoDeInmueble!!
        operacion = inmueble.operacion!!
        tamano = inmueble.tamano!!
        exterior = inmueble.exterior!!
        habitaciones = inmueble.habitaciones!!
        banos = inmueble.banos!!
        provinciaInmueble = inmueble.provincia!!
        municipioInmueble = inmueble.municipio!!
        barrio = inmueble.barrio!!
        pais = inmueble.pais!!
        latitud = inmueble.latitud!!
        longitud = inmueble.longitud!!
        estadoInmueble = inmueble.estado!!
        tieneAscensor = inmueble.tieneAscensor!!
        precioPorMetro = inmueble.precioPorMetro!!
        titulo = inmueble.titulo!!
        subtitulo = inmueble.subtitulo!!
        descripcion = inmueble.descripcion!!

        when (operacion) {
            Constantes.VENTA -> binding.radioVenta.isChecked = true
            Constantes.ALQUILER -> binding.radioAlquiler.isChecked = true
            Constantes.INTERCAMBIO_VIVIENDA -> binding.radioIntercambio.isChecked = true
        }

        when (tipoDeInmueble) {
            Constantes.ATICO -> binding.radioAtico.isChecked = true
            Constantes.CASA_CHALET -> binding.radioCasa.isChecked = true
            Constantes.HABITACION -> binding.radioHabitacion.isChecked = true
            Constantes.PISO -> binding.radioPiso.isChecked = true
        }

        when (estadoInmueble) {
            Constantes.NUEVA_CONSTRUCCION -> binding.radioNuevo.isChecked = true
            Constantes.BUEN_ESTADO -> binding.radioBuenEstado.isChecked = true
            Constantes.REFORMAR -> binding.radioReformar.isChecked = true
        }

        binding.editDireccion.setText(inmueble.direccion)
        binding.editPrecio.setText(inmueble.precio!!.toString())
        binding.editSuperficie.setText(inmueble.tamano!!.toString())
        binding.editHabitaciones.setText(inmueble.habitaciones!!.toString())
        binding.editBanos.setText(inmueble.banos!!.toString())
        binding.editPlanta.setText(inmueble.altura!!.toString())
        binding.hasAscensor.isChecked = inmueble.tieneAscensor!!
        binding.editTitulo.setText(inmueble.titulo)
        binding.editCP.setText(inmueble.codigoPostal.toString())
        binding.editDescripcion.setText(inmueble.descripcion)
        editorFichaViewModel.imagesList.value =
            datasource.fotoDAO().getAllFromInmuebleID(inmueble.inmuebleId).toMutableList()
    }

    private fun setError(view: TextInputEditText, error: String) {
        view.error = error
    }

    private fun verificarDatos() {
        var hasError = false;
        // TODO --> Aplicar extract method a los distintos if

        nuevoDesarrollo = false
        URLminiatura = ""
        dniPropietario = sharedModel.usuario!!.dni
        exterior = false
        tipoDeInmueble = tipoInmueble()
        operacion = tipoOperacion()
        estadoInmueble = getEstado() // OK
        tieneAscensor = binding.hasAscensor.isChecked
        subtitulo = "" // OK

        miniatura =
            if (editorFichaViewModel.imagesList.value!!.isEmpty()) null else editorFichaViewModel.imagesList.value!![0].imagen  // OK

        if (binding.editPlanta.text.toString() == "" || binding.editPlanta.text.toString()
                .toInt() < 0
        ) {
            hasError = true
            setError(
                binding.editPlanta,
                "Este campo no puede estar vacio. Numeros mayores o iguales a 0"
            )
        } else {
            altura = binding.editPlanta.text.toString().toInt()
        }

        if (binding.editPrecio.text.toString() == "" || binding.editPrecio.text.toString()
                .toInt() < 0
        ) {
            hasError = true
            setError(
                binding.editPrecio,
                "Este campo no puede estar vacio. Numeros mayores o iguales a 0"
            )
        } else {
            precio = binding.editPrecio.text.toString().toInt()
        }

        if (binding.editSuperficie.text.toString() == "" || binding.editSuperficie.text.toString()
                .toInt() <= 0
        ) {
            hasError = true
            setError(binding.editSuperficie, "Este campo no puede estar vacio. Numeros mayores a 0")
        } else {
            tamano = binding.editSuperficie.text.toString().toInt()
            precioPorMetro = precio / tamano // OK
        }

        if (binding.editHabitaciones.text.toString() == "" || binding.editHabitaciones.text.toString()
                .toInt() <= 0
        ) {
            hasError = true
            setError(
                binding.editHabitaciones,
                "Este campo no puede estar vacio. Numeros mayores a 0"
            )
        } else {
            habitaciones = binding.editHabitaciones.text.toString().toInt()
        }

        if (binding.editBanos.text.toString() == "" || binding.editBanos.text.toString()
                .toInt() <= 0
        ) {
            hasError = true
            setError(binding.editBanos, "Este campo no puede estar vacio. Numeros mayores a 0")
        } else {
            banos = binding.editBanos.text.toString().toInt()
        }

        if (binding.editDireccion.text.toString() == "") {
            hasError = true
            setError(binding.editDireccion, "Este campo no puede estar vacio.")
        } else {
            direccion = binding.editDireccion.text.toString()
            provinciaInmueble = getProvincia()
            municipioInmueble = getMunicipio()
            barrio = getMunicipio()
            pais = "España" // OK
            latitud = getLatitude()
            longitud = getLongitude()
        }

        if (binding.editCP.text.toString() == "") {
            hasError = true
            setError(binding.editCP, "Este campo no puede estar vacio.")
        } else {
            codigoPostalInm = binding.editCP.text.toString().toInt()
        }

        if (binding.editTitulo.text.toString() == "") {
            hasError = true
            setError(binding.editTitulo, "Este campo no puede estar vacio.")

        } else {
            titulo = binding.editTitulo.text.toString()
        }

        if (binding.editDescripcion.text.toString() == "") {
            hasError = true
            binding.editDescripcion.error = "Este campo no puede estar vacio."
        } else {
            descripcion = binding.editDescripcion.text.toString()
        }

        if (!hasError) {
            if (editorFichaViewModel.inmueble == null) {
                crearInmueble()
            } else {
                actualizarInmueble()
            }
        }
    }

    private fun actualizarInmueble() {
        val inmueble = editorFichaViewModel.inmueble!!
        inmueble.dniPropietario = dniPropietario
        inmueble.direccion = direccion
        inmueble.nuevoDesarrollo = nuevoDesarrollo
        inmueble.miniatura = miniatura
        inmueble.URLminiatura = URLminiatura
        inmueble.altura = altura
        inmueble.precio = precio
        inmueble.tipoDeInmueble = tipoDeInmueble
        inmueble.operacion = operacion
        inmueble.tamano = tamano
        inmueble.exterior = exterior
        inmueble.habitaciones = habitaciones
        inmueble.banos = banos
        inmueble.provincia = provinciaInmueble
        inmueble.municipio = municipioInmueble
        inmueble.barrio = barrio
        inmueble.pais = pais
        inmueble.latitud = latitud
        inmueble.longitud = longitud
        inmueble.estado = estadoInmueble
        inmueble.tieneAscensor = tieneAscensor
        inmueble.precioPorMetro = precioPorMetro
        inmueble.titulo = titulo
        inmueble.subtitulo = subtitulo
        inmueble.descripcion = descripcion
        inmueble.codigoPostal = codigoPostalInm

        datasource.inmuebleDAO().delete(inmueble)
        datasource.inmuebleDAO().insertAll(inmueble)
        sharedModel.inmuebles.value = datasource.inmuebleDAO().getAll().toMutableList()

        editorFichaViewModel.imagesList.value!!.forEach {
            if (datasource.fotoDAO().findById(it.fotoId.toString()) != null) {
                datasource.fotoDAO().delete(it)
            }
            datasource.fotoDAO().insertAll(it)
        }

        val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavHome()
        sharedModel.updateInmuebles()
        sharedModel.inmuebles.value = datasource.inmuebleDAO().getAll().toMutableList()
        findNavController().navigate(action)
    }

    private fun crearInmueble() {
        val inmueble = Inmueble(
            dniPropietario,
            direccion,
            nuevoDesarrollo,
            miniatura,
            URLminiatura,
            altura,
            precio,
            tipoDeInmueble,
            operacion,
            tamano,
            exterior,
            habitaciones,
            banos,
            provinciaInmueble,
            municipioInmueble,
            barrio,
            pais,
            latitud,
            longitud,
            estadoInmueble,
            tieneAscensor,
            precioPorMetro,
            titulo,
            subtitulo,
            descripcion,
            codigoPostalInm
        )

        datasource.inmuebleDAO().insertAll(inmueble)
        if (editorFichaViewModel.inmueble == null) {
            editorFichaViewModel.inmuebleID = datasource.inmuebleDAO().getAll().last().inmuebleId
        }
        editorFichaViewModel.imagesList.value?.forEach {
            datasource.fotoDAO().insertAll(Foto(editorFichaViewModel.inmuebleID!!, it.imagen))
        }

        val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavHome()
        sharedModel.updateInmuebles()
        sharedModel.inmuebles.value = datasource.inmuebleDAO().getAll().toMutableList()
        findNavController().navigate(action)
    }

    private var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            setDireccion(location.latitude, location.longitude)
            locationManager.removeUpdates(this)
        }
    }

    private fun getLatitude(): Double {
        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocationName(
            binding.editDireccion.text.toString() + binding.editCP.text.toString(),
            1
        )
        return if (result.isEmpty()) 0.0 else result.get(0).latitude
    }

    private fun getLongitude(): Double {
        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocationName(
            binding.editDireccion.text.toString() + binding.editCP.text.toString(),
            1
        )
        return if (result.isEmpty()) 0.0 else result.get(0).longitude
    }

    private fun getMunicipio(): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocationName(
            binding.editDireccion.text.toString() + binding.editCP.text.toString(),
            1
        )
        return if (result.isEmpty()) "" else result.get(0).locality
    }

    private fun getProvincia(): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val dir = geocoder.getFromLocationName(
            binding.editDireccion.text.toString() + binding.editCP.text.toString(),
            1
        )
        return if (!dir.isEmpty()) dir.get(0).adminArea else ""
    }

    private fun tipoOperacion(): String {
        val selectedID = binding.radioGroupOperacion.checkedRadioButtonId
        return when (binding.radioGroupOperacion.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.venta) -> Constantes.VENTA
            getString(R.string.alquiler) -> Constantes.ALQUILER
            getString(R.string.intercambio_de_viviendas) -> Constantes.INTERCAMBIO_VIVIENDA
            else -> ""
        }
    }

    private fun tipoInmueble(): String {
        val selectedID = binding.radioGroupTipoInmueble.checkedRadioButtonId
        return when (binding.radioGroupTipoInmueble.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.tico) -> Constantes.ATICO
            getString(R.string.casa_chalet) -> Constantes.CASA_CHALET
            getString(R.string.habitaci_n) -> Constantes.HABITACION
            getString(R.string.piso) -> Constantes.PISO
            else -> ""
        }
    }

    private fun getEstado(): String {
        val selectedID = binding.radioGroupEstado.checkedRadioButtonId
        return when (binding.radioGroupEstado.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.obra_nueva) -> Constantes.NUEVA_CONSTRUCCION
            getString(R.string.buen_estado) -> Constantes.BUEN_ESTADO
            getString(R.string.a_reformar) -> Constantes.REFORMAR
            else -> ""
        }
    }

    private fun setDireccion(latitud: Double, longitud: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val direccion: List<Address> = geocoder.getFromLocation(
            latitud,
            longitud,
            1
        )
        binding.editDireccion.setText(direccion[0].getAddressLine(0).toString())
        binding.editCP.setText(direccion[0].postalCode.toString())
    }

    private fun selectImages() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE && data?.clipData != null) {
            processImageUris(data.clipData!!)
        }
    }

    private fun processImageUris(clipData: ClipData) {
        for (i in 0 until clipData.itemCount) {
            processImages(clipData.getItemAt(i).uri)
        }
    }

    private fun processImages(imageUri: Uri) {
        val inputStream = context?.contentResolver?.openInputStream(imageUri)
        var bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 300, 300, true)
        editorFichaViewModel.addImageToList(Foto(editorFichaViewModel.inmuebleID!!, bitmap))
    }


}