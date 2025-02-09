package com.example.guidder

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.guidder.adapter.ObjekPariwisataAdapter
import com.example.guidder.adapter.ViewPagerAdapter
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityHomeBinding
import com.example.guidder.databinding.FragmentListObjekPariwisataBinding
import com.example.guidder.fragment.ListObjekPariwisata
import com.example.guidder.model.ObjekPariwisata
import com.example.guidder.session.SessionManager
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var databaseHelper: DatabaseHelper
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        databaseHelper = DatabaseHelper(this)
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

        isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        updateThemeIcon()
        updateLogo()

        binding.themeToggleBtn.setOnClickListener {
            toggleTheme()
        }

        adapter = ViewPagerAdapter(this)
        adapter.addFragment(ListObjekPariwisata())

        binding.vpObjekPariwisataList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
