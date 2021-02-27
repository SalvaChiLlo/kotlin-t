package com.kotlin_t.trobify.Persistencia

import androidx.room.*
import com.kotlin_t.trobify.Logica.Inmueble
import com.kotlin_t.trobify.Logica.Usuario

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM Usuarios")
    fun getAll(): List<Usuario>

    @Query("SELECT * FROM Usuarios WHERE dni IN (:ids)")
    fun loadAllByIds(ids: Set<Int>): List<Usuario>

    @Query("SELECT * FROM Usuarios WHERE dni like :id")
    fun findById(id: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg usuarios: Usuario)

    @Delete
    fun delete(usuario: Usuario)

    @Query("DELETE FROM Usuarios")
    fun deleteAll()

    @Update
    fun update(usuario: Usuario)

    @Query("DELETE FROM Usuarios WHERE dni = :id")
    fun deleteById(id: String)
}