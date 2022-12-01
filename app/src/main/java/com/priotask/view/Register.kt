package com.priotask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.priotask.R
import com.priotask.model.User

private lateinit var inputEmail: TextInputEditText
private lateinit var inputUsername: TextInputEditText
private lateinit var inputPassword: TextInputEditText

private lateinit var database: DatabaseReference

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        database = Firebase.database.reference

        inputEmail = findViewById(R.id.inputEmail)
        inputUsername = findViewById(R.id.inputUsername)
        inputPassword = findViewById(R.id.inputPassword)

        var buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener{

            var email: String = inputEmail.text.toString()
            var username: String = inputUsername.text.toString()
            var password: String = inputPassword.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please insert the field!", Toast.LENGTH_SHORT).show()

            } else {

                //cek akun double
                val databaseListener = object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild(username)) {
                            Toast.makeText(this@Register, "User already registered!", Toast.LENGTH_SHORT).show()
                        } else {
                            //add user
                            addUser(email, username, password)
                            Toast.makeText(this@Register, "Register Success!", Toast.LENGTH_SHORT).show()

                            //ke login page
                            intent = Intent(this@Register, Login::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }

                database.child("users").addListenerForSingleValueEvent(databaseListener)
            }
        }
    }

    fun addUser(email: String, username: String, password: String) {
        val user = User(email, username, password)

        database.child("users").child(username).setValue(user)
    }
}