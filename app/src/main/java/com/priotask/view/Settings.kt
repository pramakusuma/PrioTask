package com.priotask.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.priotask.R
import java.lang.Exception

private lateinit var usernameSetting: TextView
private lateinit var emailSetting: TextView
private lateinit var passwordSetting: TextView
private lateinit var textUsername: TextView

private lateinit var username: String
private lateinit var email: String
private lateinit var password: String




class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_page)

        val bundle: Bundle? = intent.extras

        username = bundle?.get("username").toString()
        email = bundle?.get("email").toString()
        password = bundle?.get("password").toString()
        var tasks = bundle?.get("tasks").toString()

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
            logout()
        }

        val buttonBackup = findViewById<Button>(R.id.buttonBackup)
        buttonBackup.setOnClickListener {
            startBackupData(email, "PrioTask Backup", tasks)
//            Toast.makeText(this, "Backup Success!", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("IntentReset")
    fun startBackupData(email: String, subject: String, message: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.setData(Uri.parse("mailto:"))
        mIntent.setType("text/plain")
//        mIntent.data = Uri.parse("mailto:")
//        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, email)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
//            startActivity(Intent.createChooser(mIntent, "Backup to..."))
            startActivity(mIntent)
            showBackupSuccess()
        } catch(e: Exception) {
            showBackupFailed()
        }
    }

    fun logout() {
        intent = Intent(this, Login::class.java)
        intent.putExtra("username", "")
        intent.putExtra("email", "")
        intent.putExtra("password", "")
        startActivity(intent)
    }

    fun showPagePengunjung() {

    }

    fun showBackupSuccess() {
        Toast.makeText(this, "Backup Success!", Toast.LENGTH_SHORT).show()
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    fun showBackupFailed() {
        Toast.makeText(this, "Terdapat gangguan koneksi, silakan coba lagi", Toast.LENGTH_SHORT).show()

    }
}