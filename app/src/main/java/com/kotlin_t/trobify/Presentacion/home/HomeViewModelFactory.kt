package com.kotlin_t.trobify.Presentacion.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.Persistencia.InmuebleDAO
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
    private val database: InmuebleDAO,
    private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}