package com.kotlin_t.trobify.persistencia

import androidx.room.*

@Dao
interface InmuebleDAO {
    @Query("SELECT * FROM Inmuebles where publicado = 1")
    fun getAll(): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE inmuebleId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE dniPropietario = :dni")
    fun loadAllByPropietario(dni: String): List<Inmueble>

    @Query("SELECT * FROM Inmuebles WHERE inmuebleId like :inmuebleId")
    fun findById(inmuebleId: String): Inmueble

    @Query("SELECT max(precio) FROM Inmuebles")
    fun getMaxPrecio(): Int

    @Query("SELECT min(precio) FROM Inmuebles")
    fun getMinPrecio(): Int

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