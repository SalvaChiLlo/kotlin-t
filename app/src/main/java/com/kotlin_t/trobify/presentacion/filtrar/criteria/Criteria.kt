package com.kotlin_t.trobify.presentacion.filtrar.criteria

import com.kotlin_t.trobify.logica.Inmueble

interface Criteria {
    fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble>
}