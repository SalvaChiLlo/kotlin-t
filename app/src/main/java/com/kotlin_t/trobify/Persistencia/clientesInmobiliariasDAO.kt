package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.*

@Dao
interface clientesInmobiliariasDAO{
    @Query("SELECT inmobiliariaId FROM clientesInmobiliarias WHERE dni IN (:dni)")
    fun getInmobiliarias(dni: Set<Int>): List<Inmobiliaria>

    @Query("SELECT dni FROM clientesInmobiliarias WHERE inmobiliariaId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Usuario>
    //incompleto a partir de aqui
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