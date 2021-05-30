package com.kotlin_t.trobify.logica.busqueda

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.ContextClass
import java.lang.IllegalArgumentException

class BusquedaViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val sharedModel: ContextClass,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusquedaViewModel::class.java)) {
            return BusquedaViewModel(database, application, sharedModel, viewLifecycleOwner) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}