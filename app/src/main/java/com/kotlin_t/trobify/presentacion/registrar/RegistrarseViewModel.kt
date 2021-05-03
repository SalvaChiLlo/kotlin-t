package com.kotlin_t.trobify.presentacion.registrar


import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.logica.SesionActual
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.presentacion.SharedViewModel
import java.time.LocalDateTime


class RegistrarseViewModel(
    val database: AppDatabase,
    application: Application,
    val model: SharedViewModel,
    val binding: FragmentRegistrarseBinding,
    val fragment: RegistrarseFragment
) : AndroidViewModel(application) {

    private var _usuario = binding.inputUsuario.text.toString()
    val usuario: String
        get() = _usuario

    private var _contrasena = binding.inputContrasena.text.toString()
    val contrasena: String
        get() = _contrasena

    private var _repetirContrasena = binding.inputRepetirContrasena.text.toString()
    val repetirContrasena: String
        get() = _repetirContrasena

    private var _dni = binding.inputDni.text.toString()
    val dni: String
        get() = _dni

    private var _nombre = binding.inputNombre.text.toString()
    val nombre: String
        get() = _nombre

    private var _apellidos = binding.inputApellidos.text.toString()
    val apellidos: String
        get() = _apellidos

    private var _telefono = binding.inputTelefono.text.toString()
    val telefono: String
        get() = _telefono


    var avatar: Bitmap? = null


    private var _iban = binding.inputIban.text.toString()
    val iban: String
        get() = _iban

    init {

        binding.apply {
            usuario.isErrorEnabled = false
            contrasena.isErrorEnabled = false
            repetirContrasena.isErrorEnabled = false
            dni.isErrorEnabled = false
            nombre.isErrorEnabled = false
            apellidos.isErrorEnabled = false
            telefono.isErrorEnabled = false
            iban.isErrorEnabled = false

        }


    }

    fun usuarioValido(usuario: String): Boolean {

        if (usuario.isEmpty()) {
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if(usuario.length < 5){
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.usuarioCorto)
            return false;
        }
        if (database.usuarioDAO().existsUsername(usuario)) {
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.usuarioCogido)
            return false;
        }
        for (i in usuario.indices) {
            if (usuario[i].toInt() == 32){
                binding.usuario.isErrorEnabled = true;
                binding.usuario.error = fragment.getString(R.string.usuarioIncorrecto)
                return false
            }


        }
        binding.usuario.isErrorEnabled = false;
        _usuario = usuario
        return true;
    }

    fun contrasenaValida(contrasena: String): Boolean {
        if (contrasena.isEmpty()) {
            binding.contrasena.isErrorEnabled = true;
            binding.contrasena.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (contrasena.length < 8) {
            binding.contrasena.isErrorEnabled = true;
            binding.contrasena.error = fragment.getString(R.string.contrasenaCorta)
            return false;
        }
        binding.contrasena.isErrorEnabled = false;
        _contrasena = contrasena
        return true;
    }

    fun coincidenContrasenas(contrasenaRepetida: String): Boolean {
        if (contrasenaRepetida.isEmpty()) {
            binding.repetirContrasena.isErrorEnabled = true;
            binding.repetirContrasena.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (contrasenaRepetida != contrasena) {
            binding.repetirContrasena.isErrorEnabled = true;
            binding.repetirContrasena.error = fragment.getString(R.string.contrasenaNoCoinciden)
            return false;
        }
        binding.repetirContrasena.isErrorEnabled = false
        _repetirContrasena = contrasenaRepetida
        return true;
    }

    fun dniCorrecto(dni: String): Boolean {
        if (dni.isEmpty()) {
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (database.usuarioDAO().existsId(dni)) {
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.dniCogido)
            return false;
        }
        var esValido = true
        for (i in dni.indices) {
            if(i == 8) {
                if (!Character.isLetter(dni[i])) {
                    esValido = false;
                    break
                }
            }
            else if (!Character.isDigit(dni[i])) {
                esValido = false;
                break
            }
        }
        if (dni.length != 9 || !esValido) {
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.dniIncorrecto)
            return false;
        }
        binding.dni.isErrorEnabled = false
        _dni = dni
        return true;
    }

    fun nombreCorrecto(nombre: String): Boolean {
        if (nombre.isEmpty()) {
            binding.nombre.isErrorEnabled = true;
            binding.nombre.error = fragment.getString(R.string.completaCampo)
            return false;
        }

        for(i in nombre.indices){
            if(!Character.isLetter(nombre[i])){
                binding.nombre.isErrorEnabled = true;
                binding.nombre.error = fragment.getString(R.string.nombreInvalido)
                return false;
            }
        }
        binding.nombre.isErrorEnabled = false
        _nombre = nombre
        return true;
    }

    fun apellidosCorrectos(apellidos: String): Boolean {
        if (apellidos.isEmpty()) {
            binding.apellidos.isErrorEnabled = true;
            binding.apellidos.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        for(i in apellidos.indices){
            if(!Character.isLetter(apellidos[i])){
                binding.nombre.isErrorEnabled = true;
                binding.nombre.error = fragment.getString(R.string.apellidosInvalidos)
                return false;
            }
        }
        binding.apellidos.isErrorEnabled = false
        _apellidos = apellidos
        return true;
    }

    fun telefonoCorrecto(telefono: String): Boolean {
        if (telefono.isEmpty()) {
            binding.telefono.isErrorEnabled = true;
            binding.telefono.error = fragment.getString(R.string.completaCampo)
            return false;
        }

        var esValido = true;
        for (i in telefono.indices) {
            if (!Character.isDigit(telefono[i])) {
                esValido = false;
                break
            }
        }
        if (telefono.length != 9 || !esValido) {
            binding.telefono.isErrorEnabled = true;
            binding.telefono.error = fragment.getString(R.string.telefonoIncorrecto)
            return false;
        }
        binding.telefono.isErrorEnabled = false
        _telefono = telefono
        return true;
    }

    fun ibanCorrecto(iban: String): Boolean {
        if (iban.isEmpty()) {
            binding.iban.isErrorEnabled = true
            binding.iban.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        var esValido = true;
        for (i in iban.indices) {
            if (i == 0 || i == 1) {
                if (!Character.isLetter(iban[i])) {
                    esValido = false
                    break
                }
            } else {
                if (!Character.isDigit(iban[i])) {
                    esValido = false
                    break
                }
            }
        }
        if (iban.length != 24 || !esValido) {
            binding.iban.isErrorEnabled = true;
            binding.iban.error = fragment.getString(R.string.ibanIncorrecto)
            return false;
        }
        binding.iban.isErrorEnabled = false
        _iban = iban
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registrarse() {
        if (usuarioValido(usuario) && contrasenaValida(contrasena) && coincidenContrasenas(repetirContrasena) && dniCorrecto(dni) && nombreCorrecto(nombre) &&
            apellidosCorrectos(apellidos) && telefonoCorrecto(telefono)
        ) {
            val user = Usuario(dni, usuario, contrasena, nombre, apellidos, telefono, iban, avatar)
            database.usuarioDAO().insertAll(user)
            model.updateCurrentUser(user)
            model.insertarSesionActual(usuario)
            fragment.findNavController().navigate(RegistrarseFragmentDirections.actionRegistrarseFragmentToNavHome())
            Toast.makeText(fragment.context, "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
        }
        else{
            recuerdoCompletarCampos()
        }
    }

    fun recuerdoCompletarCampos(){
        val idString = R.string.completaCampo

        if(binding.inputUsuario.text.toString().isEmpty()){
            binding.usuario.isErrorEnabled = true
            binding.usuario.error = fragment.getString(idString)
        }
        if(binding.inputContrasena.text.toString().isEmpty()){
            binding.contrasena.isErrorEnabled = true
            binding.contrasena.error = fragment.getString(idString)
        }
        if(binding.inputRepetirContrasena.text.toString().isEmpty()){
            binding.repetirContrasena.isErrorEnabled = true
            binding.repetirContrasena.error = fragment.getString(idString)
        }
        if(binding.inputDni.text.toString().isEmpty()){
            binding.dni.isErrorEnabled = true
            binding.dni.error = fragment.getString(idString)
        }
        if(binding.inputNombre.text.toString().isEmpty()){
            binding.nombre.isErrorEnabled = true
            binding.nombre.error = fragment.getString(idString)
        }
        if(binding.inputApellidos.text.toString().isEmpty()){
            binding.apellidos.isErrorEnabled = true
            binding.apellidos.error = fragment.getString(idString)
        }
        if(binding.inputTelefono.text.toString().isEmpty()){
            binding.telefono.isErrorEnabled = true
            binding.telefono.error = fragment.getString(idString)
        }
    }


}