package com.kotlin_t.trobify.presentacion.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.persistencia.InmuebleDAO

class HomeViewModel(database: InmuebleDAO, application: Application): AndroidViewModel(application) {
}