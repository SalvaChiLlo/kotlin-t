package com.kotlin_t.trobify.logica.misInmuebles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.ContextClass

class MisInmueblesViewModel(val database: AppDatabase, application: Application, val contextClass: ContextClass) :
    AndroidViewModel(application) {
    fun getMisInmuebles(): List<Inmueble> {
        val dni = if(contextClass.usuarioActual.value != null) contextClass.usuarioActual.value!!.dni else "-1"
        val listaMisInmuebles = database.inmuebleDAO().loadAllByPropietario(dni)
        return listaMisInmuebles
    }
}