package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.Contrato
import com.kotlin_t.trobify.Logica.Favorito
import com.kotlin_t.trobify.Logica.Inmueble

@Dao
interface ContratoDAO {
    @Query("SELECT * FROM Contratos")
    fun getAll(): List<Contrato>

    @Query("SELECT * FROM Contratos WHERE inmuebleId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Contrato>

    @Query("SELECT * FROM Contratos WHERE inmuebleId like :inmuebleId")
    fun findById(inmuebleId: String): Contrato

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Contrato)

    @Delete
    fun delete(favorito: Contrato)

    @Query("DELETE FROM Contratos")
    fun deleteAll()

    @Update
    fun update(favorito: Contrato)

    @Query("DELETE FROM Contratos WHERE inmuebleId = :inmuebleId")
    fun deleteById(inmuebleId: String)
}