package com.example.tebakgambarliteuas

import com.google.firebase.database.Exclude

// Model ini sekarang lebih sederhana, hanya berisi data inti soal.
// Level dan Nomor Soal ditentukan oleh lokasinya di database.
data class ModelSoal(
    @get:Exclude
    var id: String? = null,      // Ini akan berisi Nomor Soal (1-10)
    var kata1: String? = null,
    var kata2: String? = null,
    var deskripsi: String? = null, // Ini berisi string Base64 dari gambar
    // Properti username, email, dan level dihapus karena tidak lagi relevan di struktur ini.
    var username: String? = null // Tetap simpan untuk soal dari user
) {
    // Konstruktor kosong yang diperlukan oleh Firebase
    constructor() : this(null, null, null, null, null)
}