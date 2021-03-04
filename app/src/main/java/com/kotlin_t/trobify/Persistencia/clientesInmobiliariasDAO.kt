package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.*

@Dao
interface clientesInmobiliariasDAO{
    @Query("SELECT inmobiliariaId FROM clientesInmobiliarias WHERE dni IN (:dni)")
    fun getInmobiliarias(dni: Set<Int>): List<Inmobiliaria>

    @Query("SELECT dni FROM clientesInmobiliarias WHERE inmobiliariaId IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Usuario>

    @Query("SELECT * FROM clientesInmobiliarias")
    fun getAll(): List<clientesInmobiliarias>
}