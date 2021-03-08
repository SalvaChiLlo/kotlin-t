package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.Inmueble

@Dao
interface InmuebleDAO {
    @Query("SELECT * FROM Inmuebles")
    fun getAll(): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE inmuebleId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE dniPropietario = :dni")
    fun loadAllByPropietario(dni: String): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE inmuebleId like :inmuebleId")
    fun findById(inmuebleId: String): Inmueble

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Inmueble)

    @Delete
    fun delete(inmueble: Inmueble)

    @Query("DELETE FROM Inmuebles")
    fun deleteAll()

    @Update
    fun update(inmueble: Inmueble)

    @Query("DELETE FROM Inmuebles WHERE inmuebleId = :inmuebleId")
    fun deleteById(inmuebleId: String)
}