package com.kotlin_t.trobify.logica.favoritos

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.SharedViewModel
import java.lang.IllegalArgumentException

class ListaFavoritosViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    val sharedViewModel: SharedViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaFavoritosViewModel::class.java)) {
            return ListaFavoritosViewModel(database, application, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}