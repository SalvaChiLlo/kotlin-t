package com.kotlin_t.trobify.presentacion.filtrar.Criteria.NroHabitaciones

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class NroHabitacionesCriteria(val cantidad: Int): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if(cantidad == 4) {
                it.habitaciones!! >= cantidad
            } else {
                it.habitaciones!! == cantidad
            }
        }
    }
}