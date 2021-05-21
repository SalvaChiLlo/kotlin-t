package com.kotlin_t.trobify.logica.editorFicha

class Memento(
    private var operacion : Int,
    private var tipoInmueble : Int,
    private var precio: String,
    private var superficie: String,
    private var habitaciones : String,
    private var baños : String,
    private var planta : String,
    private var ascensor : Boolean,
    private var estado : Int,
    private var titulo : String,
    private var direccion : String,
    private var cp : String,
    private var descripcion : String
) {
    fun getOperacion() : Int { return operacion }
    fun getTipoInmueble() : Int { return tipoInmueble }
    fun getPrecio() : String { return precio }
    fun getSuperficie() : String { return superficie }
    fun getHabitaciones() : String { return habitaciones }
    fun getBaños() : String { return baños }
    fun getPlanta() : String { return planta }
    fun getAscensor() : Boolean {return ascensor }
    fun getEstado() : Int { return estado }
    fun getTitulo() : String { return titulo }
    fun getDireccion() : String { return direccion }
    fun getCP() : String { return cp }
    fun getDescripcion() : String { return descripcion }
}