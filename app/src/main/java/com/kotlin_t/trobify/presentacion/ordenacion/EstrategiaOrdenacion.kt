package com.kotlin_t.trobify.presentacion.ordenacion

import com.kotlin_t.trobify.logica.Inmueble

interface EstrategiaOrdenacion {
    fun ordenar(inmuebles: List<Inmueble>): List<Inmueble>
}