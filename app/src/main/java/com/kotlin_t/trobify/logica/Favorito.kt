package com.kotlin_t.trobify.logica

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Inmueble::class,
            parentColumns = arrayOf("inmuebleId"),
            childColumns = arrayOf("inmuebleId"),
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
    indices = arrayOf(Index("dni")),
    tableName = "Favoritos",
    primaryKeys = ["inmuebleId", "dni"]
)
data class Favorito(
    val inmuebleId: Int,
    val dni: String
)
