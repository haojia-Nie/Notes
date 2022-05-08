package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel  : ViewModel() {

    val model = Model()
    val notes:MutableLiveData<MutableList<Note>> = MutableLiveData<MutableList<Note>>()

    val searchBar:MutableLiveData<String> = MutableLiveData(null)
    val actionBarMessage:MutableLiveData<String> = MutableLiveData(null)

    val selectedNote:MutableLiveData<Note> = MutableLiveData(null)
    val statusMessage:MutableLiveData<String> = MutableLiveData("")
    val popupMessage:MutableLiveData<String> = MutableLiveData()


    init {
        println("MyViewModel startup")
    }

    fun clearNotes(){
        // clear all notes
        model.clearNotes()
        updateParameters()
    }

    private fun updateParameters(){
        notes.value = model.displayNotes
        popupMessage.value = model.message
        actionBarMessage.value = model.subtitle
    }

    fun randomNote(){
        debug("newRandomNote")
        model.randomNote()
        updateParameters()
    }

    // update status message
    fun statusMessage(string:String){
        statusMessage.value = string
    }

    fun editNote(){
        // update status message
        statusMessage("Edit Note #${selectedNote.value!!.id}")
        model.editNote(selectedNote.value!!)
    }

    fun addNote(){
        popupMessage.value = null
        selectedNote.value = null
        actionBarMessage.value = model.subtitle
    }

    // set the search parameters
    fun setFilter(boolean: Boolean){
        model.isFiltered = boolean
        notes.value = model.displayNotes
        actionBarMessage.value = model.subtitle
    }

    fun setSearch(string: String){
        model.search = string
        notes.value = model.displayNotes
        actionBarMessage.value = model.subtitle
        searchBar.value = model.search
    }

    // save note
    fun saveNote(note: Note){
        if (note != null){
            if (note.id == null){
                model.addNote(note)
            }
            else {
                model.updateNote(note)
            }
        }
        updateParameters()
    }

    // set the view mode
    fun viewNote(note:Note){
        selectedNote.value = note
        note.isSelected = true
        popupMessage.value = null
    }

    // delete the note
    fun deleteNote(note:Note){
        model.deleteNote(note)
        updateParameters()
    }

    fun messageShowed(){
        // re initailize message since already showed
        popupMessage.value = null
    }

}