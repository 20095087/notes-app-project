package models

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var status: String,
    var content: String,
    var isNoteArchived :Boolean)
