package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.Inmueble

@Dao
interface InmuebleDAO {
    @Query("SELECT * FROM inmueble")
    fun getAll(): List<Inmueble>

    @Query("SELECT * FROM inmueble WHERE id IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Inmueble>

    @Query("SELECT * FROM inmueble WHERE id like :id")
    fun findById(id: String): Inmueble

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Inmueble)

    @Delete
    fun delete(inmueble: Inmueble)

    @Query("DELETE FROM inmueble")
    fun deleteAll()

    @Update
    fun update(inmueble: Inmueble)

    @Query("DELETE FROM inmueble WHERE id = :id")
    fun deleteById(id: String)
}