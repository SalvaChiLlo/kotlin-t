package com.kotlin_t.trobify.presentacion.editorFicha

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.logica.Foto
import com.kotlin_t.trobify.logica.Inmueble
import com.kotlin_t.trobify.presentacion.SharedViewModel

class EditorFichaViewModel(
    val database: AppDatabase,
    application: Application,
    val model: SharedViewModel,
) : AndroidViewModel(application) {
    var inmueble: Inmueble? = database.inmuebleDAO().findById("1")
    var inmuebleID: Int? = null
    var imagesList = MutableLiveData<MutableList<Foto>>()


    init {
        if (inmueble != null) {
            inmuebleID = inmueble!!.inmuebleId
        } else {
            inmuebleID =
                if (database.inmuebleDAO().getAll().count() <= 0) 0 else database.inmuebleDAO()
                    .getAll().last().inmuebleId + 1
        }

        imagesList.value = mutableListOf()
    }

    fun addImageToList(image: Foto) {
        imagesList.value?.add(image)
        imagesList.value = imagesList.value
    }

    fun removeImageFromList(image: Foto) {
        imagesList.value?.remove(image)
        imagesList.value = imagesList.value
    }

}