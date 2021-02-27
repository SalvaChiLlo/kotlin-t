package com.kotlin_t.trobify.Logica

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Inmuebles",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("dni"),
            childColumns = arrayOf("dni"),
        )
    )

)
data class Inmueble(
    @PrimaryKey(autoGenerate = true) val inmuebleId: Int?,
    var dni: String,
    var direccion: String?,
    var nuevoDesarrollo: Boolean?,
    var miniatura: Bitmap?,
    var URLminiatura: String?,
    var altura: Int?,
    var precio: Int?,
    var tipoDeInmueble: String?,
    var operacion: String?,
    var tamaño: Int?,
    var exterior: Boolean?,
    var habitaciones: Int?,
    var baños: Int?,
    var provincia: String?,
    var municipio: String?,
    var barrio: String?,
    var pais: String?,
    var zona: String?,
    var latitud: Double?,
    var longitud: Double?,
    var estado: String?,
    var tieneAscensor: Boolean?,
    var precioPorMetro: Int?,
    var titulo: String?,
    var subtitulo: String,
    var descripcion: String?
)

