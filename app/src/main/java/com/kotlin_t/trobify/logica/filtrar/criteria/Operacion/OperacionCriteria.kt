package com.kotlin_t.trobify.logica.filtrar.criteria.Operacion

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class OperacionCriteria(val tipoDeInmueble: Set<String>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter { tipoDeInmueble.contains(it.operacion) }
    }
}