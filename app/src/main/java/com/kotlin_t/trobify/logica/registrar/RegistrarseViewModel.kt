package com.kotlin_t.trobify.logica.registrar

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.persistencia.Usuario
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.presentacion.registrar.RegistrarseFragment
import com.kotlin_t.trobify.presentacion.registrar.RegistrarseFragmentDirections

class RegistrarseViewModel(
    val database: AppDatabase,
    application: Application,
    val contextClass: ContextClass,
    val binding: FragmentRegistrarseBinding,
    val fragment: RegistrarseFragment
) : AndroidViewModel(application) {
    var avatar : Bitmap? = binding.imagen.drawable.toBitmap()
    init {

        binding.apply {
            usuario.isErrorEnabled = false
            contrasena.isErrorEnabled = false
            repetirContrasena.isErrorEnabled = false
            dni.isErrorEnabled = false
            nombre.isErrorEnabled = false
            apellidos.isErrorEnabled = false
            telefono.isErrorEnabled = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registrarse() {
        val usuario = binding.inputUsuario.text.toString()
        val contrasena = binding.inputContrasena.text.toString()
        val repetirContrasena = binding.inputRepetirContrasena.text.toString()
        val dni = binding.inputDni.text.toString()
        val nombre = binding.inputNombre.text.toString()
        val apellidos = binding.inputApellidos.text.toString()
        val telefono = binding.inputTelefono.text.toString()
        if (contextClass.usuarioCorrecto(usuario, binding.usuario, fragment) && contextClass.contrasenaCorrecta(contrasena, binding.contrasena, fragment)
            && contextClass.coincidenContrasenas(contrasena, repetirContrasena, binding.repetirContrasena, fragment) && contextClass.dniCorrecto(dni, binding.dni, fragment)
            && contextClass.nombreCorrecto(nombre, binding.nombre, fragment) && contextClass.apellidosCorrectos(apellidos, binding.apellidos, fragment)
            && contextClass.telefonoCorrecto(telefono, binding.telefono, fragment)
        ) {
            val user = Usuario(dni, usuario, contrasena, nombre, apellidos, telefono, avatar)
            database.usuarioDAO().insertAll(user)
            contextClass.updateCurrentUser(user)
            contextClass.insertarSesionActual(usuario)
            fragment.findNavController()
                .navigate(RegistrarseFragmentDirections.actionRegistrarseFragmentToNavHome())
            Toast.makeText(fragment.context, "Usuario registrado correctamente", Toast.LENGTH_LONG)
                .show()
        } else {
            recuerdoCompletarCampos()
        }
    }

    private fun recuerdoCompletarCampos() {
        val idString = R.string.completaCampo

        if (binding.inputUsuario.text.toString().isEmpty()) {
            binding.usuario.isErrorEnabled = true
            binding.usuario.error = fragment.getString(idString)
        }
        if (binding.inputContrasena.text.toString().isEmpty()) {
            binding.contrasena.isErrorEnabled = true
            binding.contrasena.error = fragment.getString(idString)
        }
        if (binding.inputRepetirContrasena.text.toString().isEmpty()) {
            binding.repetirContrasena.isErrorEnabled = true
            binding.repetirContrasena.error = fragment.getString(idString)
        }
        if (binding.inputDni.text.toString().isEmpty()) {
            binding.dni.isErrorEnabled = true
            binding.dni.error = fragment.getString(idString)
        }
        if (binding.inputNombre.text.toString().isEmpty()) {
            binding.nombre.isErrorEnabled = true
            binding.nombre.error = fragment.getString(idString)
        }
        if (binding.inputApellidos.text.toString().isEmpty()) {
            binding.apellidos.isErrorEnabled = true
            binding.apellidos.error = fragment.getString(idString)
        }
        if (binding.inputTelefono.text.toString().isEmpty()) {
            binding.telefono.isErrorEnabled = true
            binding.telefono.error = fragment.getString(idString)
        }
    }


}