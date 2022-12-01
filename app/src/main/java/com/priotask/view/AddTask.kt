package com.priotask.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.priotask.R
import com.priotask.model.Task

private lateinit var inputNama: TextInputEditText
private lateinit var inputDesc: TextInputEditText
private lateinit var inputPrioritas: TextInputEditText
private lateinit var inputDate: EditText

private lateinit var savedData: SharedPreferences
private var DATA = "KeyData"
private var NAMA = "KeyNama"
private var DESKRIPSI = "KeyDesc"
private var PRIORITAS = "KeyPrioritas"
private var DATE = "KeyDate"
private var taskId = 0

private lateinit var database: DatabaseReference

class AddTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        database = Firebase.database.reference

        inputNama = findViewById(R.id.inputNama)
        inputDesc = findViewById(R.id.inputDesc)
        inputPrioritas = findViewById(R.id.inputPrioritas)
        inputDate = findViewById(R.id.inputDate)

//        savedData = getSharedPreferences(DATA, Context.MODE_PRIVATE)

        val bundle: Bundle? = intent.extras
        val username = bundle?.get("username").toString()
        val email = bundle?.get("email").toString()
        val password = bundle?.get("password").toString()

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask)
        buttonAddTask.setOnClickListener {

            var nama = inputNama.text.toString()
            var desc = inputDesc.text.toString()
            var prioritas = inputPrioritas.text.toString()
            var date = inputDate.text.toString()

            if (nama.isEmpty() || desc.isEmpty() || prioritas.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please insert the field!", Toast.LENGTH_SHORT).show()
            } else {
                val databaseListener = object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        addTask(taskId, username, nama, desc, prioritas, date)
                        Toast.makeText(this@AddTask, "Task added!", Toast.LENGTH_SHORT).show()

                        intent = Intent(this@AddTask, MainActivity::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("email", email)
                        intent.putExtra("password", password)
                        startActivity(intent)
                        taskId + 1
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }

                database.child("task").addListenerForSingleValueEvent(databaseListener)
            }

//            val saveData = savedData.edit()
//            saveData.putString(NAMA, inputNama.text.toString())
//            saveData.putString(DESKRIPSI, inputDesc.text.toString())
//            saveData.putString(PRIORITAS, inputPrioritas.text.toString())
//            saveData.putString(DATE, inputDate.text.toString())
//            saveData.apply()
//
//            intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("nama", inputNama.text.toString())
//            intent.putExtra("deskripsi", inputDesc.text.toString())
//            intent.putExtra("prioritas", inputPrioritas.text.toString())
//            intent.putExtra("date", inputDate.text.toString())
//            startActivity(intent)
//
//            inputNama.setText("")
//            inputDesc.setText("")
//            inputPrioritas.setText("")
//            inputDate.setText("")
        }
    }

    fun addTask(taskId: Int, username: String, nama: String, desc: String, prioritas: String, date: String) {
        val task = Task(username, nama, date, prioritas, desc)
        database.child("task").child(taskId.toString()).setValue(task)
    }
}