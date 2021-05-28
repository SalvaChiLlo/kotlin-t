package com.kotlin_t.trobify.logica.ordenacion.criteria

import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.persistencia.Inmueble
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class PrecioMasBajoTest {
    private val precioMasBajo = PrecioMasBajo()
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
    }

    @Test
    fun ordenar() {
        assertEquals()
    }

    @Test
    fun testToString() {
        assertEquals("Precio más Bajo", precioMasBajo.toString())
    }
}