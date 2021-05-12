package com.kotlin_t.trobify.presentacion.editorFicha

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.editorFicha.EditorFichaViewModel
import com.kotlin_t.trobify.persistencia.Foto

class ImageAdapter(
    val context: Context,
    val dataset: List<Foto>,
    val editorFichaViewModel: EditorFichaViewModel
) : RecyclerView.Adapter<ImageAdapter.FotoViewHolder>() {
    class FotoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.fotoEditor)
        val eliminar: FloatingActionButton = view.findViewById(R.id.eliminarFoto)
        val mainFoto: FloatingActionButton = view.findViewById(R.id.fotoPrincipal)
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
            var hasMain = false
            editorFichaViewModel.imagesList.value?.forEach {
                hasMain = it.main
            }
            Log.e("EEEE", hasMain.toString())
            if(!hasMain && !editorFichaViewModel.imagesList.value!!.isEmpty()) {
                editorFichaViewModel.imagesList.value?.first()?.main = true
                editorFichaViewModel.imagesList.value = editorFichaViewModel.imagesList.value
                Log.e("AAAA", editorFichaViewModel.imagesList.value?.first()?.main.toString())
            }
        }
        setMainIcon(dataset[position], holder)

        holder.mainFoto.setOnClickListener {
            editorFichaViewModel.imagesList.value?.forEach {
                it.main = false
            }
            editorFichaViewModel.imagesList.value = editorFichaViewModel.imagesList.value
            dataset[position].main = true
            setMainIcon(dataset[position], holder)
        }
    }

    private fun setMainIcon(foto: Foto, holder: FotoViewHolder) {
        if (foto.main) {
            holder.mainFoto.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else {
            holder.mainFoto.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }
    }


    override fun getItemCount(): Int = dataset.size
}