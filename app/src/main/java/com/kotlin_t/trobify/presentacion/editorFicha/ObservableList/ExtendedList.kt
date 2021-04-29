package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

class ExtendedList<E>(val observable: ObservableList<E>): ArrayList<E>() {
    override fun add(element: E): Boolean {
        val result = super.add(element)
        observable.sendUpdateEvent()
        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val result = super.addAll(elements)
        observable.sendUpdateEvent()
        return result
    }

    override fun remove(element: E): Boolean {
        val result = super.remove(element)
        observable.sendUpdateEvent()
        return result
    }
}