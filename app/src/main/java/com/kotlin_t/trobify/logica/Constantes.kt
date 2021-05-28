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

        fun formatoDniCorrecto(dni: String): Boolean {
            if (dni.length != 9) return false
            for (i in dni.indices) {
                if (i == 8) {
                    if (!Character.isLetter(dni[i])) {
                        return false
                    }
                } else if (!Character.isDigit(dni[i])) {
                    return false

                }
            }
            return true
        }

        fun formatoNombreApellidosCorrecto(nombreApellidos: String): Boolean {
            for (i in nombreApellidos.indices) {
                if (!Character.isLetter(nombreApellidos[i]) && nombreApellidos[i].toInt() != 32) {
                    return false;
                }
            }
            return true
        }
        fun formatoTelefonoCorrecto(telefono: String): Boolean{
            if(telefono.length != 9) return false
            for (i in telefono.indices) {
                if (!Character.isDigit(telefono[i])) {
                    return false
                }
            }
            return true
        }

        fun formatoContrasenaCorrecto(contrasena: String): Boolean{
            return contrasena.length >= 8
        }

        fun formatoUsuarioCorrecto(usuario: String): Boolean {
            for (i in usuario.indices) {
                if (usuario[i].toInt() == 32) {
                    return false
                }
            }
            return true
        }

        fun crearInmueble1(): Inmueble {
            return Inmueble(
                "12345678E",
                "direccion",
                true,
                null,
                null,
                1,
                112,
                ATICO,
                VENTA,
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
                BUEN_ESTADO,
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
                2,
                21,
                CASA_CHALET,
                ALQUILER,
                22,
                false,
                23,
                5,
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
                3,
                24,
                HABITACION,
                INTERCAMBIO_VIVIENDA,
                24,
                false,
                26,
                2,
                "Valencia",
                "Valencia",
                "Benimaclet",
                "España",
                1.2,
                1.2,
                REFORMAR,
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
                4,
                22,
                PISO,
                VENTA,
                432,
                false,
                432,
                1,
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
                5,
                52,
                ATICO,
                ALQUILER,
                21,
                false,
                22,
                3,
                "Valencia",
                "Valencia",
                "Benimaclet",
                "España",
                1.2,
                1.2,
                NUEVA_CONSTRUCCION,
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
                6,
                2333,
                CASA_CHALET,
                INTERCAMBIO_VIVIENDA,
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
                NUEVA_CONSTRUCCION,
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


}