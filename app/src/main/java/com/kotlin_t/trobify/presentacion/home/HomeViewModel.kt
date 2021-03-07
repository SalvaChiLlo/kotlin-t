package com.kotlin_t.trobify.presentacion.home

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.persistencia.InmuebleDAO

class HomeViewModel(val database: AppDatabase, application: Application): AndroidViewModel(application) {
    private val inmuebleDatabase = database.inmuebleDAO()
    private val favoritoDatabase = database.favoritoDAO()

    private var _listaInmubles = MutableLiveData<List<Inmueble>>()
    val listaInmuebles: LiveData<List<Inmueble>>
        get() = _listaInmubles


    fun getListaInmubles(): List<Inmueble>{
        _listaInmubles.value = inmuebleDatabase.getAll()
        return listaInmuebles.value!!
    }

    fun setFavoriteIcon(inmueble: Inmueble, favorito: ImageView){
        var fav:Int
        if(favoritoDatabase.findById(inmueble.inmuebleId) != null)fav = R.drawable.ic_baseline_favorite_24
        else fav = R.drawable.ic_baseline_favorite_border_24
        favorito.setImageResource(fav)
        favorito.setOnClickListener{
            if(fav == R.drawable.ic_baseline_favorite_24){
                favoritoDatabase.deleteById(inmueble.inmuebleId)
                fav = R.drawable.ic_baseline_favorite_border_24
            }
            else{
                favoritoDatabase.insertAll(Favorito(inmueble.inmuebleId, null))
                fav = R.drawable.ic_baseline_favorite_24
            }
            favorito.setImageResource(fav)
        }
    }



}