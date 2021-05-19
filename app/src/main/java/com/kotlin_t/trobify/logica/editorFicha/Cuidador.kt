package com.kotlin_t.trobify.logica.editorFicha

import java.util.*

class Cuidador(val originator: EditorFichaViewModel) {

    var history : Stack<EditorFichaViewModel.Memento> = Stack()

    fun createSnapshot() {
        history.push(originator.createMemento())
    }

    fun undo() {
        if(history.size == 1) history.peek().restore()
        else history.pop().restore()
    }

}