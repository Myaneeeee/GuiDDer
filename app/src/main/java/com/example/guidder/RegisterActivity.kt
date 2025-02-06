package com.example.guidder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityMainBinding
import com.example.guidder.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.loginTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerActionBtn.setOnClickListener {
            val email = binding.emailETRegister.text.toString().trim()
            val nama = binding.usernameETRegister.text.toString().trim()
            val password = binding.passETRegister.text.toString().trim()

            if (email.isEmpty() || nama.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.registerUser(email, nama, password)
                if (success) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}