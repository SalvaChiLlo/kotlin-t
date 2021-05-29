package com.kotlin_t.trobify.logica.recuperarFavoritos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.persistencia.Favorito

class RecuperarFavoritosViewModel(val database: AppDatabase, application: Application, val contextClass: ContextClass) :
    AndroidViewModel(application){
   fun recuperarFavorito(favorito: Favorito){
       database.favoritoDAO().insertAll(favorito)
       contextClass.favoritosEliminados.remove(favorito)
   }
}