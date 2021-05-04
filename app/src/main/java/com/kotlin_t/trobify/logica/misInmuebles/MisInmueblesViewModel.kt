package com.kotlin_t.trobify.logica.misInmuebles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.SharedViewModel

class MisInmueblesViewModel(val database: AppDatabase, application: Application, val sharedViewModel: SharedViewModel) :
    AndroidViewModel(application) {
    fun getMisInmuebles(): List<Inmueble> {
        val dni = if(sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        val listaMisInmuebles = database.inmuebleDAO().loadAllByPropietario(dni)
        return listaMisInmuebles
    }
}