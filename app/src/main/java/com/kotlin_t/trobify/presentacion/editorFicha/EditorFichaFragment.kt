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
        editorFichaViewModel.addImageToList(Foto(editorFichaViewModel.inmuebleID!!, bitmap))
    }


}