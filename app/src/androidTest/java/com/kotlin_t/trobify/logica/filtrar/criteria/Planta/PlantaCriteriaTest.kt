package com.kotlin_t.trobify.logica.filtrar.criteria.Planta

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class PlantaCriteriaTest {
    lateinit var inmueble1: Inmueble
    lateinit var inmueble2: Inmueble
    lateinit var inmueble3: Inmueble
    lateinit var inmueble4: Inmueble
    lateinit var inmueble5: Inmueble
    lateinit var inmueble6: Inmueble
    lateinit var listaInmuebles: List<Inmueble>

    lateinit var criteria: PlantaCriteria

    @Before
    fun setUp() {
        inmueble1 = Constantes.crearInmueble1() // 1
        inmueble2 = Constantes.crearInmueble2() // 2
        inmueble3 = Constantes.crearInmueble3() // 3
        inmueble4 = Constantes.crearInmueble4() // 4
        inmueble5 = Constantes.crearInmueble5() // 5
        inmueble6 = Constantes.crearInmueble6() // 6
        listaInmuebles = listOf(
            inmueble1,
            inmueble2,
            inmueble3,
            inmueble4,
            inmueble5,
            inmueble6
        )

    }

    @Test
    fun plantaBajayAlta() {
        criteria =
            PlantaCriteria(setOf(Constantes.PLANTA_BAJA, Constantes.PLANTA_ALTA)) // <= 3 && >= 10
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble1, inmueble2, inmueble3))
    }

    @Test
    fun plantaAlta() {
        criteria = PlantaCriteria(setOf(Constantes.PLANTA_ALTA)) // >= 10
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf<Inmueble>())
    }

    @Test
    fun plantaIntermedia() {
        criteria = PlantaCriteria(setOf(Constantes.PLANTA_INTERMEDIA)) // > 3 && < 10
        assertEquals(
            criteria.meetCriteria(listaInmuebles), listOf<Inmueble>(
                inmueble4,
                inmueble5,
                inmueble6
            )
        )
    }

    @Test
    fun todasLasPlantas() {
        criteria = PlantaCriteria(setOf(Constantes.PLANTA_INTERMEDIA, Constantes.PLANTA_ALTA, Constantes.PLANTA_BAJA)) // > 3 && < 10
        assertEquals(
            criteria.meetCriteria(listaInmuebles), listOf<Inmueble>(
                inmueble1,
                inmueble2,
                inmueble3,
                inmueble4,
                inmueble5,
                inmueble6
            )
        )
    }

}