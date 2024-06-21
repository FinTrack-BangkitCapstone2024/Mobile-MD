package com.example.fintrack_bangkitcapstone2024.response.Usaha

import com.google.gson.annotations.SerializedName

data class ResponseReportToday(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DataToday,

	@field:SerializedName("status")
	val status: String
)

data class Pemasukan(

	@field:SerializedName("jumlah_sebelum")
	val jumlahSebelum: Int,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("selisih")
	val selisih: Int,

	@field:SerializedName("persentase")
	val persentase: String
)

data class Pengeluaran(

	@field:SerializedName("jumlah_sebelum")
	val jumlahSebelum: Int,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("selisih")
	val selisih: Int,

	@field:SerializedName("persentase")
	val persentase: String
)

data class DataToday(

	@field:SerializedName("pemasukan")
	val pemasukan: Pemasukan,

	@field:SerializedName("pengeluaran")
	val pengeluaran: Pengeluaran
)
