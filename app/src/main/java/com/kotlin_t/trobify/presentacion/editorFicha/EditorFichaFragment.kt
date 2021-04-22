package com.kotlin_t.trobify.presentacion.editorFicha

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.*
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentBusquedaBinding
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.busqueda.BusquedaAdapter
import com.kotlin_t.trobify.presentacion.busqueda.BusquedaFragmentDirections
import com.kotlin_t.trobify.presentacion.busqueda.BusquedaViewModel
import com.kotlin_t.trobify.presentacion.busqueda.BusquedaViewModelFactory
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModelFactory
import com.kotlin_t.trobify.presentacion.filtrar.FiltrarViewModel
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Busqueda.BusquedaCriteria
import java.util.*

class EditorFichaFragment : Fragment() {
    lateinit var binding: FragmentEditorFichaBinding
    lateinit var editorFichaViewModel: EditorFichaViewModel
    lateinit var datasource: AppDatabase
    lateinit var sharedModel: SharedViewModel
    lateinit var imagesRecyclerView: RecyclerView
    lateinit var locationManager: LocationManager

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
        if (editorFichaViewModel.inmueble == null) {
            (activity as AppCompatActivity).supportActionBar?.title = "Crear Inmueble"
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Editar Inmueble"
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
//            datasource.inmuebleDAO().insertAll(
                val inmueble = Inmueble(
                    sharedModel.usuario!!.dni,
                    binding.editDireccion.text.toString(),
                    false,
                    if (editorFichaViewModel.imagesList.value!!.isEmpty()) null else editorFichaViewModel.imagesList.value!![0],
                    null,
                    binding.editPlanta.text.toString().toInt(),
                    binding.editPrecio.text.toString().toInt(),
                    tipoInmueble(),
                    tipoOperacion(),
                    binding.editSuperficie.text.toString().toInt(),
                    false,
                    binding.editHabitaciones.text.toString().toInt(),
                    binding.editBanos.text.toString().toInt(),
                    getProvincia(),
                    getMunicipio(),
                    getMunicipio(),
                    "España",
                    getLatitude(),
                    getLongitude(),
                    getEstado(),
                    binding.hasAscensor.isChecked,
                    binding.editPrecio.text.toString()
                        .toInt() / binding.editSuperficie.text.toString().toInt(),
                    binding.editTitulo.text.toString(),
                    "",
                    binding.editDescripcion.toString()
                )
//            )
            datasource.inmuebleDAO().insertAll(inmueble)
            if(editorFichaViewModel.inmueble == null) {
                editorFichaViewModel.inmuebleID = datasource.inmuebleDAO().getAll().last().inmuebleId
            }
            editorFichaViewModel.imagesList.value?.forEach {
                datasource.fotoDAO().insertAll(Foto(editorFichaViewModel.inmuebleID!!, it))
            }

            val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavHome()
            sharedModel.updateInmuebles()
            sharedModel.inmuebles.value = datasource.inmuebleDAO().getAll().toMutableList()
            findNavController().navigate(action)
        }
    }

    var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            setDireccion(location.latitude, location.longitude)
            locationManager.removeUpdates(this)
        }
    }


    fun getLatitude(): Double {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocationName("${binding.editCP.text.toString()} spain", 1)
            .get(0).latitude
    }

    fun getLongitude(): Double {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocationName("${binding.editCP.text.toString()} spain", 1)
            .get(0).longitude
    }

    fun getMunicipio(): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocationName("${binding.editCP.text.toString()} spain", 1)
            .get(0).locality
    }

    fun getProvincia(): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val dir = geocoder.getFromLocationName("${binding.editCP.text.toString()} spain", 1)
        return if(!dir.isEmpty()) dir.get(0).adminArea else ""
    }

    fun tipoOperacion(): String {
        val selectedID = binding.radioGroupOperacion.checkedRadioButtonId
        return when (binding.radioGroupOperacion.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.venta) -> Constantes.VENTA
            getString(R.string.alquiler) -> Constantes.ALQUILER
            getString(R.string.intercambio_de_viviendas) -> Constantes.INTERCAMBIO_VIVIENDA
            else -> ""
        }
    }

    fun tipoInmueble(): String {
        val selectedID = binding.radioGroupTipoInmueble.checkedRadioButtonId
        return when (binding.radioGroupTipoInmueble.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.tico) -> Constantes.ATICO
            getString(R.string.casa_chalet) -> Constantes.CASA_CHALET
            getString(R.string.habitaci_n) -> Constantes.HABITACION
            getString(R.string.piso) -> Constantes.PISO
            else -> ""
        }
    }

    fun getEstado(): String {
        val selectedID = binding.radioGroupEstado.checkedRadioButtonId
        return when (binding.radioGroupEstado.findViewById<RadioButton>(selectedID).text) {
            getString(R.string.obra_nueva) -> Constantes.NUEVA_CONSTRUCCION
            getString(R.string.buen_estado) -> Constantes.BUEN_ESTADO
            getString(R.string.a_reformar) -> Constantes.REFORMAR
            else -> ""
        }
    }

    fun setDireccion(latitud: Double, longitud: Double) {
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
        var direccion: List<Address> = geocoder.getFromLocation(
            latitud,
            longitud,
            1
        )
        binding.editDireccion.setText(direccion[0].getAddressLine(0).toString())
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
        editorFichaViewModel.addImageToList(bitmap)
    }


}