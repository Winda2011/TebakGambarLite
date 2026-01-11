package com.example.tebakgambarliteuas

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tebakgambarliteuas.databinding.ActivityGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var database: DatabaseReference
    private var soalSekarang: ModelSoal? = null

    // Variabel untuk menyimpan progres
    private var levelNumber: Int = 1
    private var soalNomor: Int = 1
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        levelNumber = intent.getIntExtra("LEVEL_NUMBER", 1)
        soalNomor = intent.getIntExtra("SOAL_NUMBER", 1)

        title = "Level $levelNumber - Soal $soalNomor"

        binding.tvSkor.visibility = View.GONE
        binding.tvNyawa.visibility = View.GONE

        database = FirebaseDatabase.getInstance().getReference("levels")
            .child(levelNumber.toString())
            .child(soalNomor.toString())

        muatSatuSoal()

        binding.btnCek.setOnClickListener {
            cekJawaban()
        }
    }

    private fun muatSatuSoal() {
        binding.ivSoal.visibility = View.INVISIBLE
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                soalSekarang = snapshot.getValue(ModelSoal::class.java)
                if (soalSekarang == null) {
                    Toast.makeText(this@GameActivity, "Soal tidak ditemukan!", Toast.LENGTH_LONG).show()
                    finish()
                    return
                }
                tampilkanGambar(soalSekarang?.deskripsi)
                binding.ivSoal.visibility = View.VISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GameActivity, "Gagal memuat soal: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanGambar(base64String: String?) {
        if (!base64String.isNullOrEmpty()) {
            try {
                val decodedByte = Base64.decode(base64String, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                binding.ivSoal.setImageBitmap(bitmap)
            } catch (e: Exception) {
                binding.ivSoal.setImageResource(R.drawable.logo_tebak_gambar)
            }
        }
    }

    private fun cekJawaban() {
        val jawabanPengguna = binding.etJawaban.text.toString().trim().uppercase()
        val jawabanBenar = "${soalSekarang?.kata1} ${soalSekarang?.kata2}".uppercase()

        if (jawabanPengguna == jawabanBenar) {
            Toast.makeText(this, "JAWABAN BENAR!", Toast.LENGTH_SHORT).show()
            binding.btnCek.isEnabled = false
            
            // --- LOGIKA UTAMA: BUKA KUNCI SOAL BERIKUTNYA ---
            simpanProgresDanBukaSoalBerikutnya()
            
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 1500)
        } else {
            Toast.makeText(this, "Jawaban salah, coba lagi!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun simpanProgresDanBukaSoalBerikutnya() {
        val currentUser = auth.currentUser ?: return

        // Jika soal yg diselesaikan adalah soal terakhir (no. 10), tidak ada yg perlu dibuka lagi.
        if (soalNomor >= 10) return

        // Nomor soal berikutnya yang akan dibuka
        val soalBerikutnyaUntukDibuka = soalNomor + 1

        val progressRef = FirebaseDatabase.getInstance().getReference("user_progress")
            .child(currentUser.uid)
            .child("level_$levelNumber")

        // Cek dulu progres saat ini
        progressRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val progresSaatIni = snapshot.getValue(Int::class.java) ?: 1
                // Hanya update jika progres baru lebih besar dari progres lama
                if (soalBerikutnyaUntukDibuka > progresSaatIni) {
                    progressRef.setValue(soalBerikutnyaUntukDibuka)
                }
            }
            override fun onCancelled(error: DatabaseError) { /* Abaikan */ }
        })
    }
}
