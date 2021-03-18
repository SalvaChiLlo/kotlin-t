package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.*

@Dao
interface ClientesInmobiliariasDAO{
    @Query("SELECT * FROM ClientesInmobiliarias")
    fun getAll(): List<ClientesInmobiliarias>

    @Query("SELECT * FROM ClientesInmobiliarias WHERE inmobiliariaId like :inmobiliariaId")
    fun findByInmobiliaria(inmobiliariaId: Int): ClientesInmobiliarias

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: ClientesInmobiliarias)

    @Delete
    fun delete(cliente: ClientesInmobiliarias)

    @Query("DELETE FROM ClientesInmobiliarias")
    fun deleteAll()

    @Update
    fun update(cliente: ClientesInmobiliarias)

    @Query("DELETE FROM ClientesInmobiliarias WHERE dni = :dni")
    fun deleteById(dni: String)
}