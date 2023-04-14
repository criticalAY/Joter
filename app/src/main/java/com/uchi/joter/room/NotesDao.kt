package com.uchi.joter.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes_table ORDER BY nid DESC")
    fun getAllNote(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table ORDER BY nid ASC")
    fun getAllNoteByDate(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE note_title LIKE :query OR note_content LIKE :query OR note_date LIKE :query ORDER BY nid DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

}