package com.kotlin_t.trobify.presentacion.ordenacion.criteria

import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.ordenacion.EstrategiaOrdenacion

class PrecioMasBajo : EstrategiaOrdenacion {
    override fun ordenar(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.sortedBy {
             it.precio

         }
    }
    override fun toString(): String{
        return "Precio más Bajo"
    }


}