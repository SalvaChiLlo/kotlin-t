package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.Favorito
import com.kotlin_t.trobify.Logica.Inmueble
import com.kotlin_t.trobify.Logica.esCliente

@Dao
interface esClienteDAO {
    @Query("SELECT * FROM clientesInmobiliaria")
    fun getAll(): List<esCliente>

    @Query("SELECT * FROM clientesInmobiliaria WHERE inmobiliariaId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<esCliente>

    @Query("SELECT * FROM clientesInmobiliaria WHERE inmobiliariaId like :inmobiliariaId")
    fun findById(inmobiliariaId: String): esCliente

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: esCliente)

    @Delete
    fun delete(favorito: esCliente)

    @Query("DELETE FROM clientesInmobiliaria")
    fun deleteAll()

    @Update
    fun update(favorito: esCliente)

    @Query("DELETE FROM clientesInmobiliaria WHERE inmobiliariaId = :inmobiliariaId")
    fun deleteById(inmobiliariaId: String)
}