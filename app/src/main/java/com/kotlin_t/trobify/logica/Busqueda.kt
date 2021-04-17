package com.kotlin_t.trobify.logica

import androidx.room.Entity

@Entity(
    primaryKeys = ["busqueda"]
)
data class Busqueda(val busqueda: String)