package com.kotlin_t.trobify.persistencia

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

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
    tableName = "Favoritos",
)
data class Favorito(
    val inmuebleId: Int,
    val dni: String?
) {
    @PrimaryKey(autoGenerate = true) var primaryKey: Int = 0
}
