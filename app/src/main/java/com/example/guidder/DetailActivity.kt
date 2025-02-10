package com.example.guidder

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
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

        binding.editBtn.setOnClickListener {
            val intent = Intent(this, EditDataActivity::class.java)
            intent.putExtra("id_objek_pariwisata", id)
            startActivity(intent)
        }

        binding.deleteBtn.setOnClickListener {
            showConfirmationDialog(id)
        }

    }

    private fun showConfirmationDialog(id : Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Action")
        builder.setMessage("Are you sure you want to delete this data?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            try {
                databaseHelper.deleteObjekPariwisata(id)
                Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
            catch (e: Exception) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                Log.e("Error Delete" , "$e")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
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