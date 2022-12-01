package com.priotask.viewmodel


import com.priotask.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priotask.model.Task


class ListTaskAdapter(var list: ArrayList<Task>): RecyclerView.Adapter<ListTaskAdapter.ListTaskViewHolder>() {
    inner class ListTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasklist, parent, false)
        return ListTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListTaskViewHolder, position:
    Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.nameTask).text = list[position].nama
            findViewById<TextView>(R.id.descTask).text = list[position].note
            findViewById<TextView>(R.id.dateTask).text = list[position].date
            findViewById<TextView>(R.id.prioTask).text = list[position].priority
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}