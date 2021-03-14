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

class SharedViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    private val _inmuebles = MutableLiveData<List<Inmueble>>()
    val inmuebles: LiveData<List<Inmueble>> = _inmuebles

    init {
        _inmuebles.value = AppDatabase.getDatabase(application).inmuebleDAO().getAll()
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

}
