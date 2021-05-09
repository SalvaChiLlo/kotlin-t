package com.kotlin_t.trobify.presentacion.favoritos


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.logica.recuperarFavoritos.RecuperarFavoritosViewModel
import com.kotlin_t.trobify.persistencia.Favorito
import com.kotlin_t.trobify.presentacion.recuperarFavoritos.RecuperarFavoritosFragmentDirections


class FavoritoAdapter(
    val context: Context,
    val dataset: List<Favorito>,
    val favoritosViewModel: ListaFavoritosViewModel?,
    val recuperarFavoritosViewModel: RecuperarFavoritosViewModel?,
    val database: AppDatabase,
    val sharedViewModel: SharedViewModel,
    val recuperarFavorito: Boolean

) :
    RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {
    class FavoritoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.miniatura_fav)
        val direccion: TextView = view.findViewById(R.id.direcciontext)
        val precioMes: TextView = view.findViewById(R.id.preciotext)
        val favorito: ImageView = view.findViewById(R.id.favoicon)
        val recuperar: ImageView = view.findViewById(R.id.recuperarFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false)

        return FavoritoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        // Set image
        val inmueble = database.inmuebleDAO().findById(dataset[position].inmuebleId!!.toString())
        holder.imagen.setImageBitmap(inmueble.miniatura)

        // Set direccion
        holder.direccion.text = inmueble.direccion
        // Set precio
        val type: Int
        if (inmueble.operacion == "alquiler") type = R.string.precioMes
        else type = R.string.precio
        holder.precioMes.text = context.getString(type, inmueble.precio)
        var action: NavDirections
        if (recuperarFavorito) {
            action =
                RecuperarFavoritosFragmentDirections.actionRecuperarFavoritosFragmentToFichaFragment(inmueble.inmuebleId)
            holder.recuperar.visibility = View.VISIBLE
            holder.favorito.visibility = View.GONE
            holder.recuperar.setOnClickListener {
                recuperarFavoritosViewModel!!.recuperarFavorito(dataset[position])
            }
        } else {
            action = ListaFavoritosFragmentDirections.actionNavFavoritosToFichaFragment(inmueble.inmuebleId)

            holder.recuperar.visibility = View.GONE
            holder.favorito.visibility = View.VISIBLE
            favoritosViewModel!!.checkIfFavorito(dataset[position], holder.favorito)
            holder.favorito.setOnClickListener {
                favoritosViewModel.addOrRemoveFavorite(
                    dataset[position],
                    sharedViewModel.usuarioActual.value?.dni,
                    holder.favorito
                )
            }
        }
        holder.imagen.setOnClickListener {
            holder.itemView.findNavController().navigate(action)

        }


    }


    override fun getItemCount(): Int = dataset.size

}