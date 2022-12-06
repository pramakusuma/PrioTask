package com.priotask.viewmodel


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.priotask.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.priotask.model.Task
import com.priotask.view.*


class ListTaskAdapter(var list: ArrayList<Task>): RecyclerView.Adapter<ListTaskAdapter.ListTaskViewHolder>() {
    inner class ListTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasklist, parent, false)
        return ListTaskViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListTaskViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.nameTask).text = list[position].nama
            findViewById<TextView>(R.id.descTask).text = list[position].note
            findViewById<TextView>(R.id.dateTask).text = list[position].date
            findViewById<TextView>(R.id.prioTask).text = list[position].priority
        }
        holder.itemView.setOnClickListener{
            showEditForm(it, position)
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }

    fun showEditForm(it: View, position: Int) {
        val database : DatabaseReference = Firebase.database.reference
        var email= ""
        var password= ""

        val databaseTask = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                email = dataSnapshot.child("email").getValue().toString()
                password = dataSnapshot.child("password").getValue().toString()
                Log.d("email", email)
                val intent = Intent(it.context, EditTask::class.java)

                intent.putExtra("username", list[position].username)
                intent.putExtra("taskid", list[position].taskid.toString())
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                it.context.startActivity(intent)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        database.child("users").child(list[position].username).addListenerForSingleValueEvent(databaseTask)

    }

}