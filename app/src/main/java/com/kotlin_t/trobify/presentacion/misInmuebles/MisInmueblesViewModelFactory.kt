package com.kotlin_t.trobify.presentacion.misInmuebles

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.misInmuebles.MisInmueblesViewModel
import java.lang.IllegalArgumentException

class MisInmueblesViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    val sharedViewModel: SharedViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisInmueblesViewModel::class.java)) {
            return MisInmueblesViewModel(database, application, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}