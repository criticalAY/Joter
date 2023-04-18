package com.uchi.joter.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.uchi.joter.R
import com.uchi.joter.room.NoteEntity
import com.uchi.joter.room.NotesRepo
import com.uchi.joter.room.NotesRoomDatabase
import com.uchi.joter.room.NotesViewModelFactory
import com.uchi.joter.viewmodel.NotesAdapter
import com.uchi.joter.viewmodel.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AddNoteActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var noteCount : TextView
    private lateinit var actionList: MenuItem
    private lateinit var actionGrid: MenuItem
    private lateinit var actionlatest : MenuItem
    private lateinit var actionDate : MenuItem
    private lateinit var searchView : SearchView


    lateinit var addNote:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_note)
        addNote = findViewById(R.id.action_add_note)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        addNote.setOnClickListener {
            startActivity(Intent(this,CreateNoteActivity::class.java))
        }
        noteCount = findViewById(R.id.notes_count)
        notesRecyclerView = findViewById(R.id.recyclerview_notes)
//        notesAdapter = NotesAdapter(emptyList())
        notesAdapter = NotesAdapter(emptyList()) { note ->
        }
        notesRecyclerView.adapter = notesAdapter




    }
//
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        if(menu!=null) {

            actionList = menu.findItem(R.id.action_list_view)
            actionGrid = menu.findItem(R.id.action_grid_view)
            actionlatest = menu.findItem(R.id.action_sort_by_last_created)
            actionDate = menu.findItem(R.id.action_sort_by_time)

            setupRecyclerView(false)
//
//            val searchView = menu.findItem(R.id.action_search).actionView as SearchView
//            searchView.queryHint = "Search"
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                   notesViewModel.searchNotes(query!!)
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                   notesViewModel.searchNotes(newText!!)
//                    return false
//                }
//            })
        }

        return super.onCreateOptionsMenu(menu)
    }

//    private fun handleIntent(intent: Intent) {
//
//        if (Intent.ACTION_SEARCH == intent.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            //use the query to search your data somehow
//
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_edit ->{
                true
            }
            R.id.action_list_view -> {
                changeToListView()
                item.isVisible = false
                true
            }
            R.id.action_grid_view -> {
                changeToGridView()
                item.isVisible = false
                true
            }
            R.id.action_sort_by_time ->{
                setupRecyclerView(true)
                item.isVisible = false
                actionlatest.isVisible = true
                true
            }
            R.id.action_sort_by_last_created ->{
                setupRecyclerView(false)
                item.isVisible = false
                actionDate.isVisible = true
                true
            }
            R.id.action_search ->{
              searchView =   item.actionView as SearchView
                true
            }
            R.id.action_settings ->{
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeToGridView() {
        actionList.isVisible = true
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        notesRecyclerView.layoutManager = layoutManager
    }

    private fun changeToListView() {
        actionGrid.isVisible = true
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        notesRecyclerView.layoutManager = layoutManager
    }

    private fun setupRecyclerView(sortByDate: Boolean, searchQuery: String = ""){
       val applicationScope = CoroutineScope(Dispatchers.Default)
       val notesRepo = NotesRepo(NotesRoomDatabase.getDatabase(this, applicationScope).notesDao())
       val notesViewModelFactory = NotesViewModelFactory(notesRepo)
       val recyclerViewLayout = findViewById<RecyclerView>(R.id.recyclerview_notes)

       val emptyNotesLayout = findViewById<LinearLayout>(R.id.empty_data_layout)
       notesViewModel = ViewModelProvider(this, notesViewModelFactory)[NotesViewModel::class.java]


        if(!sortByDate) {
            notesViewModel.allNotesFlow.observe(this) { notes ->
//                notesAdapter = NotesAdapter(notes)
                notesAdapter = NotesAdapter(notes) { note ->
                }


                notesRecyclerView.adapter = notesAdapter
                if (notesAdapter.itemCount == 0) {

                    emptyNotesLayout.visibility = View.VISIBLE
                    recyclerViewLayout.visibility = View.GONE
                } else {
                    recyclerViewLayout.visibility = View.VISIBLE
                    emptyNotesLayout.visibility = View.GONE
                    noteCount.text = getString(R.string.note_count, notesAdapter.itemCount)
                }
            }
        }else{
            notesViewModel.allNotesByDate.observe(this) { notes ->
//                notesAdapter = NotesAdapter(notes)
                notesAdapter = NotesAdapter(notes) { note ->

                }

                notesRecyclerView.adapter = notesAdapter
                if(notesAdapter.itemCount == 0){

                    emptyNotesLayout.visibility = View.VISIBLE
                    recyclerViewLayout.visibility = View.GONE
                    noteCount.visibility = View.GONE
                } else{
                    noteCount.visibility = View.VISIBLE
                    recyclerViewLayout.visibility = View.VISIBLE
                    emptyNotesLayout.visibility = View.GONE
                    noteCount.text = getString(R.string.note_count, notesAdapter.itemCount)
                }
            }

        }
//
//        if (searchQuery.isNotEmpty()) {
//            notesViewModel.searchNotes(searchQuery)
//        }



       val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
       layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

       notesRecyclerView.layoutManager = layoutManager
   }


//    private fun search() {
//        if (Intent.ACTION_SEARCH == intent.action) {
//            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
//                notesViewModel.search(query)
//
//            }
//        }
//    }
}