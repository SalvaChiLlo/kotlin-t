package com.kotlin_t.trobify.logica.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.ContextClass
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
    private val database: AppDatabase,
    private val application: Application,
    val contextClass: ContextClass
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(database, application, contextClass) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}