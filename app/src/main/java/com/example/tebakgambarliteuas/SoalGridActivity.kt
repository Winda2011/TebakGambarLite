package com.example.tebakgambarliteuas

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SoalGridActivity : AppCompatActivity() {

    private lateinit var rvSoalGrid: RecyclerView
    private lateinit var tvLevelTitle: TextView
    private lateinit var soalGridAdapter: SoalGridAdapter
    private var levelNumber: Int = 1

    // Variabel untuk Firebase
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soal_grid)

        levelNumber = intent.getIntExtra("LEVEL_NUMBER", 1)

        tvLevelTitle = findViewById(R.id.tvLevelTitle)
        tvLevelTitle.text = "LEVEL $levelNumber"

        rvSoalGrid = findViewById(R.id.rvSoalGrid)
        rvSoalGrid.layoutManager = GridLayoutManager(this, 4)

        // --- PERBAIKAN --- 
        // Inisialisasi adapter dengan 2 parameter: total soal dan soal yg awalnya terbuka (default 1)
        soalGridAdapter = SoalGridAdapter(10, 1) { soalNomor ->
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("LEVEL_NUMBER", levelNumber)
                putExtra("SOAL_NUMBER", soalNomor)
            }
            startActivity(intent)
        }
        rvSoalGrid.adapter = soalGridAdapter

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    // Kita baca progres di onResume agar selalu update saat user kembali ke activity ini
    override fun onResume() {
        super.onResume()
        bacaProgresUser()
    }

    private fun bacaProgresUser() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Sesi berakhir, silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Path ke progres user untuk level ini: user_progress/{userId}/level_{levelId}
        database = FirebaseDatabase.getInstance().getReference("user_progress")
            .child(currentUser.uid)
            .child("level_$levelNumber")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // `soalTerbuka` adalah nomor soal tertinggi yang sudah bisa dikerjakan.
                // Jika belum ada progres (null), defaultnya adalah soal nomor 1.
                val soalTerbuka = snapshot.getValue(Int::class.java) ?: 1
                soalGridAdapter.updateSoalTerbuka(soalTerbuka)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SoalGridActivity, "Gagal memuat progres level.", Toast.LENGTH_SHORT).show()
                // Jika gagal, setidaknya soal 1 tetap terbuka
                soalGridAdapter.updateSoalTerbuka(1)
            }
        })
    }
}
