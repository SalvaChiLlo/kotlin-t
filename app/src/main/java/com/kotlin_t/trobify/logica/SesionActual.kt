package com.kotlin_t.trobify.logica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["usuario"]
)
data class SesionActual(val usuario: String, val fecha: String){

}
