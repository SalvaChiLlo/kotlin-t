package com.kotlin_t.trobify.presentacion.filtrar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
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

    fun filtrarInmuebles() {

        // Tipo de Operacion
        val tipoDeOperacion = OperacionCriteria(operacionesOpciones)

        // Tipo de Inmueble
        val tipoDeInmueble = TipoInmuebleCriteria(tiposOpciones)

        // Precio
        val precio = AndCriteria(
            PrecioMinimoCriteria(preciosOpciones[0]),
            PrecioMaximoCriteria(preciosOpciones[1])
        )

        // Nro Habitaciones
        val habitaciones = OrCriteria(
            NroHabitacionesCriteria(1),
            OrCriteria(
                NroHabitacionesCriteria(2),
                OrCriteria(
                    NroHabitacionesCriteria(3),
                    NroHabitacionesCriteria(4)
                )
            )
        )

        // Nro Banos
        val banos = OrCriteria(
            NroBanosCriteria(1),
            OrCriteria(
                NroBanosCriteria(2),
                OrCriteria(
                    NroBanosCriteria(3),
                    NroBanosCriteria(4)
                )
            )
        )

        // Estado
        val estado = EstadoCriteria(estadoOpciones)

        // Planta
        val planta = OrCriteria(
            PlantaCriteria("baja"),
            OrCriteria(
                PlantaCriteria("intermedia"),
                PlantaCriteria("alta")
            )
        )

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
}