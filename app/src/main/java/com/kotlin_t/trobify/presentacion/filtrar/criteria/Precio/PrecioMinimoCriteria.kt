package com.kotlin_t.trobify.presentacion.filtrar.criteria.Precio

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Criteria

class PrecioMinimoCriteria(val precioMIN: Int): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.precio!! >= precioMIN }
    }
}