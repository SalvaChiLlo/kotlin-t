package com.kotlin_t.trobify.Database

import android.content.Context
import androidx.room.*
import com.kotlin_t.trobify.Logica.Favorito
import com.kotlin_t.trobify.Logica.Inmueble
import com.kotlin_t.trobify.Logica.Usuario
import com.kotlin_t.trobify.Persistencia.FavoritoDAO
import com.kotlin_t.trobify.Persistencia.FotoDAO
import com.kotlin_t.trobify.Persistencia.InmuebleDAO
import com.kotlin_t.trobify.Persistencia.UsuarioDAO

@Database(entities = [Inmueble::class], version = 0)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inmuebleDAO(): InmuebleDAO?
    abstract fun fotoDAO(): FotoDAO?
    abstract fun usuarioDAO(): UsuarioDAO?
    abstract fun favoritoDAO(): FavoritoDAO?

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context?): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context!!,
                    AppDatabase::class.java, "userdatabase"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}