package com.kotlin_t.trobify.presentacion.favoritos

import android.content.Context
import android.service.autofill.Dataset
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Favorito
import com.kotlin_t.trobify.logica.Inmueble
import org.w3c.dom.Text

class FavoritoAdapter(val context: Context, val dataset: List<Favorito>) :
    RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {
    class FavoritoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textFav: TextView = view.findViewById(R.id.title_fav)
        val imageFav: ImageView = view.findViewById(R.id.miniatura_fav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val layoutInflater = LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false)

        return FavoritoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        holder.textFav.text = dataset[position].dni
    }

    override fun getItemCount(): Int {
        Log.e("EEEEEE", dataset.toString())
        return dataset.size
    }

}