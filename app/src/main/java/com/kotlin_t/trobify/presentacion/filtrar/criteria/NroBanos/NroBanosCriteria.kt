package com.kotlin_t.trobify.presentacion.filtrar.criteria.NroBanos

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Criteria

class NroBanosCriteria(val cantidad: Set<Int>) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if (cantidad.contains(4) && it.banos!! >= 4) {
                true
            } else if (cantidad.contains(3) && it.banos!! == 3) {
                true
            } else if (cantidad.contains(2) && it.banos!! == 2) {
                true
            } else cantidad.contains(1) && it.banos!! == 1
        }
    }
}