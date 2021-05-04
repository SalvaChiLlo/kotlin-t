package com.kotlin_t.trobify.persistencia

import androidx.room.*


@Dao
interface InmobiliariaDAO {
    @Query("SELECT * FROM Inmobiliarias")
    fun getAll(): List<Inmobiliaria>

    @Query("SELECT * FROM Inmobiliarias WHERE inmobiliariaId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Inmobiliaria>

    @Query("SELECT * FROM Inmobiliarias WHERE inmobiliariaId like :inmobiliariaId")
    fun findById(inmobiliariaId: String): Inmobiliaria

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Inmobiliaria)

    @Delete
    fun delete(favorito: Inmobiliaria)

    @Query("DELETE FROM Inmobiliarias")
    fun deleteAll()

    @Update
    fun update(favorito: Inmobiliaria)

    @Query("DELETE FROM Inmobiliarias WHERE inmobiliariaId = :inmobiliariaId")
    fun deleteById(inmobiliariaId: String)

}