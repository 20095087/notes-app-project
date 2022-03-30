package controllers

import models.Note
import persistence.Serializer


class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()

    private var serializer: Serializer = serializerType

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun listActiveNotes(): String =
        if(numberOfActiveNotes() == 0) "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived })


    fun listArchivedNotes(): String =
        if(numberOfActiveNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived })
    
    fun listNotesBySelectedPriority(priority: Int): String =
        if(notes.isEmpty()) "No notes stored."
        else notes.filter { note -> note.notePriority == priority }.joinToString(separator = "\n") { note -> notes.indexOf(note).toString() + ": " + note.toString() }

    // returns the the amount of notes.
    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes(): Int = notes.filter { note: Note -> note.isNoteArchived }.count()


    fun archiveNote(index: Int): Boolean {
        //find the note object by the index number
        val foundNote = findNote(index)
        //if we find a note with this index.
        if (foundNote != null) {
            foundNote.isNoteArchived = true
            return true
        }
        //if we did not find a note with the passed in index
        return false
    }

    fun numberOfActiveNotes(): Int = notes.filter { note: Note -> !note.isNoteArchived }.count()

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream().filter{note: Note -> note.notePriority == priority}.count().toInt()
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

//    fun listActiveNotes(): String =
//        if(numberOfActiveNotes() == 0) "No active notes stored"
//        else notes.filter{note -> note.isNoteArchived == false}.joinToString(separator = "\n") { note -> notes.indexOf(note).toString() + ": " + note.toString()  }

    fun searchForNote(title: String) =
       notes.filter { note -> note.noteTitle.contains(title, ignoreCase = true)}
           .joinToString(separator = "\n") { note -> notes.indexOf(note).toString() + ": " + note.toString()  }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    // this function formats the notes
    // we created this function so we would not repeat ourselves
    fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

    // this function loads the notes
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    // this function saves the notes ot a file
    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}
