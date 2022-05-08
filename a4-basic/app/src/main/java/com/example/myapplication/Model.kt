package com.example.myapplication

import kotlin.random.Random

class Model {
    val allNotes:MutableList<Note> = mutableListOf()
    var message:String = ""

    var isFiltered:Boolean = false
    var search:String = ""

    val displayNotes: MutableList<Note>
        get() {
            // get list with importance filter
            val list =
                if (isFiltered) {
                    allNotes.filter { x -> x.important }
                }
                else
                    allNotes

            // include any search filtering
            if (search != "") {
                return list.filter { x -> x.title.lowercase().contains(search!!) ||
                        x.body.lowercase().contains(search!!) } as MutableList<Note>
            } else {
                return list as MutableList<Note>
            }
        }

    val subtitle:String
        get() {
            val str =
                if (isFiltered || search != "")
                    "(matching ${displayNotes.size} of ${allNotes.size} notes)"
                else
                    "(${displayNotes.size} notes)"

            return str
        }



    init {

    }

    // simple unique note ID, just count up from 0 each run
    private var _counter: Long = 0
    private fun generateID(): Long {
        return _counter++
    }

    fun addNote(newNote: Note){
        val note = newNote.copy(id = generateID())
        allNotes.add(note)
        message = "Added Note #${note.id}"
    }

    // add a random initialized note
    fun randomNote(){
        // title is 1 to 3 words in Title Case
        val title = LoremIpsum.getRandomSequence(Random.nextInt(1, 3))
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }

        // body is 2 to 5 sentences, each 3 to 10 words long
        var body = ""
        for (i in 0..Random.nextInt(2, 5)) {
            val sentence = LoremIpsum.getRandomSequence(Random.nextInt(3, 10))
                .replaceFirstChar { it.uppercase() }
                .plus(". ")
            body = body.plus(sentence)
        }

        // create the new note and pick random importance with prob 20%
        val note = Note(null, title, body, important = (Random.nextDouble() < 0.2))
        addNote(note)
    }

    // delete note
    fun deleteNote(note:Note){
        allNotes.remove(note)
        message = "Deleted Note #${note.id}"
    }

    // edit note show message
    fun editNote(note: Note){
        message = "Edited Note #${note.id}"
    }

    // clear notes
    fun clearNotes(){
        message = "Cleared ${displayNotes.size} Notes"
        allNotes.removeAll(displayNotes)
    }

    // update notes
    fun updateNote(updatedNote: Note) {
        var note = allNotes.find { x -> x.id == updatedNote.id }
        // must be a better way to do this part?
        if (note != null) {
            note.title = updatedNote.title
            note.body = updatedNote.body
            note.important = updatedNote.important
            message = "Edited Note #${note.id}"
        }
    }

}