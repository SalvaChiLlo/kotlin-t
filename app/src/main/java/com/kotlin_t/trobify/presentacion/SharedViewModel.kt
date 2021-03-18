package com.kotlin_t.trobify.presentacion

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.ordenacion.EstrategiaOrdenacion

class SharedViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    val inmuebles = MutableLiveData<List<Inmueble>>()
    var estrategiaOrdenacion: EstrategiaOrdenacion? = null

    // Variables de filtro
    var operacionesOpciones = MutableLiveData<MutableSet<String>>()
    var tiposOpciones = MutableLiveData<MutableSet<String>>()
    var preciosOpciones = MutableLiveData<IntArray>()
    var habitacionesOpciones = MutableLiveData<MutableSet<Int>>()
    var banosOpciones = MutableLiveData<MutableSet<Int>>()
    var estadoOpciones = MutableLiveData<MutableSet<String>>()
    var plantaOpciones = MutableLiveData<MutableSet<String>>()
    var database = AppDatabase.getDatabase(application)
  
    init {
        inmuebles.value = database.inmuebleDAO().getAll()
        operacionesOpciones.value = mutableSetOf<String>()
        tiposOpciones.value = mutableSetOf<String>()
        preciosOpciones.value = IntArray(2)
        habitacionesOpciones.value = mutableSetOf<Int>()
        banosOpciones.value = mutableSetOf<Int>()
        estadoOpciones.value = mutableSetOf<String>()
        plantaOpciones.value = mutableSetOf<String>()
    }

    fun setInmuebles(inmuebles: List<Inmueble>) {
        this._inmuebles.value = inmuebles
    }


    fun getInmuebleIdFromLatLng(localizacion: LatLng): Int {

        val latitud = localizacion.latitude
        val longitud = localizacion.longitude

        val inmueble: Inmueble? = inmuebles.value!!.find { it.latitud == latitud && it.longitud == longitud }

        if (inmueble != null) {
            return inmueble.inmuebleId
        }

        return -1
    }

    fun getFirstInmueble(): Inmueble {
        return inmuebles.value!![0]
    }


    fun resetFiltro() {
        inmuebles.value = database.inmuebleDAO().getAll()
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

}
