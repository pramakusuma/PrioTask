package com.priotask.model

import com.google.firebase.database.Exclude
import java.sql.Timestamp
import java.util.*

data class Task(val taskid: Int, val username: String, val nama: String, val date: String, val priority: String, val note: String) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "nama" to nama,
            "date" to date,
            "priority" to priority,
            "note" to note,
        )
    }

}

