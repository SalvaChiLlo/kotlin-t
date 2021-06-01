package com.kotlin_t.trobify.logica.filtrar.criteria.Operadores

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.filtrar.criteria.Estado.EstadoCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class AndCriteriaTest {
    lateinit var inmueble1: Inmueble
    lateinit var inmueble2: Inmueble
    lateinit var inmueble3: Inmueble
    lateinit var inmueble4: Inmueble
    lateinit var inmueble5: Inmueble
    lateinit var inmueble6: Inmueble
    lateinit var listaInmuebles: List<Inmueble>

    lateinit var andCriteria: AndCriteria
    lateinit var estadoCriteria: EstadoCriteria
    lateinit var tipoInmuebleCriteria: TipoInmuebleCriteria

    @Before
    fun setUp() {
        inmueble1 = Constantes.crearInmueble1() // Atico & BUEN_ESTADO
        inmueble2 = Constantes.crearInmueble2() // Casa_Chalet & BUEN_ESTADO
        inmueble3 = Constantes.crearInmueble3() // Habitacion & REFORMAR
        inmueble4 = Constantes.crearInmueble4() // PISO & BUEN_ESTADO
        inmueble5 = Constantes.crearInmueble5() // ATICO & NUEVA_CONSTRUCCION
        inmueble6 = Constantes.crearInmueble6() // Casa_Chalet & NUEVA_CONSTRUCCION
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
    fun reformarYPiso() {
        tipoInmuebleCriteria = TipoInmuebleCriteria(setOf(Constantes.REFORMAR))
        estadoCriteria = EstadoCriteria(setOf(Constantes.PISO))
        andCriteria = AndCriteria(tipoInmuebleCriteria, estadoCriteria)
        assertEquals(andCriteria.meetCriteria(listaInmuebles), listOf<Inmueble>())
    }

    @Test
    fun buenEstadoYCasa() {
        tipoInmuebleCriteria = TipoInmuebleCriteria(setOf(Constantes.BUEN_ESTADO))
        estadoCriteria = EstadoCriteria(setOf(Constantes.CASA_CHALET))
        andCriteria = AndCriteria(tipoInmuebleCriteria, estadoCriteria)
        assertEquals(andCriteria.meetCriteria(listaInmuebles), listOf<Inmueble>())
    }
}