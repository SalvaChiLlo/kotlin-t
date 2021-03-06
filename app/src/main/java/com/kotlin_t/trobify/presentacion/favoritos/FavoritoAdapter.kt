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


class FavoritoAdapter(val context: Context, val dataset: List<Inmueble>) :
    RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {
    class FavoritoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textDir: TextView = view.findViewById(R.id.direcciontext)
        val textPrice: TextView = view.findViewById(R.id.preciotext)
        val imageFav: ImageView = view.findViewById(R.id.miniatura_fav)
        val favicon: ImageView = view.findViewById(R.id.favoicon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false)

        return FavoritoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        val database = AppDatabase.getDatabase(context)
        // Set image
        holder.imageFav.setImageResource(R.drawable.ic_launcher_background)
        // Set direccion
        holder.textDir.text = dataset[position].direccion
        // Set precio
        holder.textPrice.text =
            if (dataset[position].operacion == "alquiler")
                "${dataset[position].precio}€/mes"
            else
                "${dataset[position].precio}€"

        // Set FavButton Icon
        holder.favicon.setImageResource(R.drawable.ic_baseline_favorite_24)
        // FavButton setOnClickListener
        holder.favicon.setOnClickListener {
            Toast.makeText(context, "Called Favorito", Toast.LENGTH_LONG).show()

            val search = database.favoritoDAO().findById(dataset[position].inmuebleId.toString())
            Log.e("FAVVV", search.toString())
            if (search == null) {
                holder.favicon.setImageResource(R.drawable.ic_baseline_favorite_24)
                database.favoritoDAO().insertAll(Favorito(dataset[position].inmuebleId, "-1"))
            } else {
                holder.favicon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                database.favoritoDAO().delete(Favorito(dataset[position].inmuebleId, "-1"))
            }

        }
    }

    override fun getItemCount(): Int = dataset.size

}