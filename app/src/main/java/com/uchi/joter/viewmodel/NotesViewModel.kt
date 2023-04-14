package com.uchi.joter.viewmodel

import androidx.lifecycle.*
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.room.NotesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class NotesViewModel(private val repo: NotesRepo): ViewModel() {

    val allNotesFlow: LiveData<List<NoteEntity>> = repo.allNotes.asLiveData()

    val allNotesByDate : LiveData<List<NoteEntity>> = repo.allNotesByDate.asLiveData()

    private val _searchResult = MutableStateFlow<List<NoteEntity>>(emptyList())
    val searchResult: StateFlow<List<NoteEntity>> = _searchResult

//    fun searchNotes(query: String) {
//        repo.search(query).observeForever { noteEntities ->
//            _searchResult.value = noteEntities
//        }
//    }

    fun insert(note: NoteEntity) = viewModelScope.launch {
        repo.insert(note)
    }

    fun delete(note: NoteEntity) = viewModelScope.launch {
        repo.delete(note)
    }

//    fun search(query : String) = viewModelScope.launch {
//        repo.search(query)
//    }

    fun update(note: NoteEntity) = viewModelScope.launch {
        repo.update(note)
    }

//    fun searchNote(query: String): LiveData<List<NoteEntity>>
//    {
//        return repo.searchNote(query)
//    }

//    fun searchNotes(query: String?) {
//        if (query == null || query.isEmpty()) {
//            allNotesFlow.observeForever(observer)
//        } else {
//            searchNotesFlow(query).observeForever(observer)
//        }
//    }


}
