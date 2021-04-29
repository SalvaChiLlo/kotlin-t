package com.kotlin_t.trobify.presentacion.registrar

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.persistencia.UsuarioDAO
import com.kotlin_t.trobify.presentacion.SharedViewModel



class RegistrarseViewModel(val database: AppDatabase,
                           application: Application,
                           val model: SharedViewModel,
                           val binding: FragmentRegistrarseBinding,
                           val fragment: RegistrarseFragment
) : AndroidViewModel(application) {

    private var _usuario = MutableLiveData<String>()
    val usuario: LiveData<String>
        get() = _usuario

    private var _contrasena = MutableLiveData<String>()
    val contrasena: LiveData<String>
        get() = _contrasena

    private var _repetirContrasena = MutableLiveData<String>()
    val repetirContrasena: LiveData<String>
        get() = _repetirContrasena

    private var _dni = MutableLiveData<String>()
    val dni: LiveData<String>
        get() = _dni

    private var _nombre = MutableLiveData<String>()
    val nombre: LiveData<String>
        get() = _nombre

    private var _apellidos = MutableLiveData<String>()
    val apellidos: LiveData<String>
        get() = _apellidos

    private var _telefono = MutableLiveData<String>()
    val telefono: LiveData<String>
        get() = _telefono


    var avatar: Bitmap? = null


    private var _iban = MutableLiveData<String>()
    val iban: LiveData<String>
        get() = _iban
    init{
        _usuario.value = binding.inputUsuario.text.toString()
        _contrasena.value = binding.inputContrasena.text.toString()
        _repetirContrasena.value = binding.inputRepetirContrasena.text.toString()
        _dni.value = binding.inputDni.text.toString()
        _nombre.value = binding.inputNombre.text.toString()
        _apellidos.value = binding.inputApellidos.text.toString()
        _telefono.value = binding.inputTelefono.text.toString()
        _iban.value = binding.inputIban.text.toString()


        binding.apply {
            usuario.isErrorEnabled = true
            contrasena.isErrorEnabled = false
            repetirContrasena.isErrorEnabled = false
            dni.isErrorEnabled = false
            nombre.isErrorEnabled = false
            apellidos.isErrorEnabled = false
            telefono.isErrorEnabled = false
            iban.isErrorEnabled = false

        }


    }
    fun usuarioValido(usuario: String): Boolean{

        if(usuario.isEmpty()){
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if(database.usuarioDAO().existsUsername(usuario)){
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.usuarioCogido)
            return false;
        }
        var letra = false
        var numero = false
        var caracterEspecial = false
        for(i in usuario.indices){
            if(Character.isDigit(usuario[i])) numero = true
            else if(Character.isLetter(usuario[i])) letra = true
            else if(!Character.isLetterOrDigit(usuario[i])) caracterEspecial = true

        }
        if(!letra || !numero || !caracterEspecial){
            binding.usuario.isErrorEnabled = true;
            binding.usuario.error = fragment.getString(R.string.usuarioCorrecto)
            return false
        }

        binding.usuario.isErrorEnabled = false;
        _usuario.value = usuario
        return true;
    }

    fun contrasenaValida(contrasena: String): Boolean{
        if(contrasena.isEmpty()){
            binding.contrasena.isErrorEnabled = true;
            binding.contrasena.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if(contrasena.length < 8){
            binding.contrasena.isErrorEnabled = true;
            binding.contrasena.error = fragment.getString(R.string.contrasenaCorta)
            return false;
        }
        binding.usuario.isErrorEnabled = false;
        _contrasena.value = contrasena
        return true;
    }

    fun coincidenContrasenas(contrasenaRepetida: String): Boolean{
        if(contrasenaRepetida.isEmpty()){
            binding.repetirContrasena.isErrorEnabled = true;
            binding.repetirContrasena.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if(contrasenaRepetida != contrasena.value.toString()){
            binding.repetirContrasena.isErrorEnabled = true;
            binding.repetirContrasena.error = fragment.getString(R.string.contrasenaNoCoinciden)
            return false;
        }
        binding.repetirContrasena.isErrorEnabled = false
        _repetirContrasena.value = contrasenaRepetida
        return true;
    }

    fun dniCorrecto(dni: String): Boolean{
        if(dni.isEmpty()){
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if(database.usuarioDAO().existsId(dni)){
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.dniCogido)
            return false;
        }
        var esNumero = true
       for(i in dni.indices - 1){
           if(!Character.isDigit(dni[i])){
               esNumero = false;
               break
           }
       }
        if(dni.length != 9 || !esNumero || !Character.isLetter(dni[dni.length]) ){
            binding.dni.isErrorEnabled = true;
            binding.dni.error = fragment.getString(R.string.dniIncorrecto)
            return false;
        }
        binding.dni.isErrorEnabled = false
        _dni.value = dni
        return true;
    }
    fun nombreCorrecto(nombre: String): Boolean{
        if(nombre.isEmpty()){
            binding.nombre.isErrorEnabled = true;
            binding.nombre.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        binding.nombre.isErrorEnabled = false
        _nombre.value = nombre
        return true;
    }

    fun apellidosCorrectos(apellidos: String): Boolean{
        if(apellidos.isEmpty()){
            binding.apellidos.isErrorEnabled = true;
            binding.apellidos.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        binding.apellidos.isErrorEnabled = false
        _apellidos.value = apellidos
        return true;
    }

    fun telefonoCorrecto(telefono: String): Boolean{
        if(telefono.isEmpty()){
            binding.telefono.isErrorEnabled = true;
            binding.telefono.error = fragment.getString(R.string.completaCampo)
            return false;
        }

        var esValido = true;
        for(i in telefono.indices){
            if(!Character.isDigit(telefono[i])){
                esValido = false;
                break
            }
        }
        if(telefono.length != 9 || !esValido){
            binding.telefono.isErrorEnabled = true;
            binding.telefono.error = fragment.getString(R.string.telefonoIncorrecto)
            return false;
        }
        binding.telefono.isErrorEnabled = false
        _telefono.value = telefono
        return true;
    }

    fun ibanCorrecto(iban: String): Boolean{
        if(iban.isEmpty()){
            binding.iban.isErrorEnabled = true
            binding.iban.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        var esValido = true;
        for(i in 2..iban.length){
            if(!Character.isDigit(iban[i])){
                esValido = false
                break
            }
        }
        if(iban.length != 24 || !esValido || !Character.isLetter(iban[0]) || !Character.isLetter(iban[1])){
            binding.iban.isErrorEnabled = true;
            binding.iban.error = fragment.getString(R.string.ibanIncorrecto)
            return false;
        }
        binding.iban.isErrorEnabled = false
        _iban.value = iban
        return true;
    }

    fun registrarse(){
        if(usuario.value?.let { usuarioValido(it) } == true && contrasena.value?.let { contrasenaValida(it)} == true && repetirContrasena.value?.let {coincidenContrasenas(it)} == true &&
                dni.value?.let { dniCorrecto(it) } == true && nombre.value?.let { nombreCorrecto(it) } == true && apellidos.value?.let {apellidosCorrectos(it)} == true &&
                telefono.value?.let { telefonoCorrecto(it) } == true && iban.value?.let { ibanCorrecto(it)} == true){
                    val usuario = Usuario(dni.value!!, usuario.value!!, contrasena.value!!, nombre.value!!, apellidos.value!!, telefono.value!!, iban.value!!, avatar)
                    database.usuarioDAO().insertAll(usuario)
                    model.usuario = usuario
                    fragment.findNavController().navigate(RegistrarseFragmentDirections.actionRegistrarseFragmentToNavHome())

        }


    }


}