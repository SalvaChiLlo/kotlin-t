package com.kotlin_t.trobify.logica.ordenacion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.persistencia.Inmueble
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.ordenacion.criteria.*

class OrdenacionViewModel(database: AppDatabase, application: Application, private val model: SharedViewModel, ) : AndroidViewModel(application) {
    private var inmuebles: List<Inmueble> = database.inmuebleDAO().getAll()

    fun elegirEstrategia(estrategia: String){
        when(estrategia) {
            "Precio más Alto" -> model.estrategiaOrdenacion = PrecioMasAlto()
            "Precio más Bajo" -> model.estrategiaOrdenacion = PrecioMasBajo()
            "Más Grande" -> model.estrategiaOrdenacion = MayorTamano()
            "Más Pequeño" -> model.estrategiaOrdenacion = MenorTamano()
            "Pisos Altos" -> model.estrategiaOrdenacion = PisosAltos()
            "Pisos Bajos" -> model.estrategiaOrdenacion = PisosBajos()
            "Más Baños" -> model.estrategiaOrdenacion = MasBanos()
            "Menos Baños" -> model.estrategiaOrdenacion = MenosBanos()
            "Más Habitaciones" -> model.estrategiaOrdenacion = MasHabitaciones()
            "Menos Habitaciones" -> model.estrategiaOrdenacion = MenosHabitaciones()
        }
    }

    fun eliminarEstrategia(){
        model.estrategiaOrdenacion = null
    }

    fun getEstrategiaOrdenacion(): String {
        return model.estrategiaOrdenacion.toString()
    }


}