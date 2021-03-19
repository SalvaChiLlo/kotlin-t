package com.kotlin_t.trobify.presentacion.ficha

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textview.MaterialTextView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import org.w3c.dom.Text
import kotlin.properties.Delegates

class FichaFragment : Fragment() {
    var inmuebleId by Delegates.notNull<Int>()

    companion object {
        fun newInstance() = FichaFragment()
    }

    private lateinit var fichaViewModel: FichaViewModel
    lateinit var datasource: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        val viewModelFactory =  FichaViewModelFactory(datasource, application)
        fichaViewModel = ViewModelProvider(this, viewModelFactory).get(FichaViewModel::class.java)
        arguments?.let {
            inmuebleId = it.getInt("InmuebleID")!!
        }
        fichaViewModel.setHouse(inmuebleId)
        return inflater.inflate(R.layout.ficha_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fichaViewModel = ViewModelProvider(this).get(FichaViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            inmuebleId = it.getInt("InmuebleID")!!
        }
        updateMedia(view)
        /*
        val inmueble =
            AppDatabase.getDatabase(requireContext()).inmuebleDAO().findById(inmuebleId.toString())
        //view.findViewById<ImageView>(R.id.fotoPortal).setImageBitmap(inmueble.miniatura)
        //view.findViewById<MaterialTextView>(R.id.textoTitulo).text = inmueble.titulo

        view.findViewById<TextView>(R.id.textoEstado).text = inmueble.estado
        view.findViewById<TextView>(R.id.textoDescripcion).text = inmueble.descripcion*/
    }


    fun updateMedia(container: View) {
        setText(container)
        setPhotos(container)
        setMap(container)
    }

    private fun setText(container: View) {
        //update de todos los textos
        container!!.findViewById<MaterialTextView>(R.id.textoTitulo).text = fichaViewModel.inmueble.titulo
        container!!.findViewById<TextView>(R.id.textoPrecio).text =
            if (fichaViewModel.inmueble.operacion == "alquiler")
                getString(R.string.precioMes, fichaViewModel.inmueble.precio)
            else
                getString(R.string.precio, fichaViewModel.inmueble.precio)
        container!!.findViewById<TextView>(R.id.textoEstado).text = fichaViewModel.inmueble.estado
        container!!.findViewById<TextView>(R.id.textoDescripcion).text = fichaViewModel.inmueble.descripcion
        container!!.findViewById<TextView>(R.id.textoUsuario).text = fichaViewModel.usuario.nombre + " " + fichaViewModel.usuario.apellidos
    }

    private fun setPhotos(container: View) {
        //foto de la casa y de foto de perfil
        container!!.findViewById<ImageView>(R.id.fotoPortal).setImageBitmap(fichaViewModel.inmueble.miniatura)
        if (fichaViewModel.usuario.fotoPerfil != null)
            container!!.findViewById<ImageView>(R.id.fotoUsuario).setImageBitmap(fichaViewModel.usuario.fotoPerfil)
    }

    private fun setMap(container: View) {
        //inicializamos el mapa en esta sección
        //todo
    }
}