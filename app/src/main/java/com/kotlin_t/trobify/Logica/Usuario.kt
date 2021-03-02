package com.kotlin_t.trobify.Logica

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class Usuario(
    @PrimaryKey var dni: String,
    var username: String,
    var contrasena: String,
    var nombre: String,
    var apellidos: String,
    var telefono: String,
    var iban: String,
    var fotoPerfil: Bitmap?
) {
    var baneado: Boolean = false
    var esGerente: Boolean = false
    var esTecnico: Boolean = false
}
