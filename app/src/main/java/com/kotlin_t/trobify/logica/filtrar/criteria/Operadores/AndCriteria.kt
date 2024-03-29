package com.kotlin_t.trobify.logica.filtrar.criteria.Operadores

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class AndCriteria(val criteria: Criteria, val otherCriteria: Criteria) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        val firstCriteriaInmuebles = criteria.meetCriteria(inmuebles)
        return otherCriteria.meetCriteria(firstCriteriaInmuebles)
    }

}