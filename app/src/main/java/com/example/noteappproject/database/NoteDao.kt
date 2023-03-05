package com.example.noteappproject.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteappproject.models.Note
import androidx.room.Dao
import androidx.room.Entity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: com.example.noteappproject.models.Note)

    @Delete
    suspend fun delete(note:Note)

    @Query("SELECT * from notes_table order by id ASC")

    fun getAllNotes(): LiveData<List<Note>>

    @Query("UPDATE notes_table set title= :title,note=:note WHERE id=:id")
    suspend fun update(id:Int?,title:String?,note:String?)
}