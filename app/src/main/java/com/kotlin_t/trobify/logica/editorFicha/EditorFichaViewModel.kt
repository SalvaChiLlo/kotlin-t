package com.kotlin_t.trobify.logica.editorFicha

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.persistencia.Foto
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.presentacion.editorFicha.EditorFichaFragment
import java.util.*

class EditorFichaViewModel(
    val database: AppDatabase,
    application: Application,
    val sharedViewModel: com.kotlin_t.trobify.logica.ContextClass,
    val binding: FragmentEditorFichaBinding,
    val context: Context

) : AndroidViewModel(application) {
    var inmueble: Inmueble? = null
    var inmuebleID: Int? = null
    val imagesList: MutableLiveData<MutableList<Foto>> by lazy {
        MutableLiveData<MutableList<Foto>>()
    }

    private lateinit var geocoder: Geocoder
    private lateinit var geocoderInfo: List<Address>

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
    private var hasError = false

    init {
        imagesList.value = mutableListOf()
        if (inmueble != null) {
            inmuebleID = inmueble!!.inmuebleId
        } else {
            inmuebleID = database.inmuebleDAO().getAllPublicAndNoPublic().last().inmuebleId + 1
        }

        geocoder = Geocoder(context, Locale.getDefault())
        Log.e("EEEEEEE", inmuebleID.toString())

    }

    fun removeImageFromList(image: Foto) {
        imagesList.value!!.remove(image)
        imagesList.value = imagesList.value!!

        if (database.fotoDAO().findById(image.fotoId.toString()) != null) {
            database.fotoDAO().delete(image)
        }
    }

    fun verificarDatos(): Boolean {
        hasError = false
        nuevoDesarrollo = false
        URLminiatura = ""
        dniPropietario =
            if (sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        exterior = false
        tipoDeInmueble = tipoInmueble()
        operacion = tipoOperacion()
        estadoInmueble = getEstado() // OK
        tieneAscensor = binding.hasAscensor.isChecked
        subtitulo = "" // OK
        miniatura =
            if (imagesList.value?.isEmpty() == true) null else imagesList.value?.filter { it.main }?.get(0)?.imagen  // OK

        hasError = checkBanos(hasError)
        hasError = checkCodigoPostal(hasError)
        hasError = checkDescripcion(hasError)
        hasError = checkDireccion(hasError)
        hasError = checkHabitaciones(hasError)
        hasError = checkPlanta(hasError)
        hasError = checkPrecio(hasError)
        hasError = checkSuperficie(hasError)
        hasError = checkTitulo(hasError)
        return hasError
    }

    private fun isEmpty(cadena: String): Boolean {
        return cadena == ""
    }

    private fun setError(view: TextInputEditText, error: String) {
        view.error = error
    }

    private fun checkDescripcion(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editDescripcion.text.toString())) {
            hasError1 = true
            binding.editDescripcion.error = EditorFichaFragment.NO_VACIO_ERR_MSG
        } else {
            descripcion = binding.editDescripcion.text.toString()
        }
        return hasError1
    }

    private fun checkTitulo(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editTitulo.text.toString())) {
            hasError1 = true
            setError(binding.editTitulo, EditorFichaFragment.NO_VACIO_ERR_MSG)

        } else {
            titulo = binding.editTitulo.text.toString()
        }
        return hasError1
    }

    private fun checkCodigoPostal(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editCP.text.toString())) {
            hasError1 = true
            setError(binding.editCP, EditorFichaFragment.NO_VACIO_ERR_MSG)
        } else {
            codigoPostalInm = binding.editCP.text.toString().toInt()
        }
        return hasError1
    }

    private fun checkDireccion(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editCP.text.toString())) {
            hasError1 = true
            setError(binding.editDireccion, EditorFichaFragment.NO_VACIO_ERR_MSG)
        } else {
            val completeDir = binding.editDireccion.text.toString() + binding.editCP.text.toString()
            geocoderInfo = getInfoFromGeocoder(completeDir)

            direccion = binding.editDireccion.text.toString()
            provinciaInmueble = getProvincia()
            municipioInmueble = getMunicipio()
            barrio = getMunicipio()
            pais = "España" // OK
            latitud = getLatitude()
            longitud = getLongitude()
        }
        return hasError1
    }

    private fun checkBanos(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editBanos.text.toString()) || binding.editBanos.text.toString()
                .toInt() <= 0
        ) {
            hasError1 = true
            setError(
                binding.editBanos,
                "${EditorFichaFragment.NO_VACIO_ERR_MSG} Numeros mayores a 0"
            )
        } else {
            banos = binding.editBanos.text.toString().toInt()
        }
        return hasError1
    }

    private fun checkHabitaciones(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editHabitaciones.text.toString()) || binding.editHabitaciones.text.toString()
                .toInt() <= 0
        ) {
            hasError1 = true
            setError(
                binding.editHabitaciones,
                "${EditorFichaFragment.NO_VACIO_ERR_MSG} Numeros mayores a 0"
            )
        } else {
            habitaciones = binding.editHabitaciones.text.toString().toInt()
        }
        return hasError1
    }

    private fun checkSuperficie(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editSuperficie.text.toString()) || binding.editSuperficie.text.toString()
                .toInt() <= 0
        ) {
            hasError1 = true
            setError(
                binding.editSuperficie,
                "${EditorFichaFragment.NO_VACIO_ERR_MSG} Numeros mayores a 0"
            )
        } else {
            tamano = binding.editSuperficie.text.toString().toInt()
            precioPorMetro = precio / tamano // OK
        }
        return hasError1
    }

    private fun checkPrecio(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editPrecio.text.toString()) || binding.editPrecio.text.toString()
                .toInt() < 0
        ) {
            hasError1 = true
            setError(
                binding.editPrecio,
                "${EditorFichaFragment.NO_VACIO_ERR_MSG} Numeros mayores o iguales a 0"
            )
        } else {
            precio = binding.editPrecio.text.toString().toInt()
        }
        return hasError1
    }

    private fun checkPlanta(hasError: Boolean): Boolean {
        var hasError1 = hasError
        if (isEmpty(binding.editPlanta.text.toString()) || binding.editPlanta.text.toString()
                .toInt() < 0
        ) {
            hasError1 = true
            setError(
                binding.editPlanta,
                "${EditorFichaFragment.NO_VACIO_ERR_MSG} Numeros mayores o iguales a 0"
            )
        } else {
            altura = binding.editPlanta.text.toString().toInt()
        }
        return hasError1
    }

    private fun tipoOperacion(): String {
        val selectedID = binding.radioGroupOperacion.checkedRadioButtonId
        return when (binding.radioGroupOperacion.findViewById<RadioButton>(selectedID).text) {
            context.getString(R.string.venta) -> Constantes.VENTA
            context.getString(R.string.alquiler) -> Constantes.ALQUILER
            context.getString(R.string.intercambio_de_viviendas) -> Constantes.INTERCAMBIO_VIVIENDA
            else -> ""
        }
    }

    private fun tipoInmueble(): String {
        val selectedID = binding.radioGroupTipoInmueble.checkedRadioButtonId
        return when (binding.radioGroupTipoInmueble.findViewById<RadioButton>(selectedID).text) {
            context.getString(R.string.tico) -> Constantes.ATICO
            context.getString(R.string.casa_chalet) -> Constantes.CASA_CHALET
            context.getString(R.string.habitaci_n) -> Constantes.HABITACION
            context.getString(R.string.piso) -> Constantes.PISO
            else -> ""
        }
    }

    private fun getEstado(): String {
        val selectedID = binding.radioGroupEstado.checkedRadioButtonId

        return when (binding.radioGroupEstado.findViewById<RadioButton>(selectedID).text) {
            context.getString(R.string.obra_nueva) -> Constantes.NUEVA_CONSTRUCCION
            context.getString(R.string.buen_estado) -> Constantes.BUEN_ESTADO
            context.getString(R.string.a_reformar) -> Constantes.REFORMAR
            else -> ""
        }
    }

    fun getLatitude(): Double {
        return if (geocoderInfo.isEmpty()) 0.0 else if(geocoderInfo.get(0).latitude != null) geocoderInfo.get(0).latitude else 0.0
    }

    fun getLongitude(): Double {
        return if (geocoderInfo.isEmpty()) 0.0 else if(geocoderInfo.get(0).longitude != null) geocoderInfo.get(0).longitude else 0.0
    }

    fun getMunicipio(): String {
        return if (geocoderInfo.isEmpty()) "" else geocoderInfo.get(0).locality + ""
    }

    fun getProvincia(): String {
        return if (!geocoderInfo.isEmpty()) geocoderInfo.get(0).adminArea+"" else ""
    }

    private fun getInfoFromGeocoder(dir: String): List<Address> {
        return geocoder.getFromLocationName(dir, 1)
    }

    fun guardarInmueble() {
        if (hasError) return
        if (inmueble == null) return crearInmueble()
        else return actualizarInmueble()
    }

    private fun guardarImagenesDelInmueble() {
        imagesList.value?.forEach {
            if (database.fotoDAO().findById(it.fotoId.toString()) != null) {
                database.fotoDAO().delete(it)
            }
            database.fotoDAO().insertAll(it)
        }
        imagesList.value = imagesList.value

    }

    private fun actualizarInmueble() {
        val inmueble = inmueble!!
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

        database.inmuebleDAO().update(inmueble)
        sharedViewModel.inmuebles.value = database.inmuebleDAO().getAll().toMutableList()


        guardarImagenesDelInmueble()
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
            codigoPostalInm,
            false
        )

        database.inmuebleDAO().insertAll(inmueble)
        if (inmueble == null) {
            inmuebleID = database.inmuebleDAO().getAllPublicAndNoPublic().last().inmuebleId
        }

        guardarImagenesDelInmueble()
    }

    fun createMemento() : Memento {
        return Memento(
            binding.radioGroupOperacion.checkedRadioButtonId, // Tipo de Operacion
            binding.radioGroupTipoInmueble.checkedRadioButtonId, // Tipo de Inmueble
            binding.editPrecio.text.toString(), // Precio
            binding.editSuperficie.text.toString(), // Superficie
            binding.editHabitaciones.text.toString(), // Habitaciones
            binding.editBanos.text.toString(), // Baños
            binding.editPlanta.text.toString(), // Planta
            binding.hasAscensor.isChecked, // Ascensor
            binding.radioGroupEstado.checkedRadioButtonId, // Estado del Inmueble
            binding.editTitulo.text.toString(), // Titulo
            binding.editDireccion.text.toString(), // Direccion
            binding.editCP.text.toString(), // Codigo Postal
            binding.editDescripcion.text.toString() // Descripcion
        )
    }

    fun restore(memento: Memento) {
        binding.radioGroupOperacion.check(memento.getOperacion()) // Tipo de Operacion
        binding.radioGroupTipoInmueble.check(memento.getTipoInmueble()) // Tipo de Inmueble
        binding.editPrecio.setText(memento.getPrecio()) // Precio
        binding.editSuperficie.setText(memento.getSuperficie()) // Superficie
        binding.editHabitaciones.setText(memento.getHabitaciones()) // Habitaciones
        binding.editBanos.setText(memento.getBaños()) // Baños
        binding.editPlanta.setText(memento.getPlanta()) // Planta
        binding.hasAscensor.isChecked = memento.getAscensor() // Ascensor
        binding.radioGroupEstado.check(memento.getEstado()) // Estado del Inmueble
        binding.editTitulo.setText(memento.getTitulo()) // Titulo
        binding.editDireccion.setText(memento.getDireccion()) // Direccion
        binding.editCP.setText(memento.getCP()) // Codigo Postal
        binding.editDescripcion.setText(memento.getDescripcion()) // Descripcion
    }



}