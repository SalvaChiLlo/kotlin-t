package com.kotlin_t.trobify.presentacion.editorFicha

import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.editorFicha.ObservableList.RecyclerViewObserver
import java.util.*

class EditorFichaFragment : Fragment() {
    lateinit var binding: FragmentEditorFichaBinding
    lateinit var editorFichaViewModel: EditorFichaViewModel
    lateinit var datasource: AppDatabase
    lateinit var sharedModel: SharedViewModel
    lateinit var imagesRecyclerView: RecyclerView
    lateinit var locationManager: LocationManager
    val args: EditorFichaFragmentArgs by navArgs()

    companion object {
        const val PICK_IMAGE = 1
        const val NO_VACIO_ERR_MSG = "Este campo no puede estar vacio."
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

        val viewModelFactory = EditorFichaViewModelFactory(datasource, application, sharedModel, binding, requireContext())
        editorFichaViewModel =
            ViewModelProvider(this, viewModelFactory).get(EditorFichaViewModel::class.java)
        binding.viewModel = editorFichaViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagesRecyclerView = binding.imagesRecyclerView
        setEditorOrCreator()
        setAñadirImagenesAction()
        setDescartarAction()
        setGuardarInmuebleAction()
        setRecyclerViewObserver()
        setUbicacionButtonAction()
    }

    private fun setGuardarInmuebleAction() {
        binding.guardarInmueble.setOnClickListener {
            if(!editorFichaViewModel.verificarDatos()) {
                editorFichaViewModel.guardarInmueble()
                terminarEdicionCreacion()
            }
        }
    }

    private fun setDescartarAction() {
        binding.descartar.setOnClickListener {
            showDiscardAlert()
        }
    }

    private fun showDiscardAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Seguro que quieres salir?")
            .setMessage("Si sales todos los cambios se perderán.")
            .setNegativeButton("Sí") { dialog, which ->
                val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavMisInmuebles2()
                findNavController().navigate(action)
                datasource.inmuebleDAO().deleteById(editorFichaViewModel.inmuebleID.toString())
                sharedModel.inmuebles.value!!.remove(editorFichaViewModel.inmueble)
                sharedModel.inmuebles.value = datasource.inmuebleDAO().getAll().toMutableList()
            }
            .setPositiveButton("No") { dialog, which ->

            }
            .show()
    }

    private fun setUbicacionButtonAction() {
        binding.miUbicacionButton.setOnClickListener {
            requestLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkPermissions()
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

    private fun checkPermissions() {
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
    }

    private fun setAñadirImagenesAction() {
        binding.anadirImagen.setOnClickListener {
            selectImages()
        }
    }

    private fun setEditorOrCreator() {
        if (args.inmuebleID == -1) {
            editorFichaViewModel.inmueble = null
            (activity as AppCompatActivity).supportActionBar?.title = "Crear Inmueble"
            binding.radioVenta.isChecked = true
            binding.radioAtico.isChecked = true
            binding.radioNuevo.isChecked = true
        } else {
            editorFichaViewModel.inmueble =
                datasource.inmuebleDAO().findById(args.inmuebleID.toString())
            editorFichaViewModel.inmuebleID = editorFichaViewModel.inmueble!!.inmuebleId
            setBinIcon()
            (activity as AppCompatActivity).supportActionBar?.title = "Editar Inmueble"
            rellenarCamposEditables()
        }
    }

    private fun setBinIcon() {
        binding.descartar.setImageResource(R.drawable.ic_baseline_delete_24)
    }

    private fun setRecyclerViewObserver() {
        editorFichaViewModel.imagesList.addObserver(
            RecyclerViewObserver(
                imagesRecyclerView,
                requireContext(),
                editorFichaViewModel
            )
        )
    }

    private fun rellenarCamposEditables() {
        val inmueble = editorFichaViewModel.inmueble!!

        when (inmueble.operacion) {
            Constantes.VENTA -> binding.radioVenta.isChecked = true
            Constantes.ALQUILER -> binding.radioAlquiler.isChecked = true
            Constantes.INTERCAMBIO_VIVIENDA -> binding.radioIntercambio.isChecked = true
        }

        when (inmueble.tipoDeInmueble) {
            Constantes.ATICO -> binding.radioAtico.isChecked = true
            Constantes.CASA_CHALET -> binding.radioCasa.isChecked = true
            Constantes.HABITACION -> binding.radioHabitacion.isChecked = true
            Constantes.PISO -> binding.radioPiso.isChecked = true
        }

        when (inmueble.estado) {
            Constantes.NUEVA_CONSTRUCCION -> binding.radioNuevo.isChecked = true
            Constantes.BUEN_ESTADO -> binding.radioBuenEstado.isChecked = true
            Constantes.REFORMAR -> binding.radioReformar.isChecked = true
        }

        binding.editBanos.setText(inmueble.banos!!.toString())
        binding.editCP.setText(inmueble.codigoPostal.toString())
        binding.editDescripcion.setText(inmueble.descripcion)
        binding.editDireccion.setText(inmueble.direccion)
        binding.editHabitaciones.setText(inmueble.habitaciones!!.toString())
        binding.editPlanta.setText(inmueble.altura!!.toString())
        binding.editPrecio.setText(inmueble.precio!!.toString())
        binding.editSuperficie.setText(inmueble.tamano!!.toString())
        binding.editTitulo.setText(inmueble.titulo)
        binding.hasAscensor.isChecked = inmueble.tieneAscensor!!

        editorFichaViewModel.imagesList.addAllItem(
            datasource.fotoDAO().getAllFromInmuebleID(inmueble.inmuebleId)
        )

        imagesRecyclerView.adapter = ImageAdapter(requireContext(), editorFichaViewModel.imagesList.getValue(),editorFichaViewModel)

        Log.e("WEEEEE", datasource.fotoDAO().getAllFromInmuebleID(inmueble.inmuebleId).map{it.inmuebleId}.toString())
    }

    private fun terminarEdicionCreacion() {
        val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToNavMisInmuebles2()
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
        editorFichaViewModel.imagesList.addItem(Foto(editorFichaViewModel.inmuebleID!!, bitmap))
    }


}