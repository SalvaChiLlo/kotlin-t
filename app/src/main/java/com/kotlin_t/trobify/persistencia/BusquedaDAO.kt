package com.kotlin_t.trobify.persistencia

import androidx.room.*

@Dao
interface BusquedaDAO{
    @Query("SELECT * FROM Busqueda")
    fun getAll(): List<Busqueda>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg busquedas: Busqueda)
}