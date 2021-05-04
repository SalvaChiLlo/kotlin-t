package com.kotlin_t.trobify.presentacion.registrar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.logica.registrar.RegistrarseViewModel
import com.kotlin_t.trobify.logica.registrar.RegistrarseViewModelFactory
import com.kotlin_t.trobify.logica.SharedViewModel

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registrarse, container, false);
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory =
            RegistrarseViewModelFactory(datasource, application, sharedViewModel, binding, this)
        registrarseViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegistrarseViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = registrarseViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.inputUsuario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.usuarioValido(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputContrasena.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.contrasenaValida(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputRepetirContrasena.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.coincidenContrasenas(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputDni.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.dniCorrecto(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.nombreCorrecto(p0.toString())
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputApellidos.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.apellidosCorrectos(p0.toString())
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputTelefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.telefonoCorrecto(p0.toString())
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.inputIban.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registrarseViewModel.ibanCorrecto(p0.toString())
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.nuevaImagen.setOnClickListener {
            nuevaImagen()
            binding.eliminarImagen.visibility = View.VISIBLE
        }
        binding.eliminarImagen.setOnClickListener {
            eliminarImagen()
            binding.eliminarImagen.visibility = View.GONE
        }

    }

    private fun nuevaImagen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
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

    private fun eliminarImagen() {
        binding.imagen.setImageResource(R.drawable.anonymous_user)
        registrarseViewModel.avatar = null
    }

}