package com.priotask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.priotask.R

private lateinit var inputUsername: TextInputEditText
private lateinit var inputPassword: TextInputEditText
private lateinit var textRegister: TextView

private lateinit var database: DatabaseReference

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        database = Firebase.database.reference

        inputUsername = findViewById(R.id.inputUsername)
        inputPassword = findViewById(R.id.inputPassword)

        textRegister = findViewById(R.id.textRegister)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        textRegister.setOnClickListener {
            intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {

            var username: String = inputUsername.text.toString()
            var password: String = inputPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please insert the field!", Toast.LENGTH_SHORT).show()

            } else {

                val databaseListener = object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild(username)) {
                            //cek password
                            var getPassword: String? = dataSnapshot.child(username).child("password").getValue<String>()
                            if (getPassword.equals(password)) {
                                Toast.makeText(this@Login, "Login Success!", Toast.LENGTH_SHORT).show()

                                //to main page
                                intent = Intent(this@Login, MainActivity::class.java)
                                intent.putExtra("username", username)
                                intent.putExtra("email", dataSnapshot.child(username).child("email").getValue<String>())
                                intent.putExtra("password", dataSnapshot.child(username).child("password").getValue<String>())
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@Login, "Password incorrect!", Toast.LENGTH_SHORT).show()
                            }


                        } else {
                            Toast.makeText(this@Login, "User not registered!", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }

                database.child("users").addListenerForSingleValueEvent(databaseListener)
            }

        }


    }
}