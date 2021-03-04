package com.kotlin_t.trobify.logica

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Contratos",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Inmueble::class,
            parentColumns = arrayOf("inmuebleId"),
            childColumns = arrayOf("inmuebleId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("dni"),
            childColumns = arrayOf("dni"),
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    )
)
data class Contrato(
    @PrimaryKey val inmuebleId: Int,
    var dni: String,
    var fecha: String,
    var tipo: String,
    var precio: Double
)
