package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.Busqueda
import com.kotlin_t.trobify.logica.ClientesInmobiliarias

@Dao
interface BusquedaDAO{
    @Query("SELECT * FROM Busqueda")
    fun getAll(): List<Busqueda>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg busquedas: Busqueda)
}