package com.kotlin_t.trobify.presentacion.filtrar.Criteria.Planta

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class PlantaCriteria(val altura: String) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if (altura.equals("alto", true)) {
                it.altura!! >= 10
            } else if (altura.equals("intermedio", true)) {
                it.altura!! in 3..10
            } else if (altura.equals("bajo", true)) {
                it.altura!! <= 3
            } else {
                false
            }
        }
    }
}