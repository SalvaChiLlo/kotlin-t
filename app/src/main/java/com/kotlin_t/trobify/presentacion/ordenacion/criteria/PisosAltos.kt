package com.kotlin_t.trobify.presentacion.ordenacion.criteria

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.ordenacion.EstrategiaOrdenacion

class PisosAltos: EstrategiaOrdenacion {
    override fun ordenar(inmuebles: List<Inmueble>): List<Inmueble> {
        val res = inmuebles.sortedByDescending {
            it.altura
        }
        return res
    }
    override fun toString(): String{
        return "Pisos Altos"
    }
}