package com.kotlin_t.trobify.Database

import android.content.Context
import androidx.room.*
import com.kotlin_t.trobify.Logica.*
import com.kotlin_t.trobify.Persistencia.*


@Database(
    entities = [
        Contrato::class,
        clientesInmobiliarias::class,
        Favorito::class,
        Foto::class,
        Inmobiliaria::class,
        Inmueble::class,
        Usuario::class
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contratoDAO(): ContratoDAO
    abstract fun esClienteDAO(): esClienteDAO
    abstract fun favoritoDAO(): FavoritoDAO
    abstract fun fotoDAO(): FotoDAO
    abstract fun inmobiliariaDAO(): InmobiliariaDAO
    abstract fun inmuebleDAO(): InmuebleDAO
    abstract fun usuarioDAO(): UsuarioDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context?): AppDatabase? {
            synchronized(this) {
                var instance = INSTANCE
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context!!.applicationContext,
                        AppDatabase::class.java, "userdatabase"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}