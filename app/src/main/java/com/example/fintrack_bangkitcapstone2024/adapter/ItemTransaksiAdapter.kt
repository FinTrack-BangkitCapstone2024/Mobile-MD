package com.example.fintrack_bangkitcapstone2024.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack_bangkitcapstone2024.databinding.CustomDetailDialogBinding
import com.example.fintrack_bangkitcapstone2024.databinding.ItemTransaksiBinding
import com.example.fintrack_bangkitcapstone2024.response.Financial.FinancialData
import com.example.fintrack_bangkitcapstone2024.utils.CurrencyUtil.toRupiah
import com.example.fintrack_bangkitcapstone2024.utils.DateUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemTransaksiAdapter(private val listTransaksi: MutableList<FinancialData>) :
    RecyclerView.Adapter<ItemTransaksiAdapter.TransaksiViewHolder>() {


    // Create a new list for displaying data
    private val displayList: MutableList<FinancialData> = listTransaksi.toMutableList()

    // List untuk menyimpan data asli
//    private var originalList: List<FinancialData> = listTransaksi.toList()

    inner class TransaksiViewHolder(private val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaksi: FinancialData) {
            binding.namaTransaksi.text = transaksi.title
            binding.deskripsiTransaksi.text = transaksi.description
            val jumlah = transaksi.jumlah
            binding.dateTransakasi.text = DateUtils.formatDate(transaksi.tanggal)
            binding.jumlahTransaksi.text = jumlah.toRupiah().replace(",00", "")
            val nameT = transaksi.title.firstOrNull()?.toUpperCase()?.toString() ?: ""
            binding.profileImage.text = nameT
            val color = when (transaksi.tipe) {
                "pengeluaran" -> Color.parseColor("#7BAEDD")
                "pemasukan" -> Color.parseColor("#205E98")
                else -> Color.HSVToColor(
                    floatArrayOf(
                        (transaksi.description.hashCode() * 360 / Int.MAX_VALUE).toFloat(),
                        1f,
                        1f
                    )
                )
            }
            binding.profileImage.setBackgroundColor(color)
            binding.root.setOnClickListener {
                showDialog(transaksi)
            }
        }

        private fun showDialog(transaksi: FinancialData) {
            val context = binding.root.context
            val dialog = BottomSheetDialog(context)
            val dialogBinding = CustomDetailDialogBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(dialogBinding.root)

            // Set values
            dialogBinding.titleTransaksi.text = transaksi.title
            dialogBinding.descriptionTransaksi.text = transaksi.description
            dialogBinding.jumlahTransaksi.text = transaksi.jumlah.toRupiah().replace(",00", "")
            dialogBinding.tanggalTransaksi.text = DateUtils.formatDate(transaksi.tanggal)
            dialogBinding.tipeTransaksi.text = transaksi.tipe
            dialogBinding.idTransaksi.text = transaksi.id


            // Close button
            dialogBinding.btnClose.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val binding =
            ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(binding)
    }

    override fun getItemCount(): Int = listTransaksi.size

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        holder.bind(listTransaksi[position])
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sortByDateAscending() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        listTransaksi.sortBy { LocalDate.parse(it.tanggal, formatter) }
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortByDateDescending() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        listTransaksi.sortByDescending { LocalDate.parse(it.tanggal, formatter) }
        notifyDataSetChanged()
    }


    // Fungsi untuk melakukan sort berdasarkan jumlah dari terbanyak ke terkecil
    fun sortByAmountDescending() {
        listTransaksi.sortByDescending { it.jumlah }
        notifyDataSetChanged()
    }

    // Fungsi untuk melakukan sort berdasarkan jumlah dari terkecil ke terbanyak
    fun sortByAmountAscending() {
        listTransaksi.sortBy { it.jumlah }
        notifyDataSetChanged()
    }
}