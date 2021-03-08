package com.kotlin_t.trobify.presentacion.filtrar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Estado.EstadoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.NroBanos.NroBanosCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.NroHabitaciones.NroHabitacionesCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Operacion.OperacionCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Operadores.AndCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Operadores.OrCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Planta.PlantaCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Precio.PrecioMaximoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.Precio.PrecioMinimoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.Criteria.TipoInmueble.TipoInmuebleCriteria

class FiltrarViewModel(val database: AppDatabase, application: Application) :
    AndroidViewModel(application) {
    var listaInmuebles = database.inmuebleDAO().getAll()
    var operacionesOpciones = mutableSetOf<String>()
    var tiposOpciones = mutableSetOf<String>()
    var preciosOpciones = IntArray(2)
    var habitacionesOpciones = mutableSetOf<Int>()
    var banosOpciones = mutableSetOf<Int>()
    var estadoOpciones = mutableSetOf<String>()
    var plantaOpciones = mutableSetOf<String>()

    init {
        inicializarOpciones()
    }

    fun filtrarInmuebles() {

        val tipoDeOperacion = OperacionCriteria(operacionesOpciones)
        val tipoDeInmueble = TipoInmuebleCriteria(tiposOpciones)
        val precio = AndCriteria(
            PrecioMinimoCriteria(preciosOpciones[0]), PrecioMaximoCriteria(preciosOpciones[1])
        )
        val habitaciones = NroHabitacionesCriteria(habitacionesOpciones)
        val banos = NroBanosCriteria(banosOpciones)
        val estado = EstadoCriteria(estadoOpciones)
        val planta = PlantaCriteria(plantaOpciones)

        val miBusqueda = AndCriteria(
            tipoDeOperacion,
            AndCriteria(
                tipoDeInmueble,
                AndCriteria(
                    precio,
                    AndCriteria(
                        habitaciones,
                        AndCriteria(
                            banos,
                            AndCriteria(
                                estado,
                                planta
                            )
                        )
                    )
                )
            )
        )

        this.listaInmuebles = miBusqueda.meetCriteria(this.listaInmuebles)
    }

    private fun inicializarOpciones() {
        operacionesOpciones.addAll(
            listOf(
                Constantes.VENTA,
                Constantes.ALQUILER,
                Constantes.ALQUILER_HABITACION,
                Constantes.INTERCAMBIO_VIVIENDA
            )
        )

        tiposOpciones.addAll(
            listOf(Constantes.ATICO, Constantes.CASA_CHALET, Constantes.HABITACION, Constantes.PISO)
        )

        preciosOpciones[0] = 0;
        preciosOpciones[1] = 999999999

        habitacionesOpciones.addAll(
            listOf(Constantes.UNO, Constantes.DOS, Constantes.TRES, Constantes.CUATROoMAS)
        )

        banosOpciones.addAll(
            listOf(Constantes.UNO, Constantes.DOS, Constantes.TRES, Constantes.CUATROoMAS)
        )

        estadoOpciones.addAll(
            listOf(Constantes.NUEVA_CONSTRUCCION, Constantes.BUEN_ESTADO, Constantes.REFORMAR)
        )

        plantaOpciones.addAll(
            listOf(Constantes.PLANTA_BAJA, Constantes.PLANTA_INTERMEDIA, Constantes.PLANTA_ALTA)
        )
    }

}