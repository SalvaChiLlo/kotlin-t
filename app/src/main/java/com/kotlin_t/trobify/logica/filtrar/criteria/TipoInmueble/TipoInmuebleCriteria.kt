package com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class TipoInmuebleCriteria(val operacion: Set<String>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            operacion.contains(it.tipoDeInmueble)
        }
    }
}