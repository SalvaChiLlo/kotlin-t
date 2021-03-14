package com.kotlin_t.trobify.presentacion.ficha

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.logica.Usuario

class FichaViewModel(private val database: AppDatabase,
                     private val application: Application
) : ViewModel() {
    lateinit var inmueble : Inmueble
    lateinit var usuario : Usuario

    fun setHouse(inmueble: Inmueble) {
        this.inmueble = inmueble
        //TODO buscar en el DAO el usuario por el dni del inmueble asignado
        //setUser()
    }

    private fun setUser(owner : Usuario) {
        this.usuario = owner
    }
}