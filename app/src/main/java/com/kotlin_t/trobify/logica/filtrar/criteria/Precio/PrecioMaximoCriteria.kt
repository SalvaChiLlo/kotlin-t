package com.kotlin_t.trobify.logica.filtrar.criteria.Precio

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria


class PrecioMaximoCriteria(val precioMAX: Int): Criteria {

    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.precio!! <= precioMAX }
    }
}