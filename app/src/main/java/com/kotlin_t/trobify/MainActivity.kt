package com.kotlin_t.trobify

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.kotlin_t.trobify.persistencia.Usuario
import com.kotlin_t.trobify.logica.ContextClass
import com.kotlin_t.trobify.persistencia.Foto
import com.kotlin_t.trobify.presentacion.home.HomeFragmentDirections


class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var contextClass: ContextClass

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var headerView: View
    private lateinit var nav_menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        contextClass = ViewModelProvider(this).get(ContextClass::class.java)

        // Inicializar Variables
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        headerView = navView.getHeaderView(0)
        nav_menu = navView.menu


        setupNavController()
        inicializarSharedViewModel()
        crearListeners()
        crearObservers()
        preguntarPermisos()
        populate()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavController() {
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
    }

    private fun inicializarSharedViewModel() {
        val model = ViewModelProvider(this).get(ContextClass::class.java)
        model.preciosOpciones.value!!.set(
            0,
            AppDatabase.getDatabase(this).inmuebleDAO().getMinPrecio()
        )
        model.preciosOpciones.value!!.set(
            1,
            AppDatabase.getDatabase(this).inmuebleDAO().getMaxPrecio()
        )
    }

    private fun crearListeners() {
        // Botón Login
        headerView.findViewById<Button>(R.id.iniciaSesionButton)?.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(HomeFragmentDirections.actionNavHomeToLoginFragment())
        }

        // Botón Cerrar Sesión
        val cerrarSesion = nav_menu.findItem(R.id.cerrarSesion)
        val botonCerrarSesion = cerrarSesion.actionView.rootView.findViewById<Button>(R.id.botonCerrarSesion)
        botonCerrarSesion?.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun crearObservers() {

        val usuarioObserver = Observer<Usuario> { usuario ->

            val usuarioLogeado = (usuario != null)

            if (usuarioLogeado) {

                headerView.findViewById<Button>(R.id.iniciaSesionButton).visibility = View.GONE
                headerView.findViewById<TextView>(R.id.nav_header_text).visibility = View.GONE
                headerView.findViewById<TextView>(R.id.nav_header_bienvenido).visibility = View.VISIBLE
                headerView.findViewById<TextView>(R.id.nav_header_usuario).text = usuario.nombre.toUpperCase()
                headerView.findViewById<TextView>(R.id.nav_header_usuario).visibility = View.VISIBLE

                if(usuario.fotoPerfil == null) {
                    headerView.findViewById<ImageView>(R.id.nav_header_picture).setImageResource(R.drawable.anonymous_user)
                } else {
                    headerView.findViewById<ImageView>(R.id.nav_header_picture).setImageBitmap(usuario.fotoPerfil)
                }

            } else {
                headerView.findViewById<TextView>(R.id.nav_header_usuario).visibility = View.GONE
                headerView.findViewById<TextView>(R.id.nav_header_bienvenido).visibility = View.GONE
                headerView.findViewById<Button>(R.id.iniciaSesionButton).visibility = View.VISIBLE
                headerView.findViewById<TextView>(R.id.nav_header_text).visibility = View.VISIBLE

            }

            nav_menu.findItem(R.id.nav_mi_cuenta).setVisible(usuarioLogeado)
            nav_menu.findItem(R.id.nav_mis_inmuebles).setVisible(usuarioLogeado)
            nav_menu.findItem(R.id.cerrarSesion).setVisible(usuarioLogeado)

        }

        contextClass.usuarioActual.observe(this, usuarioObserver)

    }

    private fun preguntarPermisos() {
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
    }

    private fun populate() {

        val database = AppDatabase.getDatabase(this)

        Thread {

            if (database.usuarioDAO().getAll().isEmpty() || database.inmuebleDAO().getAll()
                    .isEmpty()
            ) {
                PopulateDB(database, this, contextClass).populate()

                this@MainActivity.runOnUiThread {
                    contextClass.inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
                }

                for(i in 1..20) {
                    PopulateDB(database, this, contextClass).createInmueble(i)
                    val ultimoInmueble = database.inmuebleDAO().getAllPublicAndNoPublic().last()
                    database.fotoDAO()
                        .insertAll(Foto(ultimoInmueble.inmuebleId!!, ultimoInmueble.miniatura!!, true))
                }
                this@MainActivity.runOnUiThread {
                    contextClass.inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
                }
            }
        }.start()
    }

    private fun cerrarSesion(){

        val application = requireNotNull(this).application
        val database = AppDatabase.getDatabase(application)

        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás Seguro de cerrar la Sesión?")
            .setPositiveButton("Si", DialogInterface.OnClickListener( fun(dialog : DialogInterface, _: Int){
                database.sesionActualDAO().deleteSesion()
                contextClass.favoritosEliminados.clear()
                contextClass.updateCurrentUser(null)
                headerView.findViewById<ImageView>(R.id.nav_header_picture).setImageResource(R.drawable.anonymous_user)
                findNavController(R.id.nav_host_fragment).navigate(HomeFragmentDirections.actionNavHomeSelf())
                dialog.cancel()
            }))
            .setNegativeButton("No", DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int){
                dialog.cancel()
            }))
            .setIcon(R.drawable.ic_baseline_warning_24)
            .show()
    }


}