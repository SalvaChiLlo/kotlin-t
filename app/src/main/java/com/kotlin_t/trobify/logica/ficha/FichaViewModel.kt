package com.kotlin_t.trobify.logica.ficha

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.persistencia.Usuario

class FichaViewModel(private val database: AppDatabase,
                     private val application: Application
) : ViewModel() {

    lateinit var inmueble: Inmueble
    lateinit var usuario: Usuario
    val favoritoDatabase = database.favoritoDAO()

    fun setHouse(id : Int) {
        this.inmueble = database.inmuebleDAO().findById(id.toString())
        setUser(database.usuarioDAO().findById(inmueble.dniPropietario))
    }

    private fun setUser(owner : Usuario) {
        this.usuario = owner
    }
}