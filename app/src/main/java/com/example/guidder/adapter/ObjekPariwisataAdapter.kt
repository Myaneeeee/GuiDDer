package com.example.guidder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guidder.DetailActivity
import com.example.guidder.databinding.ObjekPariwisataCardBinding
import com.example.guidder.model.ObjekPariwisata

class ObjekPariwisataAdapter(private val context: Context, private val itemList : List<ObjekPariwisata>) : RecyclerView.Adapter<ObjekPariwisataAdapter.ViewHolder>(){

    class ViewHolder(val context: Context, val binding: ObjekPariwisataCardBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context,
            ObjekPariwisataCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val objekPariwisata = itemList[position]
        val resourceId = context.resources.getIdentifier(objekPariwisata.gambar, "drawable", context.packageName)

        holder.binding.tvNama.text = objekPariwisata.nama
        holder.binding.ivGambar.setImageResource(resourceId)
        holder.binding.cvObjekPariwisata.setOnClickListener {
            val intent = Intent(holder.context, DetailActivity::class.java)
            intent.putExtra("id_objek_wisata", objekPariwisata.id_objek_wisata)
            context.startActivity(intent)
        }
    }

}