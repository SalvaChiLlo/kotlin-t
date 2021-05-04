package com.kotlin_t.trobify.logica.ordenacion

import com.kotlin_t.trobify.persistencia.Inmueble

interface EstrategiaOrdenacion {
    fun ordenar(inmuebles: List<Inmueble>): List<Inmueble>
}