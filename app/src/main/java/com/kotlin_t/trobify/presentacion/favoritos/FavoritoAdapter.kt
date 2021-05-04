package com.kotlin_t.trobify.presentacion.favoritos

import ListaFavoritosFragmentDirections
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
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.persistencia.Favorito


class FavoritoAdapter(
    val context: Context,
    val dataset: List<Favorito>,
    val viewModel: ListaFavoritosViewModel,
    val database: AppDatabase
) :
    RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {
    class FavoritoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.home_imagen)
        val direccion: TextView = view.findViewById(R.id.home_direccion)
        val precioMes: TextView = view.findViewById(R.id.home_precio_mes)
        val favorito: ImageView = view.findViewById(R.id.favorito_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.home_item, parent, false)

        return FavoritoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        // Set image
        val inmueble = database.inmuebleDAO().findById(dataset[position].inmuebleId!!.toString())
        holder.imagen.setImageBitmap(inmueble.miniatura)
        holder.imagen.setOnClickListener{
            val action = ListaFavoritosFragmentDirections.actionNavFavoritosToFichaFragment(inmueble.inmuebleId)
            holder.itemView.findNavController().navigate(action)
        }
        // Set direccion
        holder.direccion.text = inmueble.direccion
        // Set precio
        var type: Int
        if (inmueble.operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, inmueble.precio)
        viewModel.checkIfFavorito(inmueble, holder.favorito)
        holder.favorito.setOnClickListener{
            viewModel.addOrRemoveFavorite(inmueble, null, holder.favorito)
        }

    }



    override fun getItemCount(): Int = dataset.size

}