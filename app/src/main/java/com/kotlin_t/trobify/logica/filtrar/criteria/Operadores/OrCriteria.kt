package com.kotlin_t.trobify.logica.filtrar.criteria.Operadores

import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.filtrar.criteria.Criteria

class OrCriteria(val criteria: Criteria, val otherCriteria: Criteria) : Criteria {
    override fun meetCriteria(inmuebles: List<Inmueble>): List<Inmueble> {
        var firstCriteriaInmuebles = criteria.meetCriteria(inmuebles) as MutableList
        var otherCriteriaInmuebles = criteria.meetCriteria(inmuebles) as MutableList

        for (inmueble in otherCriteriaInmuebles) {
            if (!firstCriteriaInmuebles.contains(inmueble)) {
                firstCriteriaInmuebles.add(inmueble)
            }
        }

        return firstCriteriaInmuebles
    }
}