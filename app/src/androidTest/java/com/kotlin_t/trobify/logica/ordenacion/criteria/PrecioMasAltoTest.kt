package com.kotlin_t.trobify.logica.ordenacion.criteria

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class PrecioMasAltoTest {
    private val precioMasAlto = PrecioMasAlto()
    private lateinit var inmueble1: Inmueble
    private lateinit var inmueble2: Inmueble
    private lateinit var inmueble3: Inmueble
    private lateinit var inmueble4: Inmueble
    private lateinit var inmueble5: Inmueble
    private lateinit var inmueble6: Inmueble
    private lateinit var lista1: List<Inmueble>
    private lateinit var lista2: List<Inmueble>
    @Before
    fun setUp() {
        inmueble1 = Constantes.crearInmueble1()
        inmueble2 = Constantes.crearInmueble2()
        inmueble3 = Constantes.crearInmueble3()
        inmueble4 = Constantes.crearInmueble4()
        inmueble5 = Constantes.crearInmueble5()
        inmueble6 = Constantes.crearInmueble6()
        lista1 = listOf(inmueble1, inmueble2, inmueble3, inmueble4, inmueble5, inmueble6)
        lista2 = listOf(inmueble6, inmueble1, inmueble5, inmueble3, inmueble4, inmueble2)
    }

    @Test
    fun ordenar() {
        assertEquals(precioMasAlto.ordenar(lista1), lista2)
    }

    @Test
    fun testToString() {
        assertEquals("Precio más Alto", precioMasAlto.toString())
    }
}