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

class FiltrarViewModel(
    val database: AppDatabase,
    application: Application,
    val model: SharedViewModel
) :
    AndroidViewModel(application) {
    private var listaInmuebles = database.inmuebleDAO().getAll()

    fun filtrarInmuebles() {
        val tipoDeOperacion = if (model.operacionesOpciones.value!!.isEmpty()) {
            OperacionCriteria(
                setOf(
                    Constantes.VENTA,
                    Constantes.ALQUILER,
                    Constantes.ALQUILER_HABITACION,
                    Constantes.INTERCAMBIO_VIVIENDA
                )
            )
        } else {
            OperacionCriteria(model.operacionesOpciones.value!!)
        }


        val tipoDeInmueble = if (model.tiposOpciones.value!!.isEmpty()) {
            TipoInmuebleCriteria(
                setOf(
                    Constantes.ATICO,
                    Constantes.CASA_CHALET,
                    Constantes.HABITACION,
                    Constantes.PISO
                )
            )
        } else {
            TipoInmuebleCriteria(model.tiposOpciones.value!!)
        }


        val precio = AndCriteria(
            PrecioMinimoCriteria(model.preciosOpciones.value!![0]), PrecioMaximoCriteria(model.preciosOpciones.value!![1])
        )


        val habitaciones = if (model.habitacionesOpciones.value!!.isEmpty()) {
            NroHabitacionesCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroHabitacionesCriteria(model.habitacionesOpciones.value!!)
        }


        val banos = if (model.banosOpciones.value!!.isEmpty()) {
            NroBanosCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroBanosCriteria(model.banosOpciones.value!!)
        }

        val estado = if (model.estadoOpciones.value!!.isEmpty()) {
            EstadoCriteria(
                setOf(
                    Constantes.NUEVA_CONSTRUCCION,
                    Constantes.BUEN_ESTADO,
                    Constantes.REFORMAR
                )
            )
        } else {
            EstadoCriteria(model.estadoOpciones.value!!)
        }

        val planta = if (model.plantaOpciones.value!!.isEmpty()) {
            PlantaCriteria(
                setOf(
                    Constantes.PLANTA_BAJA,
                    Constantes.PLANTA_INTERMEDIA,
                    Constantes.PLANTA_ALTA
                )
            )
        } else {
            PlantaCriteria(model.plantaOpciones.value!!)
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

    fun changeOperaciones(operacion: String, remove: Boolean) {
        if (!remove) {
            model.operacionesOpciones.value!!.remove(operacion)
        } else {
            model.operacionesOpciones.value!!.add(operacion)
        }
    }

    fun changeTipos(tipo: String, remove: Boolean) {
        if (!remove) {
            model.tiposOpciones.value!!.remove(tipo)
        } else {
            model.tiposOpciones.value!!.add(tipo)
        }
    }

    fun changePrecios(precio: Int, min: Boolean) {
        if (min) {
            model.preciosOpciones.value!![0] = precio
        } else {
            model.preciosOpciones.value!![1] = precio
        }
    }

    fun changeHabitaciones(habitacion: Int, remove: Boolean) {
        if (!remove) {
            model.habitacionesOpciones.value!!.remove(habitacion)
        } else {
            model.habitacionesOpciones.value!!.add(habitacion)
        }
    }

    fun changeBanos(bano: Int, remove: Boolean) {
        if (!remove) {
            model.banosOpciones.value!!.remove(bano)
        } else {
            model.banosOpciones.value!!.add(bano)
        }
    }

    fun changeEstado(estado: String, remove: Boolean) {
        if (!remove) {
            model.estadoOpciones.value!!.remove(estado)
        } else {
            model.estadoOpciones.value!!.add(estado)
        }
    }

    fun changePlanta(planta: String, remove: Boolean) {
        if (!remove) {
            model.plantaOpciones.value!!.remove(planta)
        } else {
            model.plantaOpciones.value!!.add(planta)
        }
    }
}