package com.uchi.joter.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uchi.joter.R
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.room.NotesRepo
import com.uchi.joter.room.NotesRoomDatabase
import com.uchi.joter.room.NotesViewModelFactory
import com.uchi.joter.ui.AddNoteActivity
import com.uchi.joter.viewmodel.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NotesBottomSheetDialogFragment(private val note: NoteEntity): BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notes_bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set up the UI of the dialog


        val deleteButton = view.findViewById<LinearLayout>(R.id.delete_note)
        deleteButton.setOnClickListener {
          deleteNote(note)
            dismiss() // dismiss the dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme)
    }

    private fun deleteNote(noteEntity: NoteEntity){
        val applicationScope = CoroutineScope(Dispatchers.Default)
        val notesRepo = NotesRepo(NotesRoomDatabase.getDatabase(requireContext(), applicationScope).notesDao())
        val notesViewModelFactory = NotesViewModelFactory(notesRepo)
        val notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]
        notesViewModel.delete(noteEntity)
    }
}