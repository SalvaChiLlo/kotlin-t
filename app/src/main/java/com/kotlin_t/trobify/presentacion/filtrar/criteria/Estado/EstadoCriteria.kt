package com.kotlin_t.trobify.presentacion.filtrar.criteria.Estado

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Criteria

class EstadoCriteria(val estado: Set<String>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { estado.contains(it.estado) }
    }
}