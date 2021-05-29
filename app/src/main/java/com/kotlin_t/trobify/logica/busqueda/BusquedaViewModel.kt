package com.kotlin_t.trobify.logica.busqueda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Constantes
import com.kotlin_t.trobify.logica.ContextClass
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
    val contextClass: ContextClass,
    viewLifecycleOwner: LifecycleOwner
) : AndroidViewModel(application) {
    var listaBusquedas = database.busquedaDAO().getAll()
    var listaInmuebles = database.inmuebleDAO().getAll()
    var listaMunicipios = listaInmuebles.map { Busqueda(it.municipio!!) }.toSet()


    fun search(busqueda: String) {
        database.busquedaDAO().insertAll(Busqueda(busqueda))
        contextClass.busquedaString = busqueda
        filtrarInmuebles()
    }

    fun filtrarInmuebles() {
        val tipoDeOperacion = setOperacionCriteria()

        val tipoDeInmueble = setTipoInmuebleCriteria()

        var precioMin = -1
        var precioMax = 99999999
        val precio = setPrecioCriteria(precioMin, precioMax)

        val habitaciones = setNroHabitacionesCriteria()

        val banos = setNroBanosCriteria()

        val estado = setEstadoCriteria()

        val planta = setPlantaCriteria()

        val busqueda = BusquedaCriteria(contextClass.busquedaString)

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
        contextClass.setInmuebles(this.listaInmuebles)
    }

    private fun setPlantaCriteria() = if (contextClass.plantaOpciones.value!!.isEmpty()) {
        PlantaCriteria(
            setOf(
                Constantes.PLANTA_BAJA,
                Constantes.PLANTA_INTERMEDIA,
                Constantes.PLANTA_ALTA
            )
        )
    } else {
        PlantaCriteria(contextClass.plantaOpciones.value!!)
    }

    private fun setEstadoCriteria() = if (contextClass.estadoOpciones.value!!.isEmpty()) {
        EstadoCriteria(
            setOf(
                Constantes.NUEVA_CONSTRUCCION,
                Constantes.BUEN_ESTADO,
                Constantes.REFORMAR
            )
        )
    } else {
        EstadoCriteria(contextClass.estadoOpciones.value!!)
    }

    private fun setNroBanosCriteria() = if (contextClass.banosOpciones.value!!.isEmpty()) {
        NroBanosCriteria(
            setOf(
                Constantes.UNO,
                Constantes.DOS,
                Constantes.TRES,
                Constantes.CUATROoMAS
            )
        )
    } else {
        NroBanosCriteria(contextClass.banosOpciones.value!!)
    }

    private fun setNroHabitacionesCriteria() =
        if (contextClass.habitacionesOpciones.value!!.isEmpty()) {
            NroHabitacionesCriteria(
                setOf(
                    Constantes.UNO,
                    Constantes.DOS,
                    Constantes.TRES,
                    Constantes.CUATROoMAS
                )
            )
        } else {
            NroHabitacionesCriteria(contextClass.habitacionesOpciones.value!!)
        }

    private fun setPrecioCriteria(
        precioMin: Int,
        precioMax: Int
    ) = AndCriteria(
        PrecioMinimoCriteria(precioMin), PrecioMaximoCriteria(precioMax)
    )

    private fun setTipoInmuebleCriteria() = if (contextClass.tiposOpciones.value!!.isEmpty()) {
        TipoInmuebleCriteria(
            setOf(
                Constantes.ATICO,
                Constantes.CASA_CHALET,
                Constantes.HABITACION,
                Constantes.PISO
            )
        )
    } else {
        TipoInmuebleCriteria(contextClass.tiposOpciones.value!!)
    }

    private fun setOperacionCriteria(): OperacionCriteria {
        val tipoDeOperacion = if (contextClass.operacionesOpciones.value!!.isEmpty()) {
            OperacionCriteria(
                setOf(
                    Constantes.VENTA,
                    Constantes.ALQUILER,
                    Constantes.ALQUILER_HABITACION,
                    Constantes.INTERCAMBIO_VIVIENDA
                )
            )
        } else {
            OperacionCriteria(contextClass.operacionesOpciones.value!!)
        }
        return tipoDeOperacion
    }
}