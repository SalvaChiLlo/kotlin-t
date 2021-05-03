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

    @Query("SELECT * FROM Usuarios WHERE username like :username")
    fun findByUsername(username: String): Usuario

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM Usuarios WHERE username like :nick) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    fun existsUsername(nick: String): Boolean

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM Usuarios WHERE dni like :dni) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    fun existsId(dni: String): Boolean

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