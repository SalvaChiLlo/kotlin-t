package com.kotlin_t.trobify.logica

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
}