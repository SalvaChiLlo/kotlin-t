package com.kotlin_t.trobify.logica

import android.app.Application
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
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
    //var usuario: Usuario? = null
    val usuarioActual: MutableLiveData<Usuario> by lazy {
        MutableLiveData<Usuario>()
    }
    val favoritosEliminados = mutableSetOf<Favorito>()


    val usuarioActual__PRUEBA = database.usuarioDAO().findById("-1")

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
    var busquedaString = "";

    init {

        if (database.usuarioDAO().getAll().isEmpty()) {
            val usuarioPopulate = Usuario(
                "$2345678E",
                "username",
                "contraseña",
                "nombre",
                "appellido$",
                "999888777",
                "iban",
                null
            )
            database.usuarioDAO().insertAll(usuarioPopulate!!)

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

    fun getUserFromCredentials(username: String, password: String) : Usuario? {

        //Log.i("USERNAME", "First username: " + usuarios.value!!.get(0).username)
        //Log.i("PASSWORD", "First password: " + usuarios.value!!.get(0).contrasena)
        //Log.i("VARIABLES", "Provided info Username: " + username + " Contraseña: " + password)

        val res: Usuario? = usuarios.value?.find { it.username == username && it.contrasena == password }

        return res

    }

    fun isInmuebleFavorito(inmuebleId: Int) : Boolean {
        if(usuarioActual == null) return false
        return null != database.favoritoDAO().findByIdandDni(inmuebleId,usuarioActual.value?.dni.toString())
    }

    fun getCurrentUser() : Usuario? {
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
        database.sesionActualDAO().insertSesion(SesionActual(username, LocalDateTime.now().toString()))
    }
    fun recuperarSesionActual(){
        val sesionActual = database.sesionActualDAO().getSesionActual()
        if(sesionActual != null){
            usuarioActual.value = database.usuarioDAO().findByUsername(sesionActual.usuario)
        }

    }

}
