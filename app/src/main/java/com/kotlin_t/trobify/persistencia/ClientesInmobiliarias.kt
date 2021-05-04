package com.kotlin_t.trobify.persistencia

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

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
    ),
    indices = arrayOf(Index(value = ["dni", "inmobiliariaId"], unique = true))
)
data class ClientesInmobiliarias(
    val inmobiliariaId: Int,
    val dni: String
)