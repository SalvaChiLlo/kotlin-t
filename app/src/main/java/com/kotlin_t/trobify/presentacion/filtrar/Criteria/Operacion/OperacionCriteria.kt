package com.kotlin_t.trobify.presentacion.filtrar.Criteria.Operacion

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class OperacionCriteria(val tipoDeInmueble: String): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { it.tipoDeInmueble!!.equals(tipoDeInmueble, true) }
    }
}