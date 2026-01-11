package com.example.tebakgambarliteuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

// Activity ini sekarang berfungsi sebagai "Kotak Masuk" untuk soal dari user
class ListSoalActivity : AppCompatActivity() {

    private lateinit var rvSoal: RecyclerView
    private lateinit var adapter: SoalAdminAdapter
    // --- LOKASI BARU: Arahkan ke soal kiriman user ---
    private lateinit var database: DatabaseReference
    private lateinit var fabTambah: FloatingActionButton
    private val soalList = mutableListOf<ModelSoal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_soal)
        title = "Review Soal dari User"

        rvSoal = findViewById(R.id.rvSoal)
        fabTambah = findViewById(R.id.fabTambah)
        rvSoal.layoutManager = LinearLayoutManager(this)

        adapter = SoalAdminAdapter(soalList, {
            // --- LOGIKA "PROMOSIKAN SOAL" ---
            // Buka AddEditSoalActivity dengan data dari soal user
            val intent = Intent(this, AddEditSoalActivity::class.java).apply {
                putExtra("EXTRA_KATA1", it.kata1)
                putExtra("EXTRA_KATA2", it.kata2)
                putExtra("EXTRA_DESKRIPSI_BASE64", it.deskripsi)
            }
            Toast.makeText(this, "Silakan tentukan Level dan Nomor Soal", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }, {
            // --- LOGIKA HAPUS KIRIMAN USER ---
            tampilkanDialogKonfirmasiHapus(it)
        })
        rvSoal.adapter = adapter

        // Tombol ini sekarang berfungsi untuk menambah soal secara manual oleh admin
        fabTambah.setOnClickListener {
            startActivity(Intent(this, AddEditSoalActivity::class.java))
        }

        // Arahkan ke node 'user_submissions'
        database = FirebaseDatabase.getInstance().getReference("user_submissions")
        bacaDataSoal()
    }

    private fun bacaDataSoal() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                soalList.clear()
                for (data in snapshot.children) {
                    val soal = data.getValue(ModelSoal::class.java)
                    if (soal != null) {
                        soal.id = data.key // ID unik dari soal kiriman user
                        soalList.add(soal)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListSoalActivity, "Gagal memuat data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun tampilkanDialogKonfirmasiHapus(soal: ModelSoal) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Yakin ingin menolak dan menghapus usulan soal ini?")
            .setPositiveButton("Hapus") { _, _ ->
                hapusSoalDariDatabase(soal)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun hapusSoalDariDatabase(soal: ModelSoal) {
        soal.id?.let {
            database.child(it).removeValue().addOnSuccessListener {
                Toast.makeText(this, "Usulan soal ditolak dan dihapus", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal menghapus soal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}