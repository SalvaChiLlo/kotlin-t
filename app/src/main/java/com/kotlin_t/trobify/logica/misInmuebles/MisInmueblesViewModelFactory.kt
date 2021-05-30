package com.kotlin_t.trobify.logica.misInmuebles

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.ContextClass
import java.lang.IllegalArgumentException

class MisInmueblesViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    val contextClass: ContextClass
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisInmueblesViewModel::class.java)) {
            return MisInmueblesViewModel(database, application, contextClass) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}