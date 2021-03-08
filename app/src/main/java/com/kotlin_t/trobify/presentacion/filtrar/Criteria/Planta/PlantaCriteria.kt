package com.kotlin_t.trobify.presentacion.filtrar.Criteria.Planta

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class PlantaCriteria(val planta: Set<String>) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if (planta.contains("alta") && it.altura!! >= 10) {
                true
            } else if (planta.contains("intermedia") && it.altura!! in 3..10) {
                true
            } else planta.contains("baja") && it.altura!! <= 3
        }
    }
}