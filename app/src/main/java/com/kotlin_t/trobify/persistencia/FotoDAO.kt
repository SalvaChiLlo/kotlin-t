package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.Foto

@Dao
interface FotoDAO {
    @Query("SELECT * FROM Fotos")
    fun getAll(): List<Foto>

    @Query("SELECT * FROM Fotos WHERE fotoId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Foto>

    @Query("SELECT * FROM Fotos WHERE fotoId like :fotoId")
    fun findById(fotoId: String): Foto

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Foto)

    @Delete
    fun delete(foto: Foto)

    @Query("DELETE FROM Fotos")
    fun deleteAll()

    @Update
    fun update(foto: Foto)

    @Query("DELETE FROM Fotos WHERE fotoId = :fotoId")
    fun deleteById(fotoId: String)
}