package com.kotlin_t.trobify.presentacion.favoritos

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.R
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

    fun checkIfFavorito(inmueble: Inmueble, imageView: ImageView) {
        val search = database.favoritoDAO().findById(inmueble.inmuebleId)
        if (search != null) {
            imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    fun addOrRemoveFavorite(inmueble: Inmueble, dni: String?, imageView: ImageView) {
        val search = database.favoritoDAO().findById(inmueble.inmuebleId)
        if (search == null) {
            database.favoritoDAO().insertAll(Favorito(inmueble.inmuebleId, dni))
        } else {
            database.favoritoDAO().delete(Favorito(inmueble.inmuebleId, dni))
        }
        checkIfFavorito(inmueble, imageView)
    }
}

