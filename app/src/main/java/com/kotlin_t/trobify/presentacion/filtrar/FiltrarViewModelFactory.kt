package com.kotlin_t.trobify.presentacion.filtrar

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentFiltrarBinding
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import java.lang.IllegalArgumentException

class FiltrarViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val model: SharedViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltrarViewModel::class.java)) {
            return FiltrarViewModel(database, application, model) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}