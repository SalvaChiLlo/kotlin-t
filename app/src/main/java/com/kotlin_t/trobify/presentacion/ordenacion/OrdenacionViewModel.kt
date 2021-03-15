package com.kotlin_t.trobify.presentacion.ordenacion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.SharedViewModel

class OrdenacionViewModel(database: AppDatabase, application: Application, private val model: SharedViewModel, ) : AndroidViewModel(application) {
    private var inmuebles: List<Inmueble>
    init {
        inmuebles = database.inmuebleDAO().getAll()

    }



}