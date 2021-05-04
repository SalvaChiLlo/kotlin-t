package com.kotlin_t.trobify.logica.filtrar.criteria

import com.kotlin_t.trobify.persistencia.Inmueble

interface Criteria {
    fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble>
}