package com.kotlin_t.trobify.presentacion

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Inmueble

class SharedViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    val inmuebles = MutableLiveData<List<Inmueble>>()

    init {
        inmuebles.value = AppDatabase.getDatabase(application).inmuebleDAO().getAll()
    }

    fun setInmuebles(inmuebles: List<Inmueble>) {
        this.inmuebles.value = inmuebles
    }
}
