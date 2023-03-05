package com.example.noteappproject.models

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.noteappproject.models.Note
import androidx.lifecycle.viewModelScope
import com.example.noteappproject.database.NoteDatabase
import com.example.noteappproject.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository

    val allNotes:LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        noteRepository = NoteRepository(dao)
        allNotes = noteRepository.allNotes
    }
    fun deleteNote(note:Note)=viewModelScope.launch(Dispatchers.IO){
        noteRepository.delete(note)
    }
    fun insertNote(note: Note)=viewModelScope.launch(Dispatchers.IO){
        noteRepository.insert(note)
    }
    fun update(note:Note)=viewModelScope.launch(Dispatchers.IO){
        noteRepository.update(note)
    }
}