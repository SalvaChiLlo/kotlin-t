package com.kotlin_t.trobify.presentacion.registrar

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.editorFicha.EditorFichaViewModel
import java.lang.IllegalArgumentException

class RegistrarseViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val sharedModel: SharedViewModel,
    private val binding:FragmentRegistrarseBinding,
    private val fragment: RegistrarseFragment

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrarseViewModel::class.java)) {
            return RegistrarseViewModel(database, application, sharedModel, binding, fragment) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}