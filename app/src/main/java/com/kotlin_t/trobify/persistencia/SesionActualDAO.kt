package com.kotlin_t.trobify.persistencia

import androidx.room.*

@Dao
interface SesionActualDAO {

    @Query("SELECT * FROM SesionActual LIMIT 1")
    fun getSesionActual(): SesionActual?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSesion(actual: SesionActual)

    @Query("DELETE FROM SesionActual")
    fun deleteSesion()
}