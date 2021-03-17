package com.kotlin_t.trobify.presentacion.filtrar.Criteria.Estado

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class EstadoCriteria(val estado: Set<String>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { estado.contains(it.estado) }
    }
}