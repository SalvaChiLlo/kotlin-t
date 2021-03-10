package com.kotlin_t.trobify.presentacion.filtrar.Criteria.TipoInmueble

import android.util.Log
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Criteria

class TipoInmuebleCriteria(val operacion: Set<String>): Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            operacion.contains(it.tipoDeInmueble)
        }
    }
}