package com.example.guidder.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.guidder.R
import com.example.guidder.adapter.ObjekPariwisataAdapter
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.FragmentListObjekPariwisataBinding
import com.example.guidder.model.ObjekPariwisata
import org.json.JSONObject

class ListObjekPariwisata : Fragment() {
    private lateinit var binding : FragmentListObjekPariwisataBinding
    private lateinit var objekPariwisataAdapter: ObjekPariwisataAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListObjekPariwisataBinding.inflate(layoutInflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        requestQueue = Volley.newRequestQueue(context)
        setUpRecycler()

        binding.fetchDataBtn.setOnClickListener {
            showConfirmationDialog()
        }
        return binding.root
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Action")
        builder.setMessage("Are you sure you want to delete all data and fetch data?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            fetchAndStoreData()
            setUpRecycler()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun fetchAndStoreData() {
        val url = "https://api.npoint.io/af67b98ae794348b1cc1"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    databaseHelper.deleteAllObjekPariwisata()
                    databaseHelper.deleteAllFavorites()
                    for (i in 0 until response.length()) {
                        val jsonObj: JSONObject = response.getJSONObject(i)
                        val id = jsonObj.getInt("id_objek_pariwisata")
                        val nama = jsonObj.getString("nama")
                        val gambar = jsonObj.getString("gambar")
                        val deskripsi = jsonObj.getString("deskripsi")
                        val lokasi = jsonObj.getString("lokasi")

                        val newObjekPariwisata = ObjekPariwisata(id, nama, gambar, deskripsi, lokasi)

                        databaseHelper.insertObjekPariwisata(newObjekPariwisata)
                    }
                    setUpRecycler()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }

    private fun setUpRecycler() {
        val listObjekPariwisata = databaseHelper.getAllObjekPariwisata()
        objekPariwisataAdapter = ObjekPariwisataAdapter(requireContext(), listObjekPariwisata)
        binding.objekPariwisataList.adapter = objekPariwisataAdapter
        binding.objekPariwisataList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

}