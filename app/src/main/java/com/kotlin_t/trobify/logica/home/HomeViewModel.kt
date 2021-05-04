package com.kotlin_t.trobify.logica.home

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.persistencia.Favorito
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.SharedViewModel

class HomeViewModel(val database: AppDatabase, application: Application, val sharedViewModel: SharedViewModel): AndroidViewModel(application) {
    private val inmuebleDatabase = database.inmuebleDAO()
    private val favoritoDatabase = database.favoritoDAO()

    private var _listaInmubles = MutableLiveData<List<Inmueble>>()
    val listaInmuebles: LiveData<List<Inmueble>>
        get() = _listaInmubles


    fun getListaInmubles(): List<Inmueble>{
        _listaInmubles.value = inmuebleDatabase.getAll()
        return listaInmuebles.value!!
    }

    fun setFavoriteIcon(inmueble: Inmueble, favoritoIMG: ImageView){
        var fav:Int
        val dni = if(sharedViewModel.usuarioActual.value != null) sharedViewModel.usuarioActual.value!!.dni else "-1"
        val favorito = favoritoDatabase.findByIdandDni(inmueble.inmuebleId, dni)
        if(favorito != null)fav = R.drawable.ic_baseline_favorite_24
        else fav = R.drawable.ic_baseline_favorite_border_24
        favoritoIMG.setImageResource(fav)
        favoritoIMG.setOnClickListener{
            if(fav == R.drawable.ic_baseline_favorite_24){
                favoritoDatabase.deleteByPK(favorito!!.primaryKey)
                fav = R.drawable.ic_baseline_favorite_border_24
            }
            else{
                favoritoDatabase.insertAll(Favorito(inmueble.inmuebleId, dni))
                fav = R.drawable.ic_baseline_favorite_24
            }
            favoritoIMG.setImageResource(fav)
        }
    }



}