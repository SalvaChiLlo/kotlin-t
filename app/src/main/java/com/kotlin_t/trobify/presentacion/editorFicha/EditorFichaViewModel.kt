package com.kotlin_t.trobify.presentacion.editorFicha

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.editorFicha.ObservableList.ObservableList

class EditorFichaViewModel(
    val database: AppDatabase,
    application: Application,
    val model: SharedViewModel,
) : AndroidViewModel(application) {
    var inmueble: Inmueble? = null
    var inmuebleID: Int? = null
    var imagesList = ObservableList<Foto>()

    init {
        if (inmueble != null) {
            inmuebleID = inmueble!!.inmuebleId
        } else {
            inmuebleID = database.inmuebleDAO().getAll().last().inmuebleId + 1
        }
    }

    fun removeImageFromList(image: Foto) {
        imagesList.value.remove(image)
        if(database.fotoDAO().findById(image.fotoId.toString()) != null) {
            database.fotoDAO().delete(image)
        }
    }

}