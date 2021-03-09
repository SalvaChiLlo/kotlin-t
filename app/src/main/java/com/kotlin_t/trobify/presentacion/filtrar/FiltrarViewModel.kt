package com.kotlin_t.trobify.presentacion.filtrar

import android.app.Application
import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentFiltrarBinding
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

class FiltrarViewModel(val database: AppDatabase, application: Application, val binding: FragmentFiltrarBinding) :
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

        this.listaInmuebles = miBusqueda.meetCriteria(this.listaInmuebles)
        Log.e("EEEEEEE", this.listaInmuebles.toString())
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