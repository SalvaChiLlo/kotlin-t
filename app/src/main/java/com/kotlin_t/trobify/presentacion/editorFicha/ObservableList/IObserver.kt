package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

interface IObserver<E> {
    fun update(value: MutableList<E>)
}