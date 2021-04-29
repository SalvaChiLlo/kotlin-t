package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.presentacion.editorFicha.EditorFichaViewModel
import com.kotlin_t.trobify.presentacion.editorFicha.ImageAdapter

class MyObserver<E>(val observable: ObservableList<E>, val recyclerView: RecyclerView, val context: Context, val editorFichaViewModel: EditorFichaViewModel): IObserver {
    override fun update() {
        println("Observable has changed to: ${observable.value.map{(it as Foto).inmuebleId}}")
        recyclerView.adapter = ImageAdapter(
            context,
            observable.value as ExtendedList<Foto>,
            editorFichaViewModel
        )
    }
}