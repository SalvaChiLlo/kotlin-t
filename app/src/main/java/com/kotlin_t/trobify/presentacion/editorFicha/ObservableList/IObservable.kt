package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

import java.util.*
import kotlin.collections.ArrayList

interface IObservable<E> {
    val observers: ArrayList<IObserver<E>>


    fun addObserver(observer: IObserver<E>) {
        observers.add(observer)
    }

    fun removeObserver(observer: IObserver<E>) {
        observers.remove(observer)
    }

    fun sendUpdateEvent()
}