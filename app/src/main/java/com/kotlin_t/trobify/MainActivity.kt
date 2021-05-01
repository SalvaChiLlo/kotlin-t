package com.kotlin_t.trobify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.database.PopulateDB
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.home.HomeFragmentDirections


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val nav_menu: Menu = navView.menu
        val loginButton = headerView.findViewById<Button>(R.id.iniciaSesionButton)
        if (loginButton != null) {
            loginButton.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(HomeFragmentDirections.actionNavHomeToLoginFragment())
            }
        }

        val usuarioObserver = Observer<Usuario> { usuario ->
            if (usuario == null) {

                nav_menu.findItem(R.id.nav_mi_cuenta).setVisible(false)
                nav_menu.findItem(R.id.nav_mi_cuenta)
                nav_menu.findItem(R.id.nav_mis_inmuebles).setVisible(false)
                headerView.findViewById<TextView>(R.id.nav_header_text).text =
                    "No estás identificado"

            } else {

                nav_menu.findItem(R.id.nav_mi_cuenta).setVisible(true)
                nav_menu.findItem(R.id.nav_mis_inmuebles).setVisible(true)
                headerView.findViewById<TextView>(R.id.nav_header_text).text =
                    "Bienvenido " + usuario.nombre
                headerView.findViewById<Button>(R.id.iniciaSesionButton).visibility = View.GONE


            }
        }

        sharedViewModel.usuarioActual.observe(this, usuarioObserver)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favoritos, R.id.nav_mi_cuenta, R.id.nav_mis_inmuebles
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val model = ViewModelProvider(this).get(SharedViewModel::class.java)
        model.preciosOpciones.value!!.set(
            0,
            AppDatabase.getDatabase(this).inmuebleDAO().getMinPrecio()
        )
        model.preciosOpciones.value!!.set(
            1,
            AppDatabase.getDatabase(this).inmuebleDAO().getMaxPrecio()
        )

        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    101
                )
            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.INTERNET
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.INTERNET),
                    101
                )
            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101
                )
            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        populate()
    }

    private fun populate() {
        val database = AppDatabase.getDatabase(this)
        var sharedViewModel: SharedViewModel =
            ViewModelProvider(this).get(SharedViewModel::class.java)
        Thread {

            if (database.usuarioDAO().getAll().isEmpty() || database.inmuebleDAO().getAll()
                    .isEmpty()
            ) {
                PopulateDB(database, this, sharedViewModel).populate()
            }

            this@MainActivity.runOnUiThread {
                sharedViewModel.inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
            }
        }.start()
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