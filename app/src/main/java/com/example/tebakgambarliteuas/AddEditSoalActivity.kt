package com.example.tebakgambarliteuas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tebakgambarliteuas.databinding.ActivityAddEditSoalBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class AddEditSoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditSoalBinding
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditSoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Database root sekarang adalah /levels
        database = FirebaseDatabase.getInstance().getReference("levels")

        binding.btnPilihGambar.setOnClickListener {
            pilihGambarDariGaleri()
        }

        binding.btnSimpan.setOnClickListener {
            simpanSoalKeSlot()
        }
    }

    private fun pilihGambarDariGaleri() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data?.data
            binding.ivGambarPreview.setImageURI(imageUri)
        }
    }

    private fun simpanSoalKeSlot() {
        val levelStr = binding.etLevel.text.toString().trim()
        val nomorSoalStr = binding.etNomorSoal.text.toString().trim()
        val kata1 = binding.etKata1.text.toString().trim()
        val kata2 = binding.etKata2.text.toString().trim()

        if (levelStr.isEmpty() || nomorSoalStr.isEmpty() || kata1.isEmpty() || kata2.isEmpty()) {
            Toast.makeText(this, "Level, No. Soal, dan Jawaban wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val level = levelStr.toIntOrNull()
        val nomorSoal = nomorSoalStr.toIntOrNull()

        if (level == null || level !in 1..10 || nomorSoal == null || nomorSoal !in 1..10) {
            Toast.makeText(this, "Level dan No. Soal harus antara 1-10", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Silakan pilih gambar untuk soal", Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(true)

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val base64String = bitmapToBase64(bitmap)

            // Buat objek soal yang sederhana
            val soal = ModelSoal(id = nomorSoal.toString(), kata1 = kata1, kata2 = kata2, deskripsi = base64String, username = "admin")

            // Simpan soal ke path yang spesifik: /levels/{level}/{nomorSoal}
            database.child(level.toString()).child(nomorSoal.toString()).setValue(soal)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil disimpan ke Level $level, Soal No. $nomorSoal", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { 
                    Toast.makeText(this, "Gagal menyimpan ke database", Toast.LENGTH_SHORT).show()
                    setLoading(false)
                }

        } catch (e: Exception) {
            Toast.makeText(this, "Gagal mengolah gambar", Toast.LENGTH_SHORT).show()
            setLoading(false)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnSimpan.isEnabled = !isLoading
        binding.btnSimpan.text = if (isLoading) "MENYIMPAN..." else "SIMPAN SOAL"
    }
}
