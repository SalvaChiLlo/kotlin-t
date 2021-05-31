package com.kotlin_t.trobify.persistencia

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Inmuebles",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("dni"),
            childColumns = arrayOf("dniPropietario"),
        )
    ),
    indices = arrayOf(Index(value = ["dniPropietario", "inmuebleId"], unique = true))
)
data class Inmueble(
    @PrimaryKey(autoGenerate = true) var inmuebleId: Int? = 0,
    var dniPropietario: String,
    var direccion: String?,
    var nuevoDesarrollo: Boolean?,
    var miniatura: Bitmap?,
    var URLminiatura: String?,
    var altura: Int?,
    var precio: Int?,
    var tipoDeInmueble: String?,
    var operacion: String?,
    var tamano: Int?,
    var exterior: Boolean?,
    var habitaciones: Int?,
    var banos: Int?,
    var provincia: String?,
    var municipio: String?,
    var barrio: String?,
    var pais: String?,
    var latitud: Double?,
    var longitud: Double?,
    var estado: String?,
    var tieneAscensor: Boolean?,
    var precioPorMetro: Int?,
    var titulo: String?,
    var subtitulo: String,
    var descripcion: String?,
    var codigoPostal: Int,
    var publicado: Boolean
)
