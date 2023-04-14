package com.uchi.joter.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uchi.joter.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NotesRepo(private val notesDao: NotesDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allNotes: Flow<List<NoteEntity>> = notesDao.getAllNote()

    val allNotesByDate : Flow<List<NoteEntity>> = notesDao.getAllNoteByDate()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(noteEntity: NoteEntity) = withContext(Dispatchers.IO) {
        notesDao.insertAll(noteEntity)
    }


    @WorkerThread
    suspend fun delete(noteEntity: NoteEntity){
        notesDao.deleteNote(noteEntity)
    }

//    fun search(query : String): LiveData<List<NoteEntity>> {
//        return notesDao.searchNotes(query)
//    }

    @WorkerThread
    suspend fun update(noteEntity: NoteEntity) = withContext(Dispatchers.IO) {
        notesDao.updateNote(noteEntity)
    }

    //new fun
    fun searchNote(query: String)= notesDao.searchNotes(query)

    fun searchNotesFlow(query: String): Flow<List<NoteEntity>> {
        val searchQuery = "%$query%"
        return notesDao.searchNotes(searchQuery)
    }





}

class NotesViewModelFactory(private val repo:NotesRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}