package com.kotlin_t.trobify.presentacion.misInmuebles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.SharedViewModel

class MisInmueblesViewModel(val database: AppDatabase, application: Application, val sharedViewModel: SharedViewModel) :
    AndroidViewModel(application) {
    fun getMisInmuebles(): List<Inmueble> {
        val dni = if(sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        val listaMisInmuebles = database.inmuebleDAO().loadAllByPropietario(dni)
        return listaMisInmuebles
    }
}