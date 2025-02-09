package com.example.guidder

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        databaseHelper = DatabaseHelper(this)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back_arrow)
            title = ""
        }

        val id = intent.getIntExtra("id_objek_pariwisata", -1)
        val objekPariwisata = databaseHelper.getObjekPariwisata(id)
        binding.namaObjekTV.text = objekPariwisata?.nama

        val resourceName = objekPariwisata?.gambar?.substringBeforeLast(".")
        val resourceId = this.resources.getIdentifier(resourceName, "drawable", this.packageName)
        binding.gambarIV.setImageResource(resourceId)
        binding.deskripsiTV.text = objekPariwisata?.deskripsi
        binding.lokasiTV.text = String.format("Lokasi: ${objekPariwisata?.lokasi}")

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}