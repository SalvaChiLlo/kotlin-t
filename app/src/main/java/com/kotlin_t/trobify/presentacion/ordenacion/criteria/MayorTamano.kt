package com.kotlin_t.trobify.presentacion.ordenacion.criteria

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.ordenacion.EstrategiaOrdenacion

class MayorTamano: EstrategiaOrdenacion {
    override fun ordenar(inmuebles: List<Inmueble>): List<Inmueble> {
        val res = inmuebles.sortedByDescending {
            it.tamano
        }
        return res
    }
    override fun toString(): String{
        return "Más grande"
    }

}