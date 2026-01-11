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
import com.example.tebakgambarliteuas.databinding.ActivityKirimSoalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class KirimSoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKirimSoalBinding
    private lateinit var auth: FirebaseAuth
    // --- LOKASI BARU: Kotak masuk untuk soal dari user ---
    private lateinit var database: DatabaseReference

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKirimSoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        // Arahkan ke node baru 'user_submissions'
        database = FirebaseDatabase.getInstance().getReference("user_submissions")

        val currentUser = auth.currentUser
        binding.etUsername.setText(currentUser?.displayName ?: "")
        binding.etEmailPengirim.setText(currentUser?.email ?: "")

        binding.btnPilihGambar.setOnClickListener {
            pilihGambarDariGaleri()
        }

        binding.btnKirimData.setOnClickListener {
            simpanSoalUntukReview()
        }
    }

    private fun pilihGambarDariGaleri() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data?.data
            binding.ivGambarPreview.setImageURI(imageUri)
        }
    }

    private fun simpanSoalUntukReview() {
        val username = binding.etUsername.text.toString().trim()
        val emailPengirim = binding.etEmailPengirim.text.toString().trim()
        val kata1 = binding.etKata1.text.toString().trim()
        val kata2 = binding.etKata2.text.toString().trim()

        if (username.isEmpty() || kata1.isEmpty() || kata2.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Semua field dan gambar wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(true)

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val base64String = bitmapToBase64(bitmap)

            val newSubmissionId = database.push().key
            val soalSubmission = ModelSoal(id = newSubmissionId, kata1 = kata1, kata2 = kata2, deskripsi = base64String, username = username)
            
            if (newSubmissionId != null) {
                database.child(newSubmissionId).setValue(soalSubmission).addOnSuccessListener {
                    Toast.makeText(this, "Soal berhasil dikirim untuk direview admin!", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    setLoading(false)
                    Toast.makeText(this, "Gagal mengirim soal.", Toast.LENGTH_SHORT).show()
                }
            } else {
                setLoading(false)
                Toast.makeText(this, "Gagal mendapatkan ID unik.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            setLoading(false)
            Toast.makeText(this, "Gagal mengolah gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnKirimData.isEnabled = !isLoading
        binding.btnKirimData.text = if (isLoading) "MENGIRIM..." else "KIRIM"
    }
}
