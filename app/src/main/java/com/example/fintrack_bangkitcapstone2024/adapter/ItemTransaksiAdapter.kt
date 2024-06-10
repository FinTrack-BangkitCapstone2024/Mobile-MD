package com.example.fintrack_bangkitcapstone2024.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack_bangkitcapstone2024.databinding.ItemTransaksiBinding
import com.example.fintrack_bangkitcapstone2024.response.Financial.FinancialData

class ItemTransaksiAdapter(private val listTransaksi: List<FinancialData>) :
    RecyclerView.Adapter<ItemTransaksiAdapter.TransaksiViewHolder>() {

    inner class TransaksiViewHolder(private val binding: ItemTransaksiBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaksi: FinancialData) {
//            binding.namaTransaksi.text = transaksi.name
            binding.deskripsiTransaksi.text = transaksi.description
            binding.jumlahTransaksi.text = transaksi.jumlah.toString()
            // Bind other views as needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val binding = ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(binding)
    }

    override fun getItemCount(): Int = listTransaksi.size

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        holder.bind(listTransaksi[position])
    }
}