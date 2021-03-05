package com.kotlin_t.trobify.persistencia

import androidx.room.*
import com.kotlin_t.trobify.logica.Usuario

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM Usuarios")
    fun getAll(): List<Usuario>

    @Query("SELECT * FROM Usuarios WHERE dni IN (:dnis)")
    fun loadAllByIds(dnis: Set<Int>): List<Usuario>

    @Query("SELECT * FROM Usuarios WHERE dni like :dni")
    fun findById(dni: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg usuarios: Usuario)

    @Delete
    fun delete(usuario: Usuario)

    @Query("DELETE FROM Usuarios")
    fun deleteAll()

    @Update
    fun update(usuario: Usuario)

    @Query("DELETE FROM Usuarios WHERE dni = :dni")
    fun deleteById(dni: String)
}