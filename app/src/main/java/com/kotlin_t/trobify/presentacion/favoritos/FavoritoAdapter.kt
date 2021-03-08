package com.kotlin_t.trobify.presentacion.favoritos

import android.R.drawable
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import java.lang.Appendable


class FavoritoAdapter(
    val context: Context,
    val dataset: List<Inmueble>,
    val viewModel: ListaFavoritosViewModel
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
        holder.imagen.setImageBitmap(dataset[position].miniatura)
        // Set direccion
        holder.direccion.text = dataset[position].direccion
        // Set precio
        var type: Int
        if (dataset[position].operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, dataset[position].precio)
        viewModel.checkIfFavorito(dataset[position], holder.favorito)
        holder.favorito.setOnClickListener{
            viewModel.addOrRemoveFavorite(dataset[position], null, holder.favorito)
        }

    }



    override fun getItemCount(): Int = dataset.size

}