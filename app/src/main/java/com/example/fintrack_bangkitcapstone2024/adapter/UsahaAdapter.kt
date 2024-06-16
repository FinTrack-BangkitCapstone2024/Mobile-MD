package com.example.fintrack_bangkitcapstone2024.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack_bangkitcapstone2024.R
import com.example.fintrack_bangkitcapstone2024.response.Usaha.UsahaItem

class UsahaAdapter(
    private val listUsaha: List<UsahaItem>,
    private val onUsahaClicked: (String) -> Unit
) :
    RecyclerView.Adapter<UsahaAdapter.UsahaViewHolder>() {

    inner class UsahaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaUsaha: TextView = itemView.findViewById(R.id.tvNamaUsaha)
        var tvDeskripsiUsaha: TextView = itemView.findViewById(R.id.tvDeskripsiUsaha)
        var tvTipeUsaha: TextView = itemView.findViewById(R.id.tv_tipe_usaha)

        fun bind(usaha: UsahaItem) {
            tvNamaUsaha.text = usaha.nama
            tvDeskripsiUsaha.text = usaha.lokasi
            tvTipeUsaha.text = usaha.jenis

            // Handle item click
            itemView.setOnClickListener {
                onUsahaClicked(usaha.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsahaViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_usaha, parent, false)
        return UsahaViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsahaViewHolder, position: Int) {
        val usaha = listUsaha[position]
        holder.bind(usaha)
    }

    override fun getItemCount(): Int {
        return listUsaha.size
    }
}