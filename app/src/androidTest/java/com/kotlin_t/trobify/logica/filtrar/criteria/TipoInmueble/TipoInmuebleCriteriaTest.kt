package com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class TipoInmuebleCriteriaTest {

    lateinit var inmueble1: Inmueble
    lateinit var inmueble2: Inmueble
    lateinit var inmueble3: Inmueble
    lateinit var inmueble4: Inmueble
    lateinit var inmueble5: Inmueble
    lateinit var inmueble6: Inmueble
    lateinit var listaInmuebles: List<Inmueble>

    lateinit var criteria: TipoInmuebleCriteria
    @Before
    fun setUp() {
        inmueble1 = Constantes.crearInmueble1() // Atico
        inmueble2 = Constantes.crearInmueble2() // Casa_Chalet
        inmueble3 = Constantes.crearInmueble3() // Habitacion
        inmueble4 = Constantes.crearInmueble4() // PISO
        inmueble5 = Constantes.crearInmueble5() // ATICO
        inmueble6 = Constantes.crearInmueble6() // Casa_Chalet
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
    fun tipoPiso() {
        criteria = TipoInmuebleCriteria(setOf(Constantes.PISO))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble4))
    }

    @Test
    fun tipoChalet() {
        criteria = TipoInmuebleCriteria(setOf(Constantes.CASA_CHALET))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble2, inmueble6))
    }

    @Test
    fun tipoAtico() {
        criteria = TipoInmuebleCriteria(setOf(Constantes.ATICO))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble1, inmueble5))
    }

    @Test
    fun tipoAticoyPiso() {
        criteria = TipoInmuebleCriteria(setOf(Constantes.ATICO, Constantes.PISO))
        assertEquals(criteria.meetCriteria(listaInmuebles), listOf(inmueble1, inmueble4 ,inmueble5))
    }

}