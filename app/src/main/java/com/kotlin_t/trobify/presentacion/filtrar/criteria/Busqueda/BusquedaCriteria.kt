package com.kotlin_t.trobify.presentacion.filtrar.criteria.Busqueda

import android.util.Log
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Criteria
import java.util.*

class BusquedaCriteria(val busqueda: String) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        return inmuebles.filter {
            it.direccion!!.toLowerCase().contains(busqueda.toLowerCase()) ||
                    busqueda.toLowerCase().contains(it.barrio!!.toLowerCase()) ||
                    it.descripcion!!.toLowerCase().contains(busqueda.toLowerCase()) ||
                    busqueda.toLowerCase().contains(it.municipio!!.toLowerCase()) ||
                    it.titulo!!.toLowerCase().contains(busqueda.toLowerCase()) ||
                    busqueda.toLowerCase().contains(it.pais!!.toLowerCase())
        }

    }
}