package com.priotask.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.priotask.R
import com.priotask.model.Task
import java.util.*
import kotlin.collections.ArrayList
import com.priotask.viewmodel.ListTaskAdapter


private lateinit var textKosong: TextView
private lateinit var textUsername: TextView
private lateinit var textEmail: TextView
private lateinit var listRecyclerView: RecyclerView
private lateinit var listTaskAdapter: ListTaskAdapter
var listTask = ArrayList<Task>()

private lateinit var database: DatabaseReference


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        database = Firebase.database.reference
        textKosong = findViewById(R.id.textKosong)

        if (listTask.size == 0) {
            textKosong.setText("Anda Belum Memiliki Tugas")
        } else {
            textKosong.setText("")
        }

        listTaskAdapter = ListTaskAdapter(listTask)
        listRecyclerView = findViewById(R.id.listTask)
        listRecyclerView.adapter = listTaskAdapter
        listRecyclerView.layoutManager = LinearLayoutManager(this)

        val bundle: Bundle? = intent.extras

        val username = bundle?.get("username").toString()
        val email = bundle?.get("email").toString()
        val password = bundle?.get("password").toString()
        textUsername = findViewById(R.id.username)
        textEmail = findViewById(R.id.email)
        textUsername.setText("Hello, $username")
        textEmail.setText(email)

//        val nama = bundle?.get("nama").toString()
//        val deskripsi = bundle?.get("deskripsi").toString()
//        val prioritas = bundle?.get("prioritas").toString()
//        val date = bundle?.get("date").toString()
//        if (nama == "null" && deskripsi == "null" && prioritas == "null" && date == "null") {
//
//        } else {
//            textKosong.setText("")
//
////            listTask.add(Task(nama, date, prioritas, deskripsi))
//            listRecyclerView.adapter = listTaskAdapter
//            listRecyclerView.layoutManager = LinearLayoutManager(this)
//        }

        getAllTask()

        val buttonAddTaskMain = findViewById<Button>(R.id.buttonAddTaskMain)
        buttonAddTaskMain.setOnClickListener {
            listRecyclerView.adapter = listTaskAdapter
            intent = Intent(this@MainActivity, AddTask::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)

        }

        val imageSettings = findViewById<ImageView>(R.id.settingsImage)
        imageSettings.setOnClickListener{
            intent = Intent(this@MainActivity, Settings::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }


    }

    fun getAllTask() {

        val bundle: Bundle? = intent.extras

        val username = bundle?.get("username").toString()
        val email = bundle?.get("email").toString()

        val databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    textKosong.setText("")
                    listTask.clear()
                    dataSnapshot.children.forEach {
                        val name = it.child("nama").getValue().toString()
                        val date = it.child("date").getValue().toString()
                        val priority = it.child("priority").getValue().toString()
                        val note = it.child("note").getValue().toString()
                        listTask.add(Task(username, name, date, priority, note))
                        Log.d("listtask", name.toString())
                    }

                    listRecyclerView.adapter = listTaskAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        database.child("task").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(databaseTask)
    }
}