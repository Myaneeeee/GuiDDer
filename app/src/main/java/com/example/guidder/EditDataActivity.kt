package com.example.guidder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityEditDataBinding
import com.example.guidder.model.ObjekPariwisata

class EditDataActivity : AppCompatActivity() {
    private lateinit var binding :ActivityEditDataBinding
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        dbHelper = DatabaseHelper(this)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back_arrow)
            title = ""
        }

        val id = intent.getIntExtra("id_objek_pariwisata", -1)
        val objekPariwisata = dbHelper.getObjekPariwisata(id)

        binding.namaET.setText(objekPariwisata?.nama)
        binding.gambarET.setText(objekPariwisata?.gambar)
        binding.descET.setText(objekPariwisata?.deskripsi)
        binding.locationET.setText(objekPariwisata?.lokasi)

        binding.editBtn.setOnClickListener {
            val nama = binding.namaET.text.toString()
            val image = binding.gambarET.text.toString()
            val desc = binding.descET.text.toString()
            val loc = binding.locationET.text.toString()

            if (nama.isNotEmpty() and image.isNotEmpty() and desc.isNotEmpty() and loc.isNotEmpty()) {
                try {
                    dbHelper.updateObjekPariwisata(
                        ObjekPariwisata(
                            id,
                            nama,
                            image,
                            desc,
                            loc)
                    )
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                }
                catch (e : Exception) {
                    Toast.makeText(this, "Database error, $e", Toast.LENGTH_SHORT).show()
                    Log.e("Update Error", "$e")
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