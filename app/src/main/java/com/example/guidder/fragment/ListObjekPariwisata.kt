package com.example.guidder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guidder.R
import com.example.guidder.adapter.ObjekPariwisataAdapter
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.FragmentListObjekPariwisataBinding

class ListObjekPariwisata : Fragment() {
    private lateinit var binding : FragmentListObjekPariwisataBinding
    private lateinit var objekPariwisataAdapter: ObjekPariwisataAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListObjekPariwisataBinding.inflate(layoutInflater, container, false)

        setUpRecycler()

        return binding.root
    }

    private fun setUpRecycler() {
        val listObjekPariwisata = databaseHelper.getObjekPariwisata()
        objekPariwisataAdapter = ObjekPariwisataAdapter(requireContext(), listObjekPariwisata)
        binding.objekPariwisataList.adapter = objekPariwisataAdapter
        binding.objekPariwisataList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

}