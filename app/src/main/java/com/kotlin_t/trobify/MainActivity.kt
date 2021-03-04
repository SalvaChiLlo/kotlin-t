package com.kotlin_t.trobify

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.kotlin_t.trobify.Database.AppDatabase
import com.kotlin_t.trobify.Logica.Favorito
import com.kotlin_t.trobify.Logica.Inmobiliaria
import com.kotlin_t.trobify.Logica.Inmueble
import com.kotlin_t.trobify.Logica.Usuario

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        database = AppDatabase.getDatabase(this)!!
        if (database.usuarioDAO().getAll().isEmpty()) {
            testDB()
        }
    }

    private fun testDB() {
        database.usuarioDAO()
            .insertAll(
                Usuario(
                    "12345678E",
                    "salva",
                    "1234567890",
                    "salva",
                    "chinesta",
                    "666999000",
                    "unIban",
                    null
                )
            )
        database.inmobiliariaDAO()
            .insertAll(
                Inmobiliaria(
                    null,
                    database.usuarioDAO().findById("12345678E").dni
                )
            )
        database.inmuebleDAO().insertAll(
            Inmueble(
                database.usuarioDAO().findById("12345678E").dni,
                "Dir",
                true,
                null,
                "someURL",
                12,
                123000,
                "Piso",
                "Venta",
                100,
                false,
                3,
                2,
                "Valencia",
                "Valenica",
                "Benimaclet",
                "España",
                1.2394322,
                35.1231234,
                "Nuevo",
                true,
                8,
                "Titulo",
                "Subotitulo",
                "Descripcion"
            )
        )
        database.favoritoDAO().insertAll(
            Favorito(
                database.inmuebleDAO().loadAllByPropietario("12345678E")[0].inmuebleId,
                database.usuarioDAO().findById("12345678E").dni
            )
        )
//        database.fotoDAO().insertAll()
//        database.esClienteDAO().insertAll()
//        database.contratoDAO().insertAll()

        Log.d("Contratos", database.contratoDAO().getAll().toString())
        Log.d("esCliente", database.esClienteDAO().getAll().toString())
        Log.d("Favoritos", database.favoritoDAO().getAll().toString())
        Log.d("Fotos", database.fotoDAO().getAll().toString())
        Log.d("Inmuebles", database.inmuebleDAO().getAll().toString())
        Log.d("Usuarios", database.usuarioDAO().getAll().toString())
        Log.d("Inmobiliarias", database.inmobiliariaDAO().getAll().toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}