package com.kotlin_t.trobify.logica.filtrar.criteria.NroHabitaciones

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class NroHabitacionesCriteria(val cantidad: Set<Int>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            if (cantidad.contains(4) && it.habitaciones!! >= 4) {
                true
            } else if (cantidad.contains(3) && it.habitaciones!! == 3) {
                true
            } else if (cantidad.contains(2) && it.habitaciones!! == 2) {
                true
            } else cantidad.contains(1) && it.habitaciones!! == 1
        }
    }
}