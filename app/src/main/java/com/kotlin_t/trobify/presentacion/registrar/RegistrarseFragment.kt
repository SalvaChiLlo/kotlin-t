package com.kotlin_t.trobify.presentacion.registrar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.home.HomeViewModel
import com.kotlin_t.trobify.presentacion.home.HomeViewModelFactory

class RegistrarseFragment : Fragment() {
    private lateinit var binding: FragmentRegistrarseBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var registrarseViewModel: RegistrarseViewModel
    private val REQUEST_CODE = 100;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registrarse, container, false);
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = RegistrarseViewModelFactory(datasource, application, sharedViewModel, binding, this)
        registrarseViewModel = ViewModelProvider(this, viewModelFactory).get(RegistrarseViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = registrarseViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        registrarseViewModel.usuario.observe(viewLifecycleOwner, {
            nuevoUsuario ->
                registrarseViewModel.usuarioValido(nuevoUsuario)
        })
        registrarseViewModel.contrasena.observe(viewLifecycleOwner, {
            nuevaContrsena ->
                registrarseViewModel.contrasenaValida(nuevaContrsena)
        })

        registrarseViewModel.repetirContrasena.observe(viewLifecycleOwner, {
                nuevaContrsena ->
            registrarseViewModel.coincidenContrasenas(nuevaContrsena)
        })

        registrarseViewModel.dni.observe(viewLifecycleOwner, {
                nuevoDni ->
            registrarseViewModel.dniCorrecto(nuevoDni)
        })

        registrarseViewModel.nombre.observe(viewLifecycleOwner, {
                nuevoNombre ->
            registrarseViewModel.nombreCorrecto(nuevoNombre)
        })

        registrarseViewModel.apellidos.observe(viewLifecycleOwner, {
                nuevosApellidos ->
            registrarseViewModel.apellidosCorrectos(nuevosApellidos)
        })

        registrarseViewModel.telefono.observe(viewLifecycleOwner, {
                nuevoTelefono ->
            registrarseViewModel.telefonoCorrecto(nuevoTelefono)
        })

        registrarseViewModel.iban.observe(viewLifecycleOwner, {
            nuevoIban ->
                registrarseViewModel.ibanCorrecto(nuevoIban)
        })

        binding.nuevaImagen.setOnClickListener{
            nuevaImagen()
            binding.eliminarImagen.visibility = View.VISIBLE
        }
        binding.eliminarImagen.setOnClickListener{
            eliminarImagen()
            binding.eliminarImagen.visibility = View.GONE
        }
        registrarseViewModel.usuario.value?.let { Log.d("usuario", it) }
    }
    private fun nuevaImagen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            if (data != null) {
                data.data?.let { procesarImagen(it) }
                binding.imagen.setImageURI(data.data)
            }

        }


    }
    private fun procesarImagen(imageUri: Uri) {
        val inputStream = context?.contentResolver?.openInputStream(imageUri)
        var bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 300, 300, true)
        registrarseViewModel.avatar = bitmap
    }

    private fun eliminarImagen(){
        binding.imagen.setImageResource(R.drawable.anonymous_user)
        registrarseViewModel.avatar = null
    }

}