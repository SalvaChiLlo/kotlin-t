package com.kotlin_t.trobify.persistencia

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Inmueble::class,
            parentColumns = arrayOf("inmuebleId"),
            childColumns = arrayOf("inmuebleId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ),
    tableName = "Fotos",
    indices = arrayOf(
        Index(value = ["inmuebleId"])
    )


)
data class Foto(
    @NonNull
    val inmuebleId: Int,
    var imagen: Bitmap
) {
    @PrimaryKey(autoGenerate = true) var fotoId: Int = 0
}
