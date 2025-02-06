package com.example.guidder

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guidder.adapter.LandingVPAdapter
import com.example.guidder.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.landingVP.adapter = LandingVPAdapter(this)

        TabLayoutMediator(binding.tabIndicator, binding.landingVP) { tab, position -> }.attach()

        binding.landingVP.setPageTransformer { page, position ->
            page.alpha = 0.5f + (1 - abs(position)) * 0.5f
        }

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}