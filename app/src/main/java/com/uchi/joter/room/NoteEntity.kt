package com.uchi.joter.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "notes_table"

@Entity(tableName = TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val nid: Int =0,
    @ColumnInfo(name = "note_title") val noteTitle: String?,
    @ColumnInfo(name = "note_content") val noteContent: String?,
    @ColumnInfo(name = "note_date") val noteDate: String,
    @ColumnInfo(name = "note_time") val noteTime: String
)