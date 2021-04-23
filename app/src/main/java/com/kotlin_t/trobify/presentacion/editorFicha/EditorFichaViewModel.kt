package com.kotlin_t.trobify.presentacion.editorFicha

import android.app.Application
import android.graphics.Bitmap
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
    var inmueble: Inmueble? = null
    var inmuebleID: Int? = null
    var imagesList = MutableLiveData<MutableList<Bitmap>>()


    init {
        if (inmueble != null) {
            inmuebleID = inmueble!!.inmuebleId
        }

        imagesList.value = mutableListOf()
    }

    fun addImageToList(image: Bitmap) {
        imagesList.value?.add(image)
        imagesList.value = imagesList.value
    }

    fun removeImageFromList(image: Bitmap) {
        imagesList.value?.remove(image)
        imagesList.value = imagesList.value
    }

}