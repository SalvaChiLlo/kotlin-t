package com.kotlin_t.trobify.presentacion.ficha

import androidx.lifecycle.ViewModel
import com.kotlin_t.trobify.logica.Inmueble

class FichaViewModel : ViewModel() {
    lateinit var inmueble : Inmueble


    fun setInmueble(setter: Inmueble) {
        this.inmueble = setter
    }
}