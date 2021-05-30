package com.kotlin_t.trobify.logica.ordenacion

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.ContextClass
import java.lang.IllegalArgumentException

class OrdenacionViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    private val contextClass: ContextClass,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdenacionViewModel::class.java)) {
            return OrdenacionViewModel(database, application, contextClass) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }

}