package com.kotlin_t.trobify.presentacion.busqueda

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Busqueda
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.filtrar.FiltrarViewModel
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Busqueda.BusquedaCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Estado.EstadoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.NroBanos.NroBanosCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.NroHabitaciones.NroHabitacionesCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Operacion.OperacionCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Operadores.AndCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Planta.PlantaCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Precio.PrecioMaximoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.Precio.PrecioMinimoCriteria
import com.kotlin_t.trobify.presentacion.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria
import com.kotlin_t.trobify.presentacion.home.HomeFragmentDirections

class BusquedaViewModel(
    val database: AppDatabase,
    application: Application,
    val model: SharedViewModel,
    val viewLifecycleOwner: LifecycleOwner
): AndroidViewModel(application) {
    var listaBusquedas = database.busquedaDAO().getAll()
    var listaMunicipios = database.inmuebleDAO().getAll().map { Busqueda(it.municipio!!) }.toSet()

    var listaInmuebles = listOf<Inmueble>()

    init {
        model.inmuebles.observe(viewLifecycleOwner, {
            listaInmuebles = it
        })
    }


    fun search(busqueda: String) {
        database.busquedaDAO().insertAll(Busqueda(busqueda))
        model.busquedaString = busqueda
        filtrarInmuebles()
    }

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

        val busqueda = BusquedaCriteria(model.busquedaString)



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
                                AndCriteria(
                                    planta,
                                    busqueda
                                )
                            )
                        )
                    )
                )
            )
        )

        this.listaInmuebles = miBusqueda.meetCriteria(database.inmuebleDAO().getAll())

        model.setInmuebles(this.listaInmuebles)
    }
}