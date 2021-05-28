package com.kotlin_t.trobify.logica.filtrar.criteria.Estado

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class EstadoCriteriaTest {
    lateinit var inmueble1: Inmueble
    lateinit var inmueble2: Inmueble
    lateinit var inmueble3: Inmueble
    lateinit var inmueble4: Inmueble
    lateinit var inmueble5: Inmueble
    lateinit var inmueble6: Inmueble
    lateinit var listaInmuebles: List<Inmueble>

    lateinit var criteria: EstadoCriteria
    @Before
    fun setUp() {
        inmueble1 = Constantes.crearInmueble1() // BUEN_ESTADO
        inmueble2 = Constantes.crearInmueble2() // BUEN_ESTADO
        inmueble3 = Constantes.crearInmueble3() // REFORMAR
        inmueble4 = Constantes.crearInmueble4() // BUEN_ESTADO
        inmueble5 = Constantes.crearInmueble5() // NUEVA_CONSTRUCCION
        inmueble6 = Constantes.crearInmueble6() // NUEVA_CONSTRUCCION
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
    fun estadoBuenoYReformar() {
        criteria = EstadoCriteria(setOf(Constantes.BUEN_ESTADO, Constantes.REFORMAR))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble1, inmueble2, inmueble3, inmueble4))
    }

    @Test
    fun estadoReformar() {
        criteria = EstadoCriteria(setOf(Constantes.REFORMAR))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble3))
    }

    @Test
    fun estadoNuevaConstruccion() {
        criteria = EstadoCriteria(setOf(Constantes.NUEVA_CONSTRUCCION))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble5, inmueble6))
    }
}