package com.priotask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.priotask.R
import com.priotask.model.Task
import java.util.*

private lateinit var inputNama: TextInputEditText
private lateinit var inputDesc: TextInputEditText
private lateinit var inputPrioritas: TextInputEditText
private lateinit var inputDate: DatePicker

private lateinit var username: String
private lateinit var email: String
private lateinit var password: String

private lateinit var database: DatabaseReference

class EditTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)

        database = Firebase.database.reference

        inputNama = findViewById(R.id.inputNama)
        inputDesc = findViewById(R.id.inputDesc)
        inputPrioritas = findViewById(R.id.inputPrioritas)
        inputDate = findViewById(R.id.inputDate)

        val bundle: Bundle? = intent.extras
        username = bundle?.get("username").toString()
        email = bundle?.get("email").toString()
        password = bundle?.get("password").toString()
        var taskid = bundle?.get("taskid").toString()

        val databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                inputNama.setText(dataSnapshot.child("nama").getValue().toString())
                inputDesc.setText(dataSnapshot.child("note").getValue().toString())
                inputPrioritas.setText(dataSnapshot.child("priority").getValue().toString())
                var dates = dataSnapshot.child("date").getValue().toString().split("/").toTypedArray()
//                inputDate.setText(dataSnapshot.child("date").getValue().toString())
                inputDate.init(dates[2].toInt(), dates[1].toInt()-1, dates[0].toInt())
                {   view, year, month, day ->
//                    date = "$day/$month/$year"
//                    Log.d("date", date)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        database.child("task").child(taskid).addListenerForSingleValueEvent(databaseTask)

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        val buttonEditTask = findViewById<Button>(R.id.buttonEditTask)
        buttonEditTask.setOnClickListener {
            var nama = inputNama.text.toString()
            var desc = inputDesc.text.toString()
            var prioritas = inputPrioritas.text.toString()
            var date = ""

//            inputDate.init(inputDate.year, inputDate.month, dates[0])
//            {   view, year, month, day ->
//                date = "$day/$month/$year"
//                Log.d("date", date)
//            }

            date = "${inputDate.dayOfMonth}/${inputDate.month+1}/${inputDate.year}"
            Log.d("edittaskid", taskId.toString())
            if (nama.isEmpty() || desc.isEmpty() || prioritas.isEmpty()) {
                Toast.makeText(this, "Mohon isi data secara lengkap", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task berhasil diperbarui", Toast.LENGTH_SHORT).show()
                editTask(taskid, username, nama, desc, prioritas, date)
                showPagePengunjung()
            }

        }

        val buttonDone = findViewById<Button>(R.id.buttonDone)
        buttonDone.setOnClickListener {
            Toast.makeText(this, "Task Erased!", Toast.LENGTH_SHORT).show()

            deleteTask(taskid.toInt())
            showDeleteSuccess()
        }
    }

    fun editTask(taskid: String, username: String, nama: String, desc: String, prioritas: String, date: String) {
        val task = Task(taskid, username, nama, date, prioritas, desc)
        Log.d("edittaskid", taskid.toString())

        database.child("task").child(taskid.toString()).setValue(task)
    }

    fun showPagePengunjung() {
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    fun deleteTask(taskid: Int) {
        database.child("task").child(taskid.toString()).removeValue()
    }

    fun showDeleteSuccess() {
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }
}