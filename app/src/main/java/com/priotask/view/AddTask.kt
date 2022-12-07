package com.priotask.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.priotask.R
import com.priotask.model.Task
import java.util.*

private lateinit var inputNama: TextInputEditText
private lateinit var inputDesc: TextInputEditText
private lateinit var inputPrioritas: TextInputEditText
//private lateinit var inputDate: DatePicker
//
//private lateinit var savedData: SharedPreferences
//private var DATA = "KeyData"
//private var NAMA = "KeyNama"
//private var DESKRIPSI = "KeyDesc"
//private var PRIORITAS = "KeyPrioritas"
//private var DATE = "KeyDate"
//private var taskId = 0

private lateinit var username: String
private lateinit var email: String
private lateinit var password: String

private lateinit var database: DatabaseReference

class AddTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        database = Firebase.database.reference

        var date = ""
        var taskid = UUID.randomUUID().toString()

        inputNama = findViewById(R.id.inputNama)
        inputDesc = findViewById(R.id.inputDesc)
        inputPrioritas = findViewById(R.id.inputPrioritas)
        val inputDate = findViewById<DatePicker>(R.id.inputDate)

//        savedData = getSharedPreferences(DATA, Context.MODE_PRIVATE)

        val bundle: Bundle? = intent.extras
        username = bundle?.get("username").toString()
        email = bundle?.get("email").toString()
        password = bundle?.get("password").toString()

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


            inputDate.init(inputDate.year, inputDate.month, inputDate.dayOfMonth)
            {   view, year, month, day ->
//                if (month < 10) {
//                    if (day < 10) {
//                        date = "0$day-0$month-$year"
//                    } else {
//                        date = "$day-0$month-$year"
//                    }
//                } else {
//                    if (day < 10) {
//                        date = "0$day-$month-$year"
//                    } else {
//                        date = "$day-$month-$year"
//                    }
//                }

                Log.d("date", date)
            }
            var dateinput = inputDate.dayOfMonth
            var monthinput = inputDate.month + 1
            var yearinput = inputDate.year
            if (monthinput < 10) {
                if (dateinput < 10) {
                    date = "0${dateinput}-0${monthinput}-${yearinput}"
                } else {
                    date = "${dateinput}-0${monthinput}-${yearinput}"
                }
            } else {
                if (dateinput < 10) {
                    date = "0${dateinput}-${monthinput}-${yearinput}"
                } else {
                    date = "${dateinput}-${monthinput}-${yearinput}"
                }
            }
//            date = "${inputDate.dayOfMonth}/${inputDate.month+1}/${inputDate.year}"

            if (nama.isEmpty() || desc.isEmpty() || prioritas.isEmpty()) {
                Toast.makeText(this, "Mohon isi data secara lengkap", Toast.LENGTH_SHORT).show()
            } else {
                addTask(taskid, username, nama, desc, prioritas, date)
                Toast.makeText(this@AddTask, "Task berhasil dibuat", Toast.LENGTH_SHORT).show()
                showMainPage()
//
            }

        }
    }


    fun addTask(taskid: String, username: String, nama: String, desc: String, prioritas: String, date: String) {
        val task = Task(taskid, username, nama, date, prioritas, desc)
        database.child("task").child(taskid).setValue(task)
    }

    fun showMainPage() {
        intent = Intent(this@AddTask, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }
}

