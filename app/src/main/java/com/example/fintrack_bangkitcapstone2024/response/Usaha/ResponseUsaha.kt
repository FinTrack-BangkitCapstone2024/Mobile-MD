package com.example.fintrack_bangkitcapstone2024.response.Usaha

import com.google.gson.annotations.SerializedName

data class ResponseUsaha(

	val code: Int,

	val data: Data,

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

//	@field:SerializedName("financials")
//	val financials: List<FinancialsItem>,
//
//	@field:SerializedName("balance")
//	val balance: Int,
//
//	@field:SerializedName("nama")
//	val nama: String,
//
//	@field:SerializedName("logo_path")
//	val logoPath: String,
//
//	@field:SerializedName("lokasi")
//	val lokasi: Lokasi,
//
//	@field:SerializedName("nama_usaha")
//	val namaUsaha: String,
//
//	@field:SerializedName("jenis")
//	val jenis: String,

	@field:SerializedName("id")
	val id: String,

//	@field:SerializedName("deskripsi")
//	val deskripsi: String,
//
//	@field:SerializedName("user")
//	val user: User
)

data class FinancialsItem(

	@field:SerializedName("jumlah")
	val jumlah: String,

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

data class Lokasi(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("alamat")
	val alamat: String
)
