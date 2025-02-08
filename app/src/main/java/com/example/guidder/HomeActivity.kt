package com.example.guidder

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.guidder.adapter.ViewPagerAdapter
import com.example.guidder.databinding.ActivityHomeBinding
import com.example.guidder.fragment.ListObjekPariwisata
import com.example.guidder.session.SessionManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var sessionManager: SessionManager
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
        }

        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val username = sessionManager.getUserName()
        binding.welcomeLbl.text = "Welcome, $username"

        // **Check current theme**
        isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        updateThemeIcon()
        updateLogo()

        // **Theme Toggle Button Click**
        binding.themeToggleBtn.setOnClickListener {
            toggleTheme()
        }

        // **ViewPager Adapter**
        adapter = ViewPagerAdapter(this)
        adapter.addFragment(ListObjekPariwisata())

        binding.vpObjekPariwisataList.adapter = adapter
    }

    // **Create Menu (3 dots)**
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // **Handle Menu Clicks**
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fetch_json -> {
                // Placeholder for Fetch JSON action
                return true
            }
            R.id.action_logout -> {
                sessionManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        updateThemeIcon()
        updateLogo()
    }

    private fun updateThemeIcon() {
        if (isDarkTheme) {
            binding.themeToggleBtn.setImageResource(R.drawable.icon_lightmode)
        } else {
            binding.themeToggleBtn.setImageResource(R.drawable.icon_darkmode)
        }
    }

    private fun updateLogo() {
        if (isDarkTheme) {
            binding.logoIV.setImageResource(R.drawable.guidder_logo_dark)
        } else {
            binding.logoIV.setImageResource(R.drawable.guidder_logo)
        }
    }
}
