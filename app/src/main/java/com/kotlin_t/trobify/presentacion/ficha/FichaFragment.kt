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
import kotlin.properties.Delegates

class FichaFragment : Fragment() {
    var inmuebleId by Delegates.notNull<Int>()

    companion object {
        fun newInstance() = FichaFragment()
    }

    private lateinit var viewModel: FichaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ficha_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(FichaViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            inmuebleId = it.getInt("InmuebleID")!!
        }
        val inmueble =
            AppDatabase.getDatabase(requireContext()).inmuebleDAO().findById(inmuebleId.toString())
        view.findViewById<ImageView>(R.id.fotoPortal).setImageBitmap(inmueble.miniatura)
        view.findViewById<MaterialTextView>(R.id.textoTitulo).text = inmueble.titulo
        view.findViewById<TextView>(R.id.textoPrecio).text =
            if (inmueble.operacion == "alquiler")
                getString(R.string.precioMes, inmueble.precio)
            else
                getString(R.string.precio, inmueble.precio)
        view.findViewById<TextView>(R.id.textoEstado).text = inmueble.estado
        view.findViewById<TextView>(R.id.textoDescripcion).text = inmueble.descripcion
    }

}