package com.priotask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.priotask.R

private lateinit var usernameSetting: TextView
private lateinit var emailSetting: TextView
private lateinit var passwordSetting: TextView
private lateinit var textUsername: TextView



class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_page)

        val bundle: Bundle? = intent.extras

        val username = bundle?.get("username").toString()
        val email = bundle?.get("email").toString()
        val password = bundle?.get("password").toString()

        textUsername = findViewById(R.id.textUsername)
        textUsername.setText("Hello, $username")

        usernameSetting = findViewById(R.id.usernameSetting)
        emailSetting = findViewById(R.id.emailSetting)
        passwordSetting = findViewById(R.id.passwordSetting)

        usernameSetting.setText(username)
        emailSetting.setText(email)
        passwordSetting.setText(password)


        passwordSetting.setOnClickListener{
            if (passwordSetting.transformationMethod == null) {
                passwordSetting.setTransformationMethod(PasswordTransformationMethod.getInstance())

            } else {
                passwordSetting.setTransformationMethod(null)

            }
        }

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            intent = Intent(this, Login::class.java)
            startActivity(intent)

        }

    }
}