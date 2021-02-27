package com.kotlin_t.trobify.Logica

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class Usuario(
    @PrimaryKey var dni: String,
    var username: String,
    var contraseña: String,
    var nombre: String,
    var apellidos: String,
    var telefono: Double,
    var iban: String,
    var fotoPerfil: Bitmap,
    var baneado: Boolean,
    var esGerente: Boolean,
    var esTecnico: Boolean
)
