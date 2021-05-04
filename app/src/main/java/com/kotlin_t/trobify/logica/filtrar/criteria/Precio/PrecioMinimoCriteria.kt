package com.kotlin_t.trobify.logica.filtrar.criteria.Precio

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class PrecioMinimoCriteria(val precioMIN: Int): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.precio!! >= precioMIN }
    }
}