package controllers

import models.Note
import persistence.Serializer


class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()

    private var serializer: Serializer = serializerType

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

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

    fun listActiveNotes(): String {
        return if (numberOfActiveNotes() == 0) {
            "No active notes stored"
        } else {
            var listOfActiveNotes = ""
            for (note in notes) {
                if (!note.isNoteArchived) {
                    listOfActiveNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfActiveNotes
        }
    }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() == 0) {
            "No archived notes stored"
        } else {
            var listOfArchivedNotes = ""
            for (note in notes) {
                if (note.isNoteArchived) {
                    listOfArchivedNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfArchivedNotes
        }
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfNotes +=
                        """$i: ${notes[i]}
                        """.trimIndent()
                }
            }
            if (listOfNotes.equals("")) {
                "No notes with priority: $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }
        }
    }

    // returns the the amount of notes.
    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes(): Int {
        //return notes.stream().filter { obj: Note -> obj.isNoteArchived }.count().toInt()
        var counter = 0
        for (note in notes) {
            if (note.isNoteArchived) {
                counter++
            }
        }
        return counter
    }

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

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isNoteArchived}
            .count()
            .toInt()
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //return notes.stream().filter { p: Note -> p.notePriority == priority }.count().toInt()
        var counter = 0
        for (note in notes) {
            if (note.notePriority == priority) {
                counter++
            }
        }
        return counter
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}
