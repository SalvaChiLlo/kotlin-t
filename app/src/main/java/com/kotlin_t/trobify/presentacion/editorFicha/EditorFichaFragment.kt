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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.logica.editorFicha.Cuidador
import com.kotlin_t.trobify.logica.editorFicha.EditorFichaViewModel
import com.kotlin_t.trobify.logica.editorFicha.EditorFichaViewModelFactory
import com.kotlin_t.trobify.persistencia.Foto
import java.util.*

class EditorFichaFragment : Fragment() {
    lateinit var binding: FragmentEditorFichaBinding
    lateinit var editorFichaViewModel: EditorFichaViewModel
    lateinit var cuidador: Cuidador
    lateinit var datasource: AppDatabase
    lateinit var sharedModel: com.kotlin_t.trobify.logica.ContextClass
    lateinit var imagesRecyclerView: RecyclerView
    lateinit var locationManager: LocationManager
    lateinit var buttonsOperation : RadioGroup
    lateinit var buttonsTipo : RadioGroup
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
        sharedModel = ViewModelProvider(requireActivity()).get(ContextClass::class.java)

        val viewModelFactory = EditorFichaViewModelFactory(datasource, application, sharedModel, binding, requireContext())
        editorFichaViewModel =
            ViewModelProvider(this, viewModelFactory).get(EditorFichaViewModel::class.java)
        binding.viewModel = editorFichaViewModel
        binding.lifecycleOwner = this
        cuidador = Cuidador(editorFichaViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagesRecyclerView = binding.imagesRecyclerView
        buttonsOperation = view.findViewById(R.id.radioGroupOperacion)
        buttonsTipo = view.findViewById(R.id.radioGroupTipoInmueble)
        setEditorOrCreator()
        setAñadirImagenesAction()
        setDescartarAction()
        setGuardarInmuebleAction()
        setRecyclerViewObserver()
        setUbicacionButtonAction()
        setUndoAction()
        setListeners()
    }

    private fun setUndoAction() {
        binding.deshacerAccionButton.setOnClickListener {
            cuidador.undo()
            cuidador.undo()
        }
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
                val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToMisInmueblesFragment()
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
            for (i in 0 until buttonsOperation.getChildCount()) {
                buttonsOperation.getChildAt(i).setEnabled(false)
            }
            for (i in 0 until buttonsTipo.getChildCount()) {
                buttonsTipo.getChildAt(i).setEnabled(false)
            }
        }
    }

    private fun setBinIcon() {
        binding.descartar.setImageResource(R.drawable.ic_baseline_delete_24)
    }

    private fun setRecyclerViewObserver() {
        imagesRecyclerView.adapter = ImageAdapter(requireContext(), editorFichaViewModel.imagesList.value!!.toList(),editorFichaViewModel)
        editorFichaViewModel.imagesList.observe(viewLifecycleOwner, {
            imagesRecyclerView.adapter = ImageAdapter(requireContext(), it.toList(),editorFichaViewModel)
        })
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

        editorFichaViewModel.imagesList.value?.addAll(
            datasource.fotoDAO().getAllFromInmuebleID(inmueble.inmuebleId!!)
        )
        editorFichaViewModel.imagesList.value = editorFichaViewModel.imagesList.value

        editorFichaViewModel.imagesList.observe(viewLifecycleOwner, {
            imagesRecyclerView.adapter = ImageAdapter(requireContext(), it.toList(),editorFichaViewModel)
        })

    }

    private fun terminarEdicionCreacion() {
        val action = EditorFichaFragmentDirections.actionEditorFichaFragmentToMisInmueblesFragment()
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
        var wasEmpty = editorFichaViewModel.imagesList.value?.isEmpty() == true
        editorFichaViewModel.imagesList.value?.add(Foto(editorFichaViewModel.inmuebleID!!, bitmap, false))
        editorFichaViewModel.imagesList.value = editorFichaViewModel.imagesList.value

        if(wasEmpty) {
            editorFichaViewModel.imagesList.value?.get(0)?.main = true
        }

        editorFichaViewModel.imagesList.value = editorFichaViewModel.imagesList.value
        imagesRecyclerView.adapter = ImageAdapter(requireContext(), editorFichaViewModel.imagesList.value!!.toList(),editorFichaViewModel)
    }

    private fun setListeners() {

        // Array of TextInputEditText
        val textInputEditTextList: List<EditText> = listOf(
            binding.editPrecio, binding.editSuperficie, binding.editHabitaciones, binding.editBanos,
            binding.editTitulo, binding.editDireccion, binding.editCP, binding.editDescripcion
        )

        //Timers
        var timer = Timer()
        val delay : Long = 1000


        //Asign Listener to Each EditText
        textInputEditTextList.forEach{it.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            cuidador.createSnapshot()
                        }
                    },
                    delay
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })}

        //RadioGroup Listeners
        val radioGroupList: List<RadioGroup> = listOf(
            binding.radioGroupOperacion, binding.radioGroupTipoInmueble, binding.radioGroupEstado
        )
        radioGroupList.forEach{it.setOnCheckedChangeListener { group, checkedId ->  cuidador.createSnapshot()}}

        //Ascensor CheckBox Listener
        binding.hasAscensor.setOnCheckedChangeListener { buttonView, isChecked ->  cuidador.createSnapshot()}
    }

}