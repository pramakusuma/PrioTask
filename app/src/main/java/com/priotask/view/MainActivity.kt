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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private lateinit var textKosong: TextView
private lateinit var textUsername: TextView
private lateinit var textEmail: TextView
private lateinit var listRecyclerView: RecyclerView
private lateinit var listTaskAdapter: ListTaskAdapter
var listTask = ArrayList<Task>()
var listDate = ArrayList<String>()
var listPriority = ArrayList<String>()
var sortedTime: List<String> = listOf("")
var sortedPriority: List<String> = listOf("")

private lateinit var username: String
private lateinit var email: String
private lateinit var password: String

private lateinit var database: DatabaseReference
private lateinit var databaseTask: ValueEventListener


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

        val bundle: Bundle? = intent.extras

        username = bundle?.get("username").toString()
        email = bundle?.get("email").toString()
        password = bundle?.get("password").toString()

        textUsername = findViewById(R.id.username)
        textEmail = findViewById(R.id.email)
        textUsername.setText("$username")
        textEmail.setText(email)


        getAllTask(username)

        val buttonAddTaskMain = findViewById<Button>(R.id.buttonAddTaskMain)
        buttonAddTaskMain.setOnClickListener {
            listRecyclerView.adapter = listTaskAdapter
            showAddTask()

        }

        val buttonSortAll = findViewById<Button>(R.id.buttonSortAll)
        buttonSortAll.setOnClickListener {
            getAllTask(username)
        }

        val buttonSortTime = findViewById<Button>(R.id.buttonSortTime)
        buttonSortTime.setOnClickListener {
            getTaskByTime(username)
        }

        val buttonSortPriority = findViewById<Button>(R.id.buttonSortPriority)
        buttonSortPriority.setOnClickListener {
            getTaskByPriority(username)
        }

        val imageSettings = findViewById<ImageView>(R.id.settingsImage)
        imageSettings.setOnClickListener{
            showSettings()
        }


    }

    fun getAllTask(username: String) {
        listTask.clear()
        databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    textKosong.setText("")
                    listTask.clear()
                    dataSnapshot.children.forEach {
                        val taskid = it.child("taskid").getValue().toString()
                        val name = it.child("nama").getValue().toString()
                        val date = it.child("date").getValue().toString()
                        val priority = it.child("priority").getValue().toString()
                        val note = it.child("note").getValue().toString()
                        listTask.add(Task(taskid, username, name, date, priority, note))
                        Log.d("listtask", name.toString())
                    }
                    listRecyclerView.adapter = listTaskAdapter
                    listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                } else {
                    showEmpty()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        database.child("task").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(databaseTask)

//        listRecyclerView.adapter = listTaskAdapter
//        listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    fun getTaskByTime(username: String) {
//        var sortedTime: List<String> = listOf("")
        listTask.clear()
        listDate.clear()
        sortedTime = listOf("")
        Log.d("sortTime", listTask.toString())
        val databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    textKosong.setText("")
                    listTask.clear()
                    dataSnapshot.children.forEach {
                        if (it.child("username").getValue().toString().equals(username)) {
                            val date = it.child("date").getValue().toString()
                            if(listDate.contains(date)) {

                            } else {
                                listDate.add(date)

                            }
                        }
                    }
                    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                    sortedTime = listDate.sortedBy {
                        LocalDate.parse(it, dateTimeFormatter)
                    }

                    Log.d("sortedTime", listDate.toString())
                    Log.d("sortedTime", sortedTime.toString())

                    listTask.clear()
                    sortedTime.forEach{
                        var currentDate = it
                        val databaseTask = object: ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    textKosong.setText("")
//                                    listTask.clear()
                                    dataSnapshot.children.forEach {
                                        if (it.child("username").getValue().toString().equals(username)) {
                                            if (it.child("date").getValue().toString().equals(currentDate)) {
                                                val taskid = it.child("taskid").getValue().toString()
                                                val name = it.child("nama").getValue().toString()
                                                val date = it.child("date").getValue().toString()
                                                val priority = it.child("priority").getValue().toString()
                                                val note = it.child("note").getValue().toString()
                                                Log.d("sortedTime", it.toString())
                                                listTask.add(Task(taskid, username, name, date, priority, note))
                                            }
                                        }

                                    }
                                    listRecyclerView.adapter = listTaskAdapter
                                    listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                                } else {
                                    showEmpty()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        }
                        Log.d("sortedTime", it)
                        database.child("task").orderByChild("date").addListenerForSingleValueEvent(databaseTask)
                    }

                } else {
                    showEmpty()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.child("task").orderByChild("date").addListenerForSingleValueEvent(databaseTask)

        listRecyclerView.adapter = listTaskAdapter
        listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

    }

    fun getTaskByPriority(username: String) {
        listTask.clear()
        listPriority.clear()
        sortedPriority = listOf("")
        val databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    textKosong.setText("")
                    listTask.clear()
                    dataSnapshot.children.forEach {
                        if (it.child("username").getValue().toString().equals(username)) {
                            val priority = it.child("priority").getValue().toString()
                            if (listPriority.contains(priority)) {

                            } else {
                                listPriority.add(priority)

                            }
                        }
                    }
                    sortedPriority = listPriority.sortedByDescending {it.length}

                    listTask.clear()
                    sortedPriority.forEach{
                        var currentPriority = it
                        val databaseTask = object: ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    textKosong.setText("")
//                                    listTask.clear()
                                    dataSnapshot.children.forEach {
                                        if (it.child("username").getValue().toString().equals(username)) {
                                            val taskid = it.child("taskid").getValue().toString()
                                            val name = it.child("nama").getValue().toString()
                                            val date = it.child("date").getValue().toString()
                                            val priority = it.child("priority").getValue().toString()
                                            val note = it.child("note").getValue().toString()
                                            Log.d("sortedTime", it.toString())
                                            listTask.add(Task(taskid, username, name, date, priority, note))

                                        }

                                    }
                                    listRecyclerView.adapter = listTaskAdapter
                                    listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                                } else {
                                    showEmpty()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        }
                        Log.d("sortedPrio", it)
                        database.child("task").orderByChild("priority").equalTo(currentPriority).addListenerForSingleValueEvent(databaseTask)
                    }


                } else {
                    showEmpty()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        database.child("task").orderByChild("priority").addListenerForSingleValueEvent(databaseTask)

        listRecyclerView.adapter = listTaskAdapter
        listRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

    }

    fun showAddTask() {
        intent = Intent(this, AddTask::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    fun showEmpty() {
        textKosong.setText("Anda belum membuat pengingat task")
    }

    fun showSettings() {
        intent = Intent(this, Settings::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("tasks", listTask.toString())
        startActivity(intent)
    }
}