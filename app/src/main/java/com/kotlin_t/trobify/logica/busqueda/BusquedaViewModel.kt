package com.kotlin_t.trobify.logica.busqueda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.filtrar.criteria.Busqueda.BusquedaCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Estado.EstadoCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.NroBanos.NroBanosCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.NroHabitaciones.NroHabitacionesCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Operacion.OperacionCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Operadores.AndCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Planta.PlantaCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Precio.PrecioMaximoCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.Precio.PrecioMinimoCriteria
import com.kotlin_t.trobify.logica.filtrar.criteria.TipoInmueble.TipoInmuebleCriteria
import com.kotlin_t.trobify.persistencia.Busqueda

class BusquedaViewModel(
    val database: AppDatabase,
    application: Application,
    val sharedViewModel: SharedViewModel,
    viewLifecycleOwner: LifecycleOwner
): AndroidViewModel(application) {
    var listaBusquedas = database.busquedaDAO().getAll()
    var listaInmuebles = database.inmuebleDAO().getAll()
    var listaMunicipios = listaInmuebles.map { Busqueda(it.municipio!!) }.toSet()


    fun search(busqueda: String) {
        database.busquedaDAO().insertAll(Busqueda(busqueda))
        sharedViewModel.busquedaString = busqueda
        filtrarInmuebles()
    }

    fun filtrarInmuebles() {
        val tipoDeOperacion = if (sharedViewModel.operacionesOpciones.value!!.isEmpty()) {
            OperacionCriteria(
                setOf(
                    Constantes.VENTA,
                    Constantes.ALQUILER,
                    Constantes.ALQUILER_HABITACION,
                    Constantes.INTERCAMBIO_VIVIENDA
                )
            )
        } else {
            OperacionCriteria(sharedViewModel.operacionesOpciones.value!!)
        }

        val tipoDeInmueble = if (sharedViewModel.tiposOpciones.value!!.isEmpty()) {
            TipoInmuebleCriteria(
                setOf(
                    Constantes.ATICO,
                    Constantes.CASA_CHALET,
                    Constantes.HABITACION,
                    Constantes.PISO
                )
            )
        } else {
            TipoInmuebleCriteria(sharedViewModel.tiposOpciones.value!!)
        }

        var precioMin = -1
        var precioMax = 99999999
        val precio = AndCriteria(
            PrecioMinimoCriteria(precioMin), PrecioMaximoCriteria(precioMax)
        )

        val habitaciones = if (sharedViewModel.habitacionesOpciones.value!!.isEmpty()) {
            NroHabitacionesCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroHabitacionesCriteria(sharedViewModel.habitacionesOpciones.value!!)
        }

        val banos = if (sharedViewModel.banosOpciones.value!!.isEmpty()) {
            NroBanosCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroBanosCriteria(sharedViewModel.banosOpciones.value!!)
        }

        val estado = if (sharedViewModel.estadoOpciones.value!!.isEmpty()) {
            EstadoCriteria(
                setOf(
                    Constantes.NUEVA_CONSTRUCCION,
                    Constantes.BUEN_ESTADO,
                    Constantes.REFORMAR
                )
            )
        } else {
            EstadoCriteria(sharedViewModel.estadoOpciones.value!!)
        }

        val planta = if (sharedViewModel.plantaOpciones.value!!.isEmpty()) {
            PlantaCriteria(
                setOf(
                    Constantes.PLANTA_BAJA,
                    Constantes.PLANTA_INTERMEDIA,
                    Constantes.PLANTA_ALTA
                )
            )
        } else {
            PlantaCriteria(sharedViewModel.plantaOpciones.value!!)
        }

        val busqueda = BusquedaCriteria(sharedViewModel.busquedaString)

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
        sharedViewModel.setInmuebles(this.listaInmuebles)
    }
}