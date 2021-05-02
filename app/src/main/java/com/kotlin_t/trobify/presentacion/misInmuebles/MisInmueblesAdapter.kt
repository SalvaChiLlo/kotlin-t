package com.kotlin_t.trobify.presentacion.misInmuebles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.presentacion.home.HomeFragmentDirections

class MisInmueblesAdapter(
    val context: Context,
    val dataset: List<Inmueble>,
    val viewModel: MisInmueblesViewModel,
    val database: AppDatabase
) :
    RecyclerView.Adapter<MisInmueblesAdapter.MisInmueblesViewHolder>() {
        class MisInmueblesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val imagen: ImageView = view.findViewById(R.id.home_imagen)
            val direccion: TextView = view.findViewById(R.id.home_direccion)
            val precioMes: TextView = view.findViewById(R.id.home_precio_mes)
            val favorito: ImageView = view.findViewById(R.id.favorito_icon)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisInmueblesViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.home_item, parent, false)
        return MisInmueblesViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MisInmueblesViewHolder, position: Int) {
        // Set image
        val inmueble = database.inmuebleDAO().findById(dataset[position].inmuebleId!!.toString())
        holder.imagen.setImageBitmap(inmueble.miniatura)
        holder.imagen.setOnClickListener{
            holder.itemView.findNavController().navigate(MisInmueblesFragmentDirections.actionNavMisInmueblesToEditorFichaFragment(inmueble.inmuebleId))
        }
        // Set direccion
        holder.direccion.text = inmueble.direccion
        // Set precio
        var type: Int
        if (inmueble.operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, inmueble.precio)
        holder.favorito.visibility = View.GONE
    }

    override fun getItemCount(): Int = dataset.size

    }
