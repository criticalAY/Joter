package com.uchi.joter

import android.app.Application
import com.uchi.joter.room.NotesRepo
import com.uchi.joter.room.NotesRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class JoterApp: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())


    val database by lazy { NotesRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NotesRepo(database.notesDao()) }

}