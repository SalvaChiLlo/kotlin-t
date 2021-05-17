package com.kotlin_t.trobify.logica

import android.app.Application
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.persistencia.SesionActual
import com.kotlin_t.trobify.persistencia.Usuario
import com.kotlin_t.trobify.logica.ordenacion.EstrategiaOrdenacion
import com.kotlin_t.trobify.persistencia.Favorito
import java.time.LocalDateTime

class SharedViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    var database = AppDatabase.getDatabase(application)

    ///////////////////////////////////////////////
    // USUARIO QUE ESTÁ UTILIZANDO LA APLICACIÓN //
    ///////////////////////////////////////////////

    val usuarioActual: MutableLiveData<Usuario> by lazy {
        MutableLiveData<Usuario>()
    }
    val favoritosEliminados = mutableSetOf<Favorito?>()


    val inmuebles = MutableLiveData<MutableList<Inmueble>?>()
    val usuarios = MutableLiveData<MutableList<Usuario>>()
    var estrategiaOrdenacion: EstrategiaOrdenacion? = null
    lateinit var binding: FragmentRegistrarseBinding

    // Variables de filtro
    var operacionesOpciones = MutableLiveData<MutableSet<String>>()
    var tiposOpciones = MutableLiveData<MutableSet<String>>()
    var preciosOpciones = MutableLiveData<IntArray>()
    var habitacionesOpciones = MutableLiveData<MutableSet<Int>>()
    var banosOpciones = MutableLiveData<MutableSet<Int>>()
    var estadoOpciones = MutableLiveData<MutableSet<String>>()
    var plantaOpciones = MutableLiveData<MutableSet<String>>()

    // Variables de busqueda
    var busquedaString = ""

    init {

        if (database.usuarioDAO().getAll().isEmpty()) {
            val usuarioPopulate = Usuario(
                "$2345678E",
                "username",
                "contraseña",
                "nombre",
                "appellido$",
                "999888777",
                null
            )
            database.usuarioDAO().insertAll(usuarioPopulate)

        }

        updateUsuarios()
        updateInmuebles()
        operacionesOpciones.value = mutableSetOf<String>()
        tiposOpciones.value = mutableSetOf<String>()
        preciosOpciones.value = IntArray(2)

        preciosOpciones.value!!.set(0, database.inmuebleDAO().getMinPrecio())
        preciosOpciones.value!!.set(1, database.inmuebleDAO().getMaxPrecio())
        preciosOpciones.value = preciosOpciones.value

        habitacionesOpciones.value = mutableSetOf<Int>()
        banosOpciones.value = mutableSetOf<Int>()
        estadoOpciones.value = mutableSetOf<String>()
        plantaOpciones.value = mutableSetOf<String>()
        usuarioActual.value = null
    }

    fun setInmuebles(inmuebles: List<Inmueble>) {
        this.inmuebles.value = inmuebles.toMutableList()
    }

    fun updateInmuebles() {
        inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
    }


    fun getInmuebleIdFromLatLng(localizacion: LatLng): Int {

        val latitud = localizacion.latitude
        val longitud = localizacion.longitude

        val inmueble: Inmueble? =
            inmuebles.value!!.find { it.latitud == latitud && it.longitud == longitud }

        if (inmueble != null) {
            return inmueble.inmuebleId
        }

        return -1
    }

    fun updateUsuarios() {
        usuarios.value = database.usuarioDAO().getAll().toMutableList()
    }

    fun getUserFromCredentials(username: String, password: String): Usuario? {

        //Log.i("USERNAME", "First username: " + usuarios.value!!.get(0).username)
        //Log.i("PASSWORD", "First password: " + usuarios.value!!.get(0).contrasena)
        //Log.i("VARIABLES", "Provided info Username: " + username + " Contraseña: " + password)

        val res: Usuario? =
            database.usuarioDAO().getAll().find { it.username == username && it.contrasena == password }

        return res

    }

    fun getTipoInmueble(inmuebleId: Int): Int {
        // -1 --> Es Propio
        // 0 --> Es Favorito
        // 1 --> No es Propio ni Favorito
        val dni = if (usuarioActual.value == null) "-1" else usuarioActual.value!!.dni
        if (database.inmuebleDAO().findByIdandDni(inmuebleId, dni) != null) return -1
        if (database.favoritoDAO().findByIdandDni(inmuebleId, dni) != null) return 0
        return 1
    }

    fun getCurrentUser(): Usuario? {
        return usuarioActual.value
    }


    fun updateCurrentUser(user: Usuario?) {
        this.usuarioActual.value = user
    }

    fun resetFiltro() {
        inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
        operacionesOpciones.value = mutableSetOf<String>()
        tiposOpciones.value = mutableSetOf<String>()
        preciosOpciones.value = IntArray(2)
        habitacionesOpciones.value = mutableSetOf<Int>()
        banosOpciones.value = mutableSetOf<Int>()
        estadoOpciones.value = mutableSetOf<String>()
        plantaOpciones.value = mutableSetOf<String>()
        preciosOpciones.value!!.set(0, database.inmuebleDAO().getMinPrecio())
        preciosOpciones.value!!.set(1, database.inmuebleDAO().getMaxPrecio())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarSesionActual(username: String) {
        database.sesionActualDAO()
            .insertSesion(SesionActual(username, LocalDateTime.now().toString()))
    }

    fun recuperarSesionActual() {
        val sesionActual = database.sesionActualDAO().getSesionActual()
        if (sesionActual != null) {
            usuarioActual.value = database.usuarioDAO().findByUsername(sesionActual.usuario)
        }

    }

    fun usuarioCorrecto(usuario: String, layout: TextInputLayout, fragment: Fragment): Boolean {
        if (usuario.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (usuario.length < 5) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.usuarioCorto)
            return false;
        }
        if (database.usuarioDAO().existsUsername(usuario)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.usuarioCogido)
            return false;
        }
        if (!formatoUsuarioCorrecto(usuario)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.usuarioIncorrecto)
            return false
        }

        layout.isErrorEnabled = false
        return true
    }

    private fun formatoUsuarioCorrecto(usuario: String): Boolean {
        for (i in usuario.indices) {
            if (usuario[i].toInt() == 32) {
                return false
            }
        }
        return true
    }

    fun dniCorrecto(dni: String, layout: TextInputLayout, fragment: Fragment): Boolean {
        if (dni.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (database.usuarioDAO().existsId(dni)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.dniCogido)
            return false;
        }

        if (!formatoDniCorrecto(dni)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.dniIncorrecto)
            return false;
        }
        layout.isErrorEnabled = false
        return true;
    }

    private fun formatoDniCorrecto(dni: String): Boolean {
        if (dni.length != 9) return false
        for (i in dni.indices) {
            if (i == 8) {
                if (!Character.isLetter(dni[i])) {
                    return false
                }
            } else if (!Character.isDigit(dni[i])) {
                return false

            }
        }
        return true
    }

    fun nombreCorrecto(nombre: String, layout: TextInputLayout, fragment: Fragment): Boolean {
        if (nombre.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (!formatoNombreApellidosCorrecto(nombre)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.nombreInvalido)
            return false;
        }

        layout.isErrorEnabled = false
        return true;
    }

    private fun formatoNombreApellidosCorrecto(nombreApellidos: String): Boolean {
        for (i in nombreApellidos.indices) {
            if (!Character.isLetter(nombreApellidos[i]) && nombreApellidos[i].toInt() != 32) {
                return false;
            }
        }
        return true
    }

    fun apellidosCorrectos(
        apellidos: String,
        layout: TextInputLayout,
        fragment: Fragment
    ): Boolean {
        if (apellidos.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }

        if (!formatoNombreApellidosCorrecto(apellidos)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.apellidosInvalidos)
            return false;
        }

        layout.isErrorEnabled = false
        return true;
    }

    fun telefonoCorrecto(telefono: String, layout: TextInputLayout,
                         fragment: Fragment): Boolean {
        if (telefono.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }

        if (!formatoTelefonoCorrecto(telefono)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.telefonoIncorrecto)
            return false;
        }
        layout.isErrorEnabled = false
        return true;
    }

    private fun formatoTelefonoCorrecto(telefono: String): Boolean{
        if(telefono.length != 9) return false
        for (i in telefono.indices) {
            if (!Character.isDigit(telefono[i])) {
                return false
            }
        }
        return true
    }

    fun contrasenaCorrecta(contrasena: String, layout: TextInputLayout,
                         fragment: Fragment): Boolean {
        if (contrasena.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (!formatoContrasenaCorrecto(contrasena)) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.contrasenaCorta)
            return false;
        }
        layout.isErrorEnabled = false;
        return true;
    }

    private fun formatoContrasenaCorrecto(contrasena: String): Boolean{
        return contrasena.length >= 8
    }

    fun coincidenContrasenas(contrasena: String, contrasenaRepetida: String, layout: TextInputLayout,
                             fragment: Fragment): Boolean {
        if (contrasenaRepetida.isEmpty()) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.completaCampo)
            return false;
        }
        if (contrasenaRepetida != contrasena) {
            layout.isErrorEnabled = true;
            layout.error = fragment.getString(R.string.contrasenaNoCoinciden)
            return false;
        }
        layout.isErrorEnabled = false
        return true;
    }
}
