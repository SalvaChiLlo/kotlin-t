package com.kotlin_t.trobify.logica.recuperarFavoritos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.persistencia.Favorito

class RecuperarFavoritosViewModel(val database: AppDatabase, application: Application, val sharedViewModel: SharedViewModel) :
    AndroidViewModel(application){
   fun recuperarFavorito(favorito: Favorito){
       database.favoritoDAO().insertAll(favorito)
   }
}