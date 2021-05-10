package com.kotlin_t.trobify.logica.favoritos

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.persistencia.Favorito
import com.kotlin_t.trobify.persistencia.Inmueble

class ListaFavoritosViewModel(val database: AppDatabase, application: Application, val sharedViewModel: SharedViewModel) :
    AndroidViewModel(application) {
    fun getInmueblesFavoritos(): List<Favorito> {
        val dni = if(sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        val listaFavoritos = database.favoritoDAO().findByDNI(dni)
        return listaFavoritos
    }

    fun checkIfFavorito(favorito: Favorito, imageView: ImageView) {
        val search = database.favoritoDAO().findByIdandDni(favorito.inmuebleId,favorito.dni.toString())
        if (search != null) {
            imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    fun addOrRemoveFavorite(favorito: Favorito, dni: String?, imageView: ImageView) {
        val search = database.favoritoDAO().findByIdandDni(favorito.inmuebleId, favorito.dni.toString())
        if (search == null) {
            database.favoritoDAO().insertAll(favorito)
            Log.e("FAvoritos", "${sharedViewModel.favoritosEliminados.remove(favorito)}")
        } else {
            database.favoritoDAO().delete(favorito)
            Log.e("FAvoritos", "${sharedViewModel.favoritosEliminados.add(favorito)}")
        }
        checkIfFavorito(favorito, imageView)
    }
}