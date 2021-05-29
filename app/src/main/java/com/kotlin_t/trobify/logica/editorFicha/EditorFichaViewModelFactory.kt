package com.kotlin_t.trobify.logica.editorFicha

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditorFichaBinding
import java.lang.IllegalArgumentException

class EditorFichaViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val sharedModel: com.kotlin_t.trobify.logica.ContextClass,
    private var binding: FragmentEditorFichaBinding,
    private val context: Context

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorFichaViewModel::class.java)) {
            return EditorFichaViewModel(database, application, sharedModel, binding, context) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}