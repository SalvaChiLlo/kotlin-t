package com.kotlin_t.trobify.presentacion.home

import android.content.Context
import android.util.Log


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.Inmueble


class HomeItemAdapter(private val context: Context, private val dataset: List<Inmueble>, private val homeViewModel: HomeViewModel): RecyclerView.Adapter<HomeItemAdapter.HomeItemViewHolder>(){
    lateinit var inmueble: Inmueble

    class HomeItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagen: ImageView = view.findViewById(R.id.home_imagen)
        val direccion : TextView = view.findViewById(R.id.home_direccion)
        val precioMes : TextView = view.findViewById(R.id.home_precio_mes)
        val favorito: ImageView = view.findViewById(R.id.favorito_icon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val layoutAdapter = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return HomeItemViewHolder(layoutAdapter)
    }


    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        inmueble = dataset[position]
        holder.imagen.setImageBitmap(inmueble.miniatura)
        holder.direccion.text = inmueble.direccion
        var type: Int
        if (inmueble.operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, dataset[position].precio)
        homeViewModel.setFavoriteIcon(inmueble, holder.favorito)

        holder.imagen.setOnClickListener{
            Log.e("INMUEBLE ID", inmueble.inmuebleId.toString())
            val action = HomeFragmentDirections.actionNavHomeToFichaFragment(inmueble.inmuebleId)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return  dataset.size
    }


}