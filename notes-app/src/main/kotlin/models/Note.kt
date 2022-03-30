package models

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var status: String,
    var isNoteArchived :Boolean)
