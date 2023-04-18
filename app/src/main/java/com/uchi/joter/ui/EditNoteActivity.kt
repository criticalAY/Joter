package com.uchi.joter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.uchi.joter.R
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.room.NotesRepo
import com.uchi.joter.room.NotesRoomDatabase
import com.uchi.joter.room.NotesViewModelFactory
import com.uchi.joter.utils.Utility
import com.uchi.joter.utils.Utility.NOTE_ID
import com.uchi.joter.viewmodel.NotesViewModel
import com.yahiaangelo.markdownedittext.MarkdownEditText
import com.yahiaangelo.markdownedittext.MarkdownStylesBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class EditNoteActivity : AppCompatActivity() {
    private lateinit var homeButton : ImageView
    lateinit var titleText: TextInputEditText
    lateinit var contentText: MarkdownEditText
    private lateinit var styleBar : MarkdownStylesBar
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var dateTimeTextView : TextView
    lateinit var time:String
    lateinit var date:String
    lateinit var delNoteEntity: NoteEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_note)
        val toolbar = findViewById<Toolbar>(R.id.create_note_toolbar)
        setSupportActionBar(toolbar)
        titleText = findViewById(R.id.title_text_input)
        homeButton = findViewById(R.id.action_home)
        contentText = findViewById(R.id.content_text_input)
        dateTimeTextView = findViewById(R.id.date_text_view)
        styleBar = findViewById(R.id.style_bar)
        styleBar.stylesList = arrayOf(MarkdownEditText.TextStyle.BOLD,MarkdownEditText.TextStyle.ITALIC,MarkdownEditText.TextStyle.ORDERED_LIST,MarkdownEditText.TextStyle.UNORDERED_LIST, MarkdownEditText.TextStyle.TASKS_LIST,
            MarkdownEditText.TextStyle.STRIKE,MarkdownEditText.TextStyle.QUOTE)
        contentText.setStylesBar(styleBar)
        time =Utility.getFormattedTime()
        date = Utility.getFormattedDate()
        dateTimeTextView.text = getString(R.string.date_time,date,time)
        setupEditNote()

        homeButton.setOnClickListener {
            val mIntent = Intent(this, AddNoteActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mIntent)

        }
    }

    private fun setupEditNote() {
        titleText.setText(intent.getStringExtra(Utility.NOTE_TITLE))
        contentText.renderMD(intent.getStringExtra(Utility.NOTE_CONTENT)!!)
//        contentText.setText(intent.getStringExtra(Utility.NOTE_CONTENT))
        dateTimeTextView.text = intent.getStringExtra(Utility.NOTE_DATE)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_note_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_save ->{
                updateNote()
                true
            }
//            R.id.action_delete ->{
//                val applicationScope = CoroutineScope(Dispatchers.Default)
//                val notesRepo = NotesRepo(NotesRoomDatabase.getDatabase(this, applicationScope).notesDao())
//                val notesViewModelFactory = NotesViewModelFactory(notesRepo)
//                val notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]
//                notesViewModel.delete(delNoteEntity)
//                finishAffinity()
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun updateNote() {
        val noteTitle = titleText.text?.toString()
        val noteContent = contentText.getMD()
        val applicationScope = CoroutineScope(Dispatchers.Default)
        val notesRepo = NotesRepo(NotesRoomDatabase.getDatabase(this, applicationScope).notesDao())
        val notesViewModelFactory = NotesViewModelFactory(notesRepo)
        notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]

        val editNote = NoteEntity(intent.getIntExtra(NOTE_ID,0), noteTitle, noteContent, date, time)

        // Pass the new note to the ViewModel to save it to the database
        notesViewModel.update(editNote)
        finish()


    }
}