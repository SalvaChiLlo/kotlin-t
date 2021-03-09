package com.kotlin_t.trobify.presentacion.filtrar.Criteria.Precio

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria


class PrecioMaximoCriteria(val precioMAX: Int): Criteria {

    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.precio!! <= precioMAX }
    }
}