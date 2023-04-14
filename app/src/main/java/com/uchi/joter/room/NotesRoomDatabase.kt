package com.uchi.joter.room

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NotesRoomDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object{
        @Volatile
        private var INSTANCE: NotesRoomDatabase? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope): NotesRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesRoomDatabase::class.java,
                    TABLE_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}