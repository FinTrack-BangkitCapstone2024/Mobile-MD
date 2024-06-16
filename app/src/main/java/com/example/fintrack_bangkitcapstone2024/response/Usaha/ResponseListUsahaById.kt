package com.example.fintrack_bangkitcapstone2024.response.Usaha

import com.google.gson.annotations.SerializedName

data class ResponseListUsahaById(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DataListUsaha,

	@field:SerializedName("status")
	val status: String
)

data class UsahaItem(

	@field:SerializedName("total_pemasukan")
	val totalPemasukan: Int,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("balance")
	val balance: Int,

	@field:SerializedName("logo_path")
	val logoPath: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("total_pengeluaran")
	val totalPengeluaran: Int
)

data class DataListUsaha(

	@field:SerializedName("usaha")
	val usaha: List<UsahaItem>
)
