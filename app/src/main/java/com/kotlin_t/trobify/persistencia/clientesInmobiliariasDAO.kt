package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.*

@Dao
interface clientesInmobiliariasDAO{
    @Query("SELECT * FROM clientesInmobiliarias")
    fun getAll(): List<clientesInmobiliarias>

    @Query("SELECT * FROM clientesInmobiliarias WHERE inmobiliariaId like :inmobiliariaId")
    fun findByInmobiliaria(inmobiliariaId: Int): clientesInmobiliarias

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: clientesInmobiliarias)

    @Delete
    fun delete(cliente: clientesInmobiliarias)

    @Query("DELETE FROM clientesInmobiliarias")
    fun deleteAll()

    @Update
    fun update(cliente: clientesInmobiliarias)

    @Query("DELETE FROM clientesInmobiliarias WHERE dni = :dni")
    fun deleteById(dni: String)
}