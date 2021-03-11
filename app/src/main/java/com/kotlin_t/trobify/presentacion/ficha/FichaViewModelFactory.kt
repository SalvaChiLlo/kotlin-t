package com.kotlin_t.trobify.presentacion.ficha

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import java.lang.IllegalArgumentException

class FichaViewModelFactory(
    private val database: AppDatabase,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FichaViewModel::class.java)) {
            return FichaViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}