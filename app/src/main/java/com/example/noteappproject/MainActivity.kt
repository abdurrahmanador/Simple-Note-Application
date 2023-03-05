package com.example.noteappproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.noteappproject.models.Note
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappproject.adapters.NotesAdapater
import com.example.noteappproject.database.NoteDatabase
import com.example.noteappproject.databinding.ActivityMainBinding
import com.example.noteappproject.models.NoteViewModel

class MainActivity : AppCompatActivity(), NotesAdapater.noteItemClickListener,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapater
    lateinit var selectedNote: com.example.noteappproject.models.Note

    private val updateNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? com.example.noteappproject.models.Note
                if (note != null) {
                    viewModel.update(note)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
         adapter = NotesAdapater(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }

            }

        binding.floating.setOnClickListener {

            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }


        })
    }

    override fun onItemClickked(note: com.example.noteappproject.models.Note) {
        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("currentNote", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClickListener(note: com.example.noteappproject.models.Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.popup_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId ==R.id.deleteNote) {
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}