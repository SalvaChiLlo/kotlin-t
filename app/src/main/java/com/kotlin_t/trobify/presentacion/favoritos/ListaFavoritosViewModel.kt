package com.kotlin_t.trobify.presentacion.favoritos

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.persistencia.InmuebleDAO

class ListaFavoritosViewModel(val database: AppDatabase, application: Application) :
    AndroidViewModel(application) {
    fun getInmueblesFavoritos(): List<Inmueble> {
        val listaFavoritos = database.favoritoDAO().getAll()
        val listaInmuebles = mutableListOf<Inmueble>()
        listaFavoritos.forEach {
            listaInmuebles.add(database.inmuebleDAO().findById(it.inmuebleId.toString()))
        }
        Log.e("FAVV", listaFavoritos.toString())
        Log.e("FAVV", listaInmuebles.toString())
        return listaInmuebles
    }

    fun checkIfFavorito(inmuebleId: Int): Boolean {
        val favorito = database.favoritoDAO().findById(inmuebleId.toString())

        return true
    }
}

