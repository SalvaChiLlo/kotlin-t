package com.kotlin_t.trobify.Presentacion.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.Persistencia.InmuebleDAO

class HomeViewModel(database: InmuebleDAO, application: Application): AndroidViewModel(application) {
}