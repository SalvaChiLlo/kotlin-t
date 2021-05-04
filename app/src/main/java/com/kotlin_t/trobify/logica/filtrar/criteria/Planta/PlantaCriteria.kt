package com.kotlin_t.trobify.logica.filtrar.criteria.Planta

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class PlantaCriteria(val planta: Set<String>) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if (planta.contains(Constantes.PLANTA_ALTA) && it.altura!! >= 10) {
                true
            } else if (planta.contains(Constantes.PLANTA_BAJA) && it.altura!! <= 3) {
                true
            } else planta.contains(Constantes.PLANTA_INTERMEDIA) && it.altura!! in 4..9
        }
    }
}