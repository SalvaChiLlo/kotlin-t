package com.kotlin_t.trobify.presentacion.filtrar.Criteria.TipoInmueble

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class TipoInmuebleCriteria(val operacion: String): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.operacion!!.equals(operacion, true) }
    }
}