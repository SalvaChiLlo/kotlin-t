package com.kotlin_t.trobify.presentacion.filtrar.Criteria.NroBanos

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class NroBanosCriteria(val cantidad: Int): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if(cantidad == 4) {
                it.banos!! >= cantidad
            } else {
                it.banos!! == cantidad
            }
        }
    }
}