package com.example.guidder

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guidder.adapter.ViewPagerAdapter
import com.example.guidder.databinding.ActivityHomeBinding
import com.example.guidder.fragment.ListObjekPariwisata
import com.example.guidder.session.SessionManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        setSupportActionBar(binding.toolbar)

        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val userName = sessionManager.getUserName()
        binding.welcomeLbl.text = "Welcome, $userName"

        binding.logoutBtn.setOnClickListener {
            sessionManager.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        adapter = ViewPagerAdapter(this)
        adapter.addFragment(ListObjekPariwisata())

        binding.vpObjekPariwisataList.adapter = adapter
    }
}