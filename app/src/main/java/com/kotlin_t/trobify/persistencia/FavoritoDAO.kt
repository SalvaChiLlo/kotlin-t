package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.Favorito

@Dao
interface FavoritoDAO {
    @Query("SELECT * FROM Favoritos")
    fun getAll(): List<Favorito>

    @Query("SELECT * FROM Favoritos WHERE inmuebleId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Favorito>

    @Query("SELECT * FROM Favoritos WHERE inmuebleId like :inmuebleId")
    fun findById(inmuebleId: Int): Favorito?

    @Query("SELECT * FROM Favoritos WHERE dni like :dni")
    fun findByDNI(dni: String): List<Favorito>

    @Query("SELECT * FROM Favoritos WHERE inmuebleId like :inmuebleId and dni like :dni")
    fun findByIdandDni(inmuebleId: Int, dni: String): Favorito?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg inmuebles: Favorito)



    @Delete
    fun delete(favorito: Favorito)

    @Query("DELETE FROM Favoritos")
    fun deleteAll()

    @Update
    fun update(favorito: Favorito)

    @Query("DELETE FROM Favoritos WHERE inmuebleId = :inmuebleId")
    fun deleteById(inmuebleId: Int)

    @Query("DELETE FROM Favoritos WHERE primaryKey = :primaryKey")
    fun deleteByPK(primaryKey: Int)
}