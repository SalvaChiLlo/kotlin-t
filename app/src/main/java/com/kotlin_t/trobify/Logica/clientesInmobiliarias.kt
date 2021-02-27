package com.kotlin_t.trobify.Logica

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = arrayOf("inmobiliariaId", "dni"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Inmobiliaria::class,
            parentColumns = arrayOf("inmobiliariaId"),
            childColumns = arrayOf("inmobiliariaId"),
            onUpdate = CASCADE,
            onDelete = CASCADE
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
data class clientesInmobiliarias(
    val inmobiliariaId: Int,
    val dni: String
)