package com.kotlin_t.trobify.logica

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

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
    @PrimaryKey(autoGenerate = true) val inmobiliariaId: Int?,
    @ColumnInfo()var dni : String
) {
    var verificado: Boolean = false
}
