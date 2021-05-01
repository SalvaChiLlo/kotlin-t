package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

class ObservableList<E>: IObservable<E> {
    override val observers: ArrayList<IObserver<E>> = ArrayList<IObserver<E>>()


    private var value = mutableListOf<E>()
        set(value) {
            field = value
            sendUpdateEvent()
        }


    fun addItem(element: E) {
        value.add(element)
        sendUpdateEvent()
    }

    fun addAllItem(elements: Collection<E>) {
        value.addAll(elements)
        sendUpdateEvent()
    }

    fun removeItem(element: E) {
        value.remove(element)
        sendUpdateEvent()
    }

    fun getValue(): List<E> {
        return value
    }

    override fun sendUpdateEvent() {
        observers.forEach { it.update(value) }
    }
}