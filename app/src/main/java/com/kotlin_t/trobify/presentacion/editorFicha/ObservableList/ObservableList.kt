package com.kotlin_t.trobify.presentacion.editorFicha.ObservableList

class ObservableList<E>: IObservable {
    override val observers: ArrayList<IObserver> = ArrayList<IObserver>()
    var value = ExtendedList<E>(this)
        set(value) {
            field = value
            sendUpdateEvent()
        }
}