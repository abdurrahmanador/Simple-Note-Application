package com.example.noteappproject.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappproject.R
import com.example.noteappproject.models.Note
import kotlin.random.Random

class NotesAdapater(private val context: Context, val listener: noteItemClickListener) :
    RecyclerView.Adapter<NotesAdapater.NoteViewHolder>() {

    private val NoteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NoteList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                randomColor(), null
            )
        )
        holder.notes_layout.setOnClickListener {
            listener.onItemClickked(NoteList[holder.absoluteAdapterPosition])
        }
        holder.notes_layout.setOnClickListener {
            listener.onLongItemClickListener(
                NoteList[holder.absoluteAdapterPosition], holder.notes_layout
            )
            true
        }
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        NoteList.clear()
        for (item in fullList) {
            if (item.title?.lowercase()
                    ?.contains(search.lowercase()) == true || item.note?.lowercase()
                    ?.contains(search.lowercase()) == true
            ) {
                NoteList.add(item)
            }
        }
    }

    fun randomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.nc1)
        list.add(R.color.nc3)
        list.add(R.color.nc2)
        list.add(R.color.nc5)
        list.add(R.color.nc4)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notes_layout = itemView.findViewById<CardView>(R.id.cardView)
        val title = itemView.findViewById<TextView>(R.id.tvtitle)
        val note = itemView.findViewById<TextView>(R.id.tvNote)
        val date = itemView.findViewById<TextView>(R.id.tvdate)
    }

    interface noteItemClickListener {
        fun onItemClickked(note: Note)
        fun onLongItemClickListener(note: Note, cardView: CardView)
    }
}