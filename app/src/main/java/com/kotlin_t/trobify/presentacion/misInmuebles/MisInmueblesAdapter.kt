package com.kotlin_t.trobify.presentacion.misInmuebles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.misInmuebles.MisInmueblesViewModel
import com.kotlin_t.trobify.persistencia.Inmueble

class MisInmueblesAdapter(
    val context: Context,
    val dataset: List<Inmueble>,
    val viewModel: MisInmueblesViewModel,
    val database: AppDatabase,
    val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<MisInmueblesAdapter.MisInmueblesViewHolder>() {
    class MisInmueblesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.home_imagen)
        val direccion: TextView = view.findViewById(R.id.home_direccion)
        val precioMes: TextView = view.findViewById(R.id.home_precio_mes)
        val favorito: ImageView = view.findViewById(R.id.favorito_icon)
        val publicar: FloatingActionButton = view.findViewById(R.id.publicarInmueble)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisInmueblesViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.home_item, parent, false)
        return MisInmueblesViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MisInmueblesViewHolder, position: Int) {
        // Set image
        checkEstadoInmueble(dataset[position],holder.publicar)
        val inmueble = database.inmuebleDAO().findById(dataset[position].inmuebleId!!.toString())
        holder.imagen.setImageBitmap(inmueble.miniatura)
        holder.imagen.setOnClickListener {
            holder.itemView.findNavController().navigate(
                MisInmueblesFragmentDirections.actionMisInmueblesFragmentToEditorFichaFragment(
                    inmueble.inmuebleId
                )
            )
        }
        // Set direccion
        holder.direccion.text = inmueble.direccion
        // Set precio
        var type: Int
        if (inmueble.operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, inmueble.precio)
        holder.favorito.visibility = View.GONE

        holder.publicar.setOnClickListener {
            checkEstadoInmueble(dataset[position], it as FloatingActionButton)
            sharedViewModel.inmuebles.value = database.inmuebleDAO().getAll().toMutableList()
        }
    }

    private fun checkEstadoInmueble(inmueble: Inmueble, button: FloatingActionButton) {
        if (inmueble.publicado) {
            inmueble.publicado = false
            button.setImageResource(R.drawable.ic_baseline_public_off_24)
            database.inmuebleDAO().update(inmueble)
        } else {
            inmueble.publicado = true
            button.setImageResource(R.drawable.ic_baseline_public_24)
            database.inmuebleDAO().update(inmueble)
        }
    }

    override fun getItemCount(): Int = dataset.size

}
