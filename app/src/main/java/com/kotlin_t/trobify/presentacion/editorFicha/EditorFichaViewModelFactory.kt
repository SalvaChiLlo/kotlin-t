package com.kotlin_t.trobify.presentacion.editorFicha

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.busqueda.BusquedaViewModel
import java.lang.IllegalArgumentException

class EditorFichaViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val sharedModel: SharedViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorFichaViewModel::class.java)) {
            return EditorFichaViewModel(database, application, sharedModel) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}