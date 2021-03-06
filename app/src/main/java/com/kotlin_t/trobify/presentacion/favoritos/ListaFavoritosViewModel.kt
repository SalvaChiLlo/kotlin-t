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
//        return listaInmuebles
        return listOf(
            Inmueble(
                "weq",
                "dasdasd",
                true,
                null,
                null,
                2,
                12345,
                "piso",
                "alquiler",
                123,
                false,
                5,
                2,
                "Valencia",
                "Valencia",
                "Benimaclet",
                "España",
                1.23322,
                36.00000,
                "nuevo",
                true,
                123,
                "Titulo",
                "213",
                "Descripcion"
            )
        )
    }

    fun checkIfFavorito(inmuebleId: Int): Boolean {
        val favorito = database.favoritoDAO().findById(inmuebleId.toString())

        return true
    }
}

