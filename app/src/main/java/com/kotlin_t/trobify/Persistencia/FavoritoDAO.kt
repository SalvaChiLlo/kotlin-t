package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.Favorito
import com.kotlin_t.trobify.Logica.Inmueble

@Dao
interface FavoritoDAO {
    @Query("SELECT * FROM Favoritos")
    fun getAll(): List<Favorito>

    @Query("SELECT * FROM Favoritos WHERE inmuebleId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Inmueble>

    @Query("SELECT * FROM Favoritos WHERE inmuebleId like :id")
    fun findById(id: String): Favorito

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Favorito)

    @Delete
    fun delete(favorito: Favorito)

    @Query("DELETE FROM Favoritos")
    fun deleteAll()

    @Update
    fun update(favorito: Favorito)

    @Query("DELETE FROM Favoritos WHERE inmuebleId = :id")
    fun deleteById(id: String)
}