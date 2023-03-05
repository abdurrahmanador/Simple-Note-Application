package com.example.noteappproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteappproject.databinding.ActivityAddNoteBinding
import com.example.noteappproject.models.Note
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note:Note
    private lateinit var oldNote:Note
    var isUpdate=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            oldNote=intent.getSerializableExtra("currentNote") as Note
            binding.titleET.setText(oldNote.title)
            binding.noteET.setText(oldNote.note)
            isUpdate=true
        }
        catch (e:java.lang.Exception){
            e.printStackTrace()
        }

        binding.doneImage.setOnClickListener {
            val title=binding.titleET.text.toString()
            val noteDescription=binding.noteET.text.toString()

            if(title.isNotEmpty()|| noteDescription.isNotEmpty()){
                val formatter=SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                if(isUpdate){
                    note=Note(
                        oldNote.id,title,noteDescription,formatter.format(Date())
                    )
                }else{
                    note= Note(
                        null,title,noteDescription,formatter.format(Date())
                    )
                }
                val intent= Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            else{
                Toast.makeText(this@AddNote,"Please Enter Text",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.backImage.setOnClickListener {
            onBackPressed()
        }
    }
}