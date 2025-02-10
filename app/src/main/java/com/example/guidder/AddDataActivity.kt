package com.example.guidder

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityAddDataBinding
import com.example.guidder.model.ObjekPariwisata

class AddDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddDataBinding
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddDataBinding.inflate(layoutInflater)

        dbHelper = DatabaseHelper(this)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back_arrow)
            title = ""
        }

        binding.addBtn.setOnClickListener {
            val nama = binding.namaET.text.toString()
            val image = binding.gambarET.text.toString()
            val desc = binding.descET.text.toString()
            val loc = binding.locationET.text.toString()

            if (nama.isNotEmpty() and image.isNotEmpty() and desc.isNotEmpty() and loc.isNotEmpty()) {
                try {
                    dbHelper.insertObjekPariwisata(ObjekPariwisata(
                        dbHelper.getLatestObjekPariwisataID() + 1,
                        nama,
                        image,
                        desc,
                        loc))
                    Toast.makeText(this, "New data inserted successfully", Toast.LENGTH_SHORT).show()
                }
                catch (e : Exception) {
                    Toast.makeText(this, "Database error, $e", Toast.LENGTH_SHORT).show()
                }

            }
            else {
                Toast.makeText(this, "All field must be filled", Toast.LENGTH_SHORT).show()
            }
        }
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