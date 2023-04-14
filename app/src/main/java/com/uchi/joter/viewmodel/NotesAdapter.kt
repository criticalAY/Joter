package com.uchi.joter.viewmodel

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.uchi.joter.R
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.ui.CreateNoteActivity
import com.uchi.joter.ui.EditNoteActivity
import com.uchi.joter.utils.NotesBottomSheetDialogFragment
import com.uchi.joter.utils.Utility.NOTE_CONTENT
import com.uchi.joter.utils.Utility.NOTE_DATE
import com.uchi.joter.utils.Utility.NOTE_ID
import com.uchi.joter.utils.Utility.NOTE_TITLE


class NotesAdapter(private val notes: List<NoteEntity>,  private val longPressListener: (NoteEntity) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        val subTitleTextView: TextView = itemView.findViewById(R.id.note_sub_title)
        val dateTextView: TextView = itemView.findViewById(R.id.note_date)
        val checkBox: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val handler = Handler()

        init {
            itemView.setOnClickListener {
                val note = notes[adapterPosition]
                val context = itemView.context
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(NOTE_ID, note.nid)
                intent.putExtra(NOTE_TITLE, note.noteTitle)
                intent.putExtra(NOTE_CONTENT, note.noteContent)
                intent.putExtra(NOTE_DATE, note.noteDate)
                context.startActivity(intent)
            }


            // Set custom long click duration
            itemView.setOnTouchListener(object : View.OnTouchListener {
                private val longClickDuration = 1200L
                private var startTime = 0L

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            startTime = System.currentTimeMillis()
                            handler.postDelayed(longClickRunnable, longClickDuration)
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            handler.removeCallbacks(longClickRunnable)

                        }
                    }
                    return false
                }


                private val longClickRunnable = Runnable {
                    val note = notes[adapterPosition]
                    longPressListener(note)
                    val dialog = NotesBottomSheetDialogFragment(note)
                    dialog.show((itemView.context as AppCompatActivity).supportFragmentManager, "MyBottomSheetDialog")


                }
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_card, parent, false)
        itemView.setBackgroundColor(Color.TRANSPARENT)

        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.noteTitle
        holder.subTitleTextView.text = note.noteContent
        holder.dateTextView.text = note.noteDate
    }

    override fun getItemCount(): Int = notes.size
}
