package com.kotlin_t.trobify.presentacion.ordenacion.criteria

import android.util.Log
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.ordenacion.EstrategiaOrdenacion

class PisosBajos: EstrategiaOrdenacion {
    override fun ordenar(inmuebles: List<Inmueble>): List<Inmueble> {
        val res = inmuebles.sortedBy{
            it.altura
        }
        return res
    }
    override fun toString(): String{
        return "Pisos Bajos"
    }

}