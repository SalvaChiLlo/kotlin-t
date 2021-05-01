package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.presentacion.editorFicha.EditorFichaViewModel
import com.kotlin_t.trobify.presentacion.editorFicha.ImageAdapter

class RecyclerViewObserver<E>(val recyclerView: RecyclerView, val context: Context, val editorFichaViewModel: EditorFichaViewModel): IObserver<E> {
    override fun update(value: MutableList<E>) {
        println("Observable has changed to: ${value.map{(it as Foto).inmuebleId}}")
        recyclerView.adapter = ImageAdapter(
            context,
            value as List<Foto>,
            editorFichaViewModel
        )
    }
}