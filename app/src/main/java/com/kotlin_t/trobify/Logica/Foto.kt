package com.kotlin_t.trobify.Logica

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = arrayOf("inmuebleId", "fotoId"), foreignKeys = arrayOf(
        ForeignKey(
            entity = Inmueble::class,
            parentColumns = arrayOf("inmuebleId"),
            childColumns = arrayOf("inmuebleId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ),
    tableName = "Fotos",

)
data class Foto(
    val inmuebleId: Int,
    val fotoId: Int,
    var imagen: Bitmap
)
