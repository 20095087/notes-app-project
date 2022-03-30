import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.StatusUtility
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ---------------------------------------
         > |        NOTE KEEPER APP              |
         > ---------------------------------------
         > | NOTE MENU                           |
         > |   1) Add a note                     |
         > |   2) List all notes                 |
         > |   3) Update a note                  |
         > |   4) Delete a note                  |
         > |   5) Archive note                   |
         > |   6) List by priority               |
         > |   7) Number of notes                |
         > |   8) List archived notes            |
         > |   9) List active notes              |
         > |   10) Find note                     |
         > |   11) Number of Active Notes        |
         > |   12) Number of Notes by Priority   |
         > |   13) Number of Archived notes      |
         > |   14) Search for note               |
         > |   20) Save notes                    |
         > |   21) Load notes                    |
         > ---------------------------------------
         > |   0) Exit                           |
         > ---------------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            // calls the addNote() function
            1  -> addNote()
            // calls the listNotes() function
            2  -> listNotes()
            // calls the updateNote() function
            3  -> updateNote()
            // calls the deleteNote() function
            4  -> deleteNote()
            // calls the archiveNote() function
            5  -> archiveNote()
            // calls the listByPriority() function
            6  -> listByPriority()
            // calls the numOfNotes() function
            7  -> numOfNotes()
            // calls the listArchivedNotes() function
            8  -> listArchivedNotes()
            // calls the listActiveNotes() function
            9  -> listActiveNotes()
            // calls the findNote() function
            10  -> findNote()
            // calls the numberOfActiveNotes() function
            11  -> numberOfActiveNotes()
            // calls the numberOfNotesByPriority() function
            12  -> numberOfNotesByPriority()
            // calls the numberOfArchivedNotes() function
            13  -> numberOfArchivedNotes()
            // calls the searchByTitle() function
            14  -> searchByTitle()
            // calls the save() function
            20  -> save()
            // calls the load() function
            21  -> load()
            // calls the exitApp() function
            0  -> exitApp()
            // else display error message
            else -> System.out.println("Invalid option entered: $option")
        }
    } while (true)
}

fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    // get the user to enter the priority of the note
    var notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    // validate if the note priority is withing 0 to 5
    while(notePriority < 0 && notePriority > 5) {
        notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
        // if the priority is greater than 0 but less than 6 then break out of the while loop
        if(notePriority > 0 && notePriority < 6){
            break;
        }
    }
    // get the user to enter the category of the note
    var noteCategory = readNextLine("Enter a category for the note, can be either Shopping, Bills, College, Home, Other:  ")
    // validate if the category is one of the allowed categories
    while(!noteCategory.equals("Shopping") || !noteCategory.equals("Bills") || !noteCategory.equals("Other") || !noteCategory.equals("College") || !noteCategory.equals("Home")) {
        // if the category is correct then break out of the while loop
        if(noteCategory.equals("Shopping") || noteCategory.equals("Bills") || noteCategory.equals("Other") || noteCategory.equals("College") || noteCategory.equals("Home")){
            break;
        }
        noteCategory = readNextLine("Enter a category for the note, can be either Shopping, Bills, College, Home, Other:  ")
    }
    // get the user to enter the status of the note
    var status = readNextLine("Enter a status, can either be: ToDo, Doing, Done: ")
    // validate if the status is one fo the allowed statuses
    while(!StatusUtility.isValidStatus(status)){
        status = readNextLine("Enter a status, can either be: ToDo, Doing, Done: ")
    }
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, status,false))
    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun numOfNotes(){
    println(noteAPI.numberOfNotes())
}

fun listNotes() {
    // if there are notes
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun archiveNote(){
    // display all active notes
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        if (noteAPI.isValidIndex(indexToArchive)) {
            //pass the index of the note to archive and if the note exists then archive
            if (noteAPI.archiveNote(indexToArchive)){
                println("Archive Successful")
            } else {
                println("Archive Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun searchByTitle(){
    var title = ScannerInput.readNextLine("Enter note title: ")
    println(noteAPI.searchForNote(title))
}

fun findNote(){
    listNotes()
    var note: Int = ScannerInput.readNextInt("Enter note index: ")
    println(noteAPI.findNote(note))
}

fun numberOfNotesByPriority(){
    var priority: Int = ScannerInput.readNextInt("Enter priority: ")
    println(noteAPI.numberOfNotesByPriority(priority))
}

fun numberOfArchivedNotes(){
    println(noteAPI.numberOfArchivedNotes())
}

fun listByPriority(){
    var priority: Int = ScannerInput.readNextInt("Enter priority: ")
    println(noteAPI.listNotesBySelectedPriority(priority))
}

fun listArchivedNotes(){
    println(noteAPI.listArchivedNotes())
}

fun numberOfActiveNotes(){
    println(noteAPI.numberOfActiveNotes())
}

fun listActiveNotes(){
    println(noteAPI.listActiveNotes())
}

fun updateNote(){
//    logger.info { "updateNote() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        //logger.info { "addNote() function invoked" }
        val noteTitle = readNextLine("Enter a title for the note: ")
        // get the user to enter the priority of the note
        var notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
        // validate if the note priority is withing 0 to 5
        while(notePriority < 0 && notePriority > 5) {
            notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            // if the priority is greater than 0 but less than 6 then break out of the while loop
            if(notePriority > 0 && notePriority < 6){
                break;
            }
        }
        // get the user to enter the category of the note
        var noteCategory = readNextLine("Enter a category for the note, can be either Shopping, Bills, College, Home, Other:  ")
        // validate if the category is one of the allowed categories
        while(!noteCategory.equals("Shopping") || !noteCategory.equals("Bills") || !noteCategory.equals("Other") || !noteCategory.equals("College") || !noteCategory.equals("Home")) {
            // if the category is correct then break out of the while loop
            if(noteCategory.equals("Shopping") || noteCategory.equals("Bills") || noteCategory.equals("Other") || noteCategory.equals("College") || noteCategory.equals("Home")){
                break;
            }
            noteCategory = readNextLine("Enter a category for the note, can be either Shopping, Bills, College, Home, Other:  ")
        }
        // get the user to enter the status of the note
        var status = readNextLine("Enter a status, can either be: ToDo, Doing, Done: ")
        // validate if the status is one fo the allowed statuses
        while(!status.equals("ToDo") || !status.equals("Doing") || !status.equals("Done")) {
            // if the status is correct then break out of the while loop
            if(status.equals("ToDo") || status.equals("Doing") || status.equals("Done")){
                break;
            }
            status = readNextLine("Enter a status, can either be: ToDo, Doing, Done: ")
        }
        //pass the index of the note and the new note details to NoteAPI for updating and check for success.
        if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, status,false))){
            println("Update Successful")
        } else {
            println("Update Failed")
        }
    } else {
        println("There are no notes for this index number")
    }
}


fun deleteNote(){
//    logger.info { "deleteNote() function invoked" }
    // list the notes
    listNotes()
    // if there are notes
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

