package com.uchi.joter.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.uchi.joter.MainActivity
import com.uchi.joter.R
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.room.NotesRepo
import com.uchi.joter.room.NotesRoomDatabase
import com.uchi.joter.room.NotesViewModelFactory
import com.uchi.joter.utils.TextViewUndoRedo
import com.uchi.joter.utils.Utility
import com.uchi.joter.utils.Utility.NOTE_CONTENT
import com.uchi.joter.utils.Utility.NOTE_DATE
import com.uchi.joter.utils.Utility.NOTE_ID
import com.uchi.joter.utils.Utility.NOTE_TITLE
import com.uchi.joter.viewmodel.NotesViewModel
import com.yahiaangelo.markdownedittext.MarkdownEditText
import com.yahiaangelo.markdownedittext.MarkdownStylesBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*


class CreateNoteActivity : AppCompatActivity() {

    lateinit var titleText: TextInputEditText
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var dateTimeTextView : TextView
    private lateinit var closeNoteButton: ImageView
    lateinit var time:String
    lateinit var date:String
    lateinit var undoBtn: ImageView
    lateinit var redoBtn: ImageView
    lateinit var textHelper: TextViewUndoRedo
    private lateinit var noteID : String
    private lateinit var contentText : MarkdownEditText
    private lateinit var styleBar : MarkdownStylesBar


    override fun onCreate(savedInstanceState: Bundle?) {
      //  supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_note)
        // Inside your activity's onCreate method

        val toolbar = findViewById<Toolbar>(R.id.create_note_toolbar)
        setSupportActionBar(toolbar)
        titleText = findViewById(R.id.title_text_input)
        contentText = findViewById(R.id.note_content_input)
        dateTimeTextView = findViewById(R.id.date_text_view)
        closeNoteButton = findViewById(R.id.action_cancel)
        styleBar = findViewById(R.id.style_bar)
        styleBar.stylesList = arrayOf(MarkdownEditText.TextStyle.BOLD,MarkdownEditText.TextStyle.ITALIC,MarkdownEditText.TextStyle.ORDERED_LIST,MarkdownEditText.TextStyle.UNORDERED_LIST, MarkdownEditText.TextStyle.TASKS_LIST,
            MarkdownEditText.TextStyle.STRIKE,MarkdownEditText.TextStyle.QUOTE)
        contentText.setStylesBar(styleBar)

        undoBtn = findViewById(R.id.action_undo)
        redoBtn = findViewById(R.id.action_redo)

        textHelper = TextViewUndoRedo(contentText)
        textHelper.setMaxHistorySize(50)
        textUndoRedo()

        time =Utility.getFormattedTime()
        date = Utility.getFormattedDate()
        dateTimeTextView.text = getString(R.string.date_time,date,time)

        closeNoteButton.setOnClickListener {
            val mIntent = Intent(this, AddNoteActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mIntent)

        }


    }


    private fun textUndoRedo() {


        undoBtn.setOnClickListener {
            if(contentText.text!!.isNotEmpty()){
                textHelper.undo()

            }

        }
        redoBtn.setOnClickListener {
            if(contentText.text!!.isNotEmpty()){
                textHelper.redo()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_note_menu,menu)
        val delete : MenuItem = menu!!.findItem(R.id.action_delete)
        delete.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_save ->{
                 saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveNote() {
        val noteTitle = titleText.text?.toString()
        val noteContent = contentText.getMD().toString()
        val applicationScope = CoroutineScope(Dispatchers.Default)
        val notesRepo = NotesRepo(NotesRoomDatabase.getDatabase(this, applicationScope).notesDao())
        val notesViewModelFactory = NotesViewModelFactory(notesRepo)
        notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]

        // Create a new NoteEntity object with the retrieved data
        val newNote = NoteEntity(0, noteTitle, noteContent, date, time)

        // Pass the new note to the ViewModel to save it to the database
        notesViewModel.insert(newNote)

        // Return to the main activity
        finish()
    }



}