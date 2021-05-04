package com.kotlin_t.trobify.logica.filtrar.criteria.Busqueda

import com.kotlin_t.trobify.persistencia.Busqueda
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria
import java.text.Normalizer

class BusquedaCriteria(val busqueda: String) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {

        return inmuebles.filter {
            cleanString(it.direccion!!.toLowerCase()).contains(cleanString(busqueda.toLowerCase())) ||
                    cleanString(busqueda.toLowerCase()).contains(cleanString(it.barrio!!.toLowerCase())) ||
                    cleanString(it.descripcion!!.toLowerCase()).contains(cleanString(busqueda.toLowerCase())) ||
                    cleanString(busqueda.toLowerCase()).contains(cleanString(it.municipio!!.toLowerCase())) ||
                    cleanString(it.titulo!!.toLowerCase()).contains(cleanString(busqueda.toLowerCase())) ||
                    cleanString(busqueda.toLowerCase()).contains(cleanString(it.pais!!.toLowerCase()))
        }

    }

    fun meetCriteriaBusqueda(busquedas: List<Busqueda>): List<Busqueda> {
        return busquedas.filter {
            cleanString(it.busqueda.toLowerCase()).contains(cleanString(busqueda))
        }

    }

    fun cleanString(texto: String): String {
        var texto = Normalizer.normalize(texto, Normalizer.Form.NFD)
        texto = texto.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        return texto
    }
}