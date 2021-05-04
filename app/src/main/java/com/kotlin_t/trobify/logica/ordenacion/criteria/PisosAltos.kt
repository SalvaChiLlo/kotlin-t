package com.kotlin_t.trobify.logica.ordenacion.criteria

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.ordenacion.EstrategiaOrdenacion

class PisosAltos: EstrategiaOrdenacion {
    override fun ordenar(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.sortedByDescending {
            it.altura
        }
    }
    override fun toString(): String{
        return "Pisos Altos"
    }
}