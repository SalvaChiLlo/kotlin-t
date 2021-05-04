package com.kotlin_t.trobify.persistencia

import androidx.room.Entity

@Entity(
    primaryKeys = ["busqueda"]
)
data class Busqueda(val busqueda: String)