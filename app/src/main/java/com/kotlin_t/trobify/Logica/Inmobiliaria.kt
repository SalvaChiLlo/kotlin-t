package com.kotlin_t.trobify.Logica

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Inmobiliarias",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("dni"),
            childColumns = arrayOf("dni"),
            onDelete = CASCADE,
            onUpdate = CASCADE

        )
    )
)
data class Inmobiliaria(
    @PrimaryKey(autoGenerate = true) val inmobiliariaId: Int,
    var dni : String,
    var verificado: Boolean
)
