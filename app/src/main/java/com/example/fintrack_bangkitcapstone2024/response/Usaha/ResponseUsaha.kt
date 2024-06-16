package com.example.fintrack_bangkitcapstone2024.response.Usaha

import com.google.gson.annotations.SerializedName

data class ResponseUsaha(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)

data class Data(

	@field:SerializedName("financials")
	val financials: List<FinancialsItem>,

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
	val totalPengeluaran: Int,

	@field:SerializedName("user")
	val user: User
)

data class FinancialsItem(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("tipe")
	val tipe: String,

	@field:SerializedName("usaha_id")
	val usahaId: String
)
