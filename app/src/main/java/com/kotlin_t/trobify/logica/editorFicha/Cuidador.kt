package com.kotlin_t.trobify.logica.editorFicha

import java.util.*

class Cuidador(val originator: EditorFichaViewModel) {

    var history : Stack<Memento> = Stack()

    fun createSnapshot() {
        history.push(originator.createMemento())
    }

    fun undo() {
        if(history.size == 1) originator.restore(history.peek())
        else originator.restore(history.pop())
    }

}

