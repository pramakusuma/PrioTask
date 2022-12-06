package com.priotask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

private lateinit var username: String
private lateinit var emailuser: String
private lateinit var passworduser: String

private lateinit var database: DatabaseReference

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.login_page)

        database = Firebase.database.reference

        inputUsername = findViewById(R.id.inputUsername)
        inputPassword = findViewById(R.id.inputPassword)

        val bundle: Bundle? = intent.extras

        username = ""
        var password = ""


        textRegister = findViewById(R.id.textRegister)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        textRegister.setOnClickListener {
            intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            username = inputUsername.text.toString()
            password = inputPassword.text.toString()
            login(username, password)
        }


    }

    fun login(username: String, password: String) {

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username/password wajib diisi", Toast.LENGTH_SHORT).show()

        } else {

            val databaseListener = object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild(username)) {
                        //cek password
                        var getPassword: String? = dataSnapshot.child(username).child("password").getValue<String>()
                        if (getPassword.equals(password)) {
                            Toast.makeText(this@Login, "Login Success!", Toast.LENGTH_SHORT).show()
                            Log.d("usename active", username)
                            emailuser = dataSnapshot.child(username).child("email").getValue<String>().toString()
                            passworduser = dataSnapshot.child(username).child("password").getValue<String>().toString()
                            showMainPage()
                        } else {
                            sendInvalid()
                        }


                    } else {
                        Toast.makeText(this@Login, "User belum terdaftar", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }

            database.child("users").addListenerForSingleValueEvent(databaseListener)
        }
    }

    fun sendInvalid() {
        Toast.makeText(this@Login, "Username/password tidak valid", Toast.LENGTH_SHORT).show()
    }

    fun showMainPage() {
        //to main page
//                                Toast.makeText(this@Login, username, Toast.LENGTH_SHORT).show()
        intent = Intent(this@Login, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", emailuser)
        intent.putExtra("password", passworduser)
        startActivity(intent)
    }
}