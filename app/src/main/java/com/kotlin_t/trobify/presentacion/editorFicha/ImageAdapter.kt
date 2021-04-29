package com.kotlin_t.trobify.presentacion.editorFicha

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.presentacion.editorFicha.ObservableList.ExtendedList

class ImageAdapter(
    val context: Context,
    val dataset: ExtendedList<Foto>,
    val editorFichaViewModel: EditorFichaViewModel
) : RecyclerView.Adapter<ImageAdapter.FotoViewHolder>() {
    class FotoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.fotoEditor)
        val eliminar: FloatingActionButton = view.findViewById(R.id.eliminarFoto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FotoViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)

        return FotoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FotoViewHolder, position: Int) {
        holder.imagen.setImageBitmap(dataset[position].imagen)

        holder.eliminar.setOnClickListener {
            editorFichaViewModel.removeImageFromList(dataset[position])
        }
    }

    override fun getItemCount(): Int = dataset.size
}