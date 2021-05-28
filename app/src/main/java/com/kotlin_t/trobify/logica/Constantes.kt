package com.kotlin_t.trobify.logica

import com.kotlin_t.trobify.persistencia.Inmueble
import kotlin.random.Random

class Constantes {
    companion object {
        // Tipos de Operaciones
        val VENTA = "venta"
        val ALQUILER = "alquiler"
        val ALQUILER_HABITACION = "alquiler de habitacion"
        val INTERCAMBIO_VIVIENDA = "intercambio de viviendas"

        // Tipos de inmuebles
        val ATICO = "atico"
        val CASA_CHALET = "casa chalet"
        val HABITACION = "habitacion"
        val PISO = "piso"

        // Cantidades
        val UNO = 1
        val DOS = 2
        val TRES = 3
        val CUATROoMAS = 4

        // Estado
        val NUEVA_CONSTRUCCION = "nueva construccion"
        val BUEN_ESTADO = "buen estado"
        val REFORMAR = "a reformar"

        // Planta
        val PLANTA_BAJA = "planta baja"
        val PLANTA_INTERMEDIA = "planta intermedia"
        val PLANTA_ALTA = "planta alta"

        fun loadCriterios(): List<String> {
            return listOf(
                "Precio más Alto",
                "Precio más Bajo",
                "Más Grande",
                "Más Pequeño",
                "Pisos Altos",
                "Pisos Bajos",
                "Más Baños",
                "Menos Baños",
                "Más Habitaciones",
                "Menos Habitaciones"
            )
        }
    }

    fun crearInmueble1(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            10,
            112,
            "atico",
            "venta",
            121,
            false,
            13,
            7,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.1,
            1.1,
            "buen estado",
            true,
            13,
            "direccion",
            "Subtitulo",
            "",
            46000,
            true
        )
    }

    fun crearInmueble2(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            20,
            21,
            "atico",
            "venta",
            22,
            false,
            23,
            52,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.2,
            1.2,
            "buen estado",
            true,
            2,
            "direccion",
            "Subtitulo",
            "",
            46002,
            true
        )
    }
    fun crearInmueble3(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            12,
            24,
            "atico",
            "venta",
            24,
            false,
            26,
            23,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.2,
            1.2,
            "buen estado",
            true,
            2,
            "direccion",
            "Subtitulo",
            "",
            46002,
            true
        )
    }
    fun crearInmueble4(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            122,
            22,
            "atico",
            "venta",
            432,
            false,
            432,
            12,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.2,
            1.2,
            "buen estado",
            true,
            2,
            "direccion",
            "Subtitulo",
            "",
            46002,
            true
        )
    }
    fun crearInmueble5(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            12,
            52,
            "atico",
            "venta",
            21,
            false,
            22,
            26,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.2,
            1.2,
            "buen estado",
            true,
            2,
            "direccion",
            "Subtitulo",
            "",
            46002,
            true
        )
    }
    fun crearInmueble6(): Inmueble {
        return Inmueble(
            "12345678E",
            "direccion",
            true,
            null,
            null,
            423,
            2333,
            "atico",
            "venta",
            212,
            false,
            642,
            1212,
            "Valencia",
            "Valencia",
            "Benimaclet",
            "España",
            1.2,
            1.2,
            "buen estado",
            true,
            1232,
            "direccion",
            "Subtitulo",
            "",
            46002,
            true
        )
    }
}