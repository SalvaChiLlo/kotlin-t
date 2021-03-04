package com.kotlin_t.trobify.Database

import android.content.Context
import androidx.room.*
import com.kotlin_t.trobify.Logica.*
import com.kotlin_t.trobify.Persistencia.*

@Database(
    entities = [
        Contrato::class,
        esCliente::class,
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