package com.kotlin_t.trobify.presentacion.filtrar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Estado.EstadoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.NroBanos.NroBanosCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.NroHabitaciones.NroHabitacionesCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Operacion.OperacionCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Operadores.AndCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Planta.PlantaCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Precio.PrecioMaximoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Precio.PrecioMinimoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria

class FiltrarViewModel(val database: AppDatabase, application: Application, val model: SharedViewModel) :
    AndroidViewModel(application) {
    private var listaInmuebles = database.inmuebleDAO().getAll()
    private var operacionesOpciones = mutableSetOf<String>()
    private var tiposOpciones = mutableSetOf<String>()
    private var preciosOpciones = IntArray(2)
    private var habitacionesOpciones = mutableSetOf<Int>()
    private var banosOpciones = mutableSetOf<Int>()
    private var estadoOpciones = mutableSetOf<String>()
    private var plantaOpciones = mutableSetOf<String>()

    init {
        preciosOpciones[0] = database.inmuebleDAO().getMinPrecio()
        preciosOpciones[1] = database.inmuebleDAO().getMaxPrecio()
    }

    fun filtrarInmuebles() {

        val tipoDeOperacion = if (operacionesOpciones.isEmpty()) {
            OperacionCriteria(
                setOf(
                    Constantes.VENTA,
                    Constantes.ALQUILER,
                    Constantes.ALQUILER_HABITACION,
                    Constantes.INTERCAMBIO_VIVIENDA
                )
            )
        } else {
            OperacionCriteria(operacionesOpciones)
        }


        val tipoDeInmueble = if (tiposOpciones.isEmpty()) {
            TipoInmuebleCriteria(
                setOf(
                    Constantes.ATICO,
                    Constantes.CASA_CHALET,
                    Constantes.HABITACION,
                    Constantes.PISO
                )
            )
        } else {
            TipoInmuebleCriteria(tiposOpciones)
        }


        val precio = AndCriteria(
            PrecioMinimoCriteria(preciosOpciones[0]), PrecioMaximoCriteria(preciosOpciones[1])
        )


        val habitaciones = if (habitacionesOpciones.isEmpty()) {
            NroHabitacionesCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroHabitacionesCriteria(habitacionesOpciones)
        }


        val banos = if (banosOpciones.isEmpty()) {
            NroBanosCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroBanosCriteria(banosOpciones)
        }

        val estado = if (estadoOpciones.isEmpty()) {
            EstadoCriteria(
                setOf(
                    Constantes.NUEVA_CONSTRUCCION,
                    Constantes.BUEN_ESTADO,
                    Constantes.REFORMAR
                )
            )
        } else {
            EstadoCriteria(estadoOpciones)
        }

        val planta = if (plantaOpciones.isEmpty()) {
            PlantaCriteria(
                setOf(
                    Constantes.PLANTA_BAJA,
                    Constantes.PLANTA_INTERMEDIA,
                    Constantes.PLANTA_ALTA
                )
            )
        } else {
            PlantaCriteria(plantaOpciones)
        }


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

        this.listaInmuebles = miBusqueda.meetCriteria(database.inmuebleDAO().getAll())

        model.setInmuebles(this.listaInmuebles)
    }

    fun changeOperaciones(operacion: String, remove:Boolean) {
        if(!remove) {
            operacionesOpciones.remove(operacion)
        } else {
            operacionesOpciones.add(operacion)
        }
        filtrarInmuebles()
    }

    fun changeTipos(tipo: String, remove: Boolean) {
        if(!remove) {
            tiposOpciones.remove(tipo)
        } else {
            tiposOpciones.add(tipo)
        }
        filtrarInmuebles()
    }

    fun changePrecios(precio: Int, min: Boolean) {
        if(min){
            preciosOpciones[0] = precio
        } else {
            preciosOpciones[1] = precio
        }
        filtrarInmuebles()
    }

    fun changeHabitaciones(habitacion: Int, remove: Boolean) {
        if(!remove) {
            habitacionesOpciones.remove(habitacion)
        } else {
            habitacionesOpciones.add(habitacion)
        }
        filtrarInmuebles()
    }

    fun changeBanos(bano: Int, remove: Boolean) {
        if(!remove) {
            banosOpciones.remove(bano)
        } else {
            banosOpciones.add(bano)
        }
        filtrarInmuebles()
    }

    fun changeEstado(estado: String, remove: Boolean) {
        if(!remove) {
            estadoOpciones.remove(estado)
        } else {
            estadoOpciones.add(estado)
        }
        filtrarInmuebles()
    }

    fun changePlanta(planta: String, remove: Boolean) {
        if(!remove) {
            plantaOpciones.remove(planta)
        } else {
            plantaOpciones.add(planta)
        }
        filtrarInmuebles()
    }
}