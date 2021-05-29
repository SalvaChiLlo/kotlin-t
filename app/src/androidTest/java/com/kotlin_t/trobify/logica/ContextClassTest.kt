package com.kotlin_t.trobify.logica

import androidx.lifecycle.ViewModelProvider
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ContextClassTest {
    @Test
    fun formatoUsuarioCorrecto() {
        assertTrue(Constantes.formatoUsuarioCorrecto("delaOssa"))
    }

    @Test
    fun formatoDniCorrecto() {
        assertTrue(Constantes.formatoDniCorrecto("12345678G"))
    }

    @Test
    fun formatoNombreApellidosCorrecto() {
        assertTrue(Constantes.formatoNombreApellidosCorrecto("Pablo"))
    }

    @Test
    fun formatoTelefonoCorrecto() {
        assertTrue(Constantes.formatoTelefonoCorrecto("644257750"))
    }

    @Test
    fun formatoContrasenaCorrecto() {
        assertTrue(Constantes.formatoContrasenaCorrecto("xdfsfgedffgds"))
    }

    @Test
    fun formatoUsuarioIncorrecto() {
        assertFalse(Constantes.formatoUsuarioCorrecto("King Pablo"))
    }

    @Test
    fun formatoDniCorto() {
        assertFalse(Constantes.formatoDniCorrecto("1234567G"))
    }

    @Test
    fun formatoDniSinLetra() {
        assertFalse(Constantes.formatoDniCorrecto("123456789"))
    }

    @Test
    fun formatoDniConCaracterEspecial() {
        assertFalse(Constantes.formatoDniCorrecto("1234567*G"))
    }

    @Test
    fun formatoNombreApellidosIncorrecto() {
        assertFalse(Constantes.formatoNombreApellidosCorrecto("Pa*lo"))
    }

    @Test
    fun formatoTelefonoCorto() {
        assertFalse(Constantes.formatoTelefonoCorrecto("64425775"))
    }
    @Test
    fun formatoTelefonoConLetras() {
        assertFalse(Constantes.formatoTelefonoCorrecto("6442577e4"))
    }

    @Test
    fun formatoContrasenaIncorrecto() {
        assertFalse(Constantes.formatoContrasenaCorrecto("xdf"))
    }
}