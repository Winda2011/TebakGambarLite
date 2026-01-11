package com.example.tebakgambarliteuas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetailSoalActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    // private lateinit var etEmail: EditText // Dihapus
    private lateinit var etKata1: EditText
    private lateinit var etKata2: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_soal)

        initView()

        val id = intent.getStringExtra("id")
        etUsername.setText(intent.getStringExtra("username"))
        // etEmail.setText(intent.getStringExtra("email")) // Dihapus
        etKata1.setText(intent.getStringExtra("kata1"))
        etKata2.setText(intent.getStringExtra("kata2"))
        etDeskripsi.setText(intent.getStringExtra("deskripsi"))

        btnUpdate.setOnClickListener {
            updateData(id)
        }

        btnDelete.setOnClickListener {
            deleteData(id)
        }
    }

    private fun initView() {
        etUsername = findViewById(R.id.etEditUsername)
        // Temukan dan sembunyikan EditText untuk email jika ada
        findViewById<EditText>(R.id.etEditEmail)?.visibility = View.GONE
        etKata1 = findViewById(R.id.etEditKata1)
        etKata2 = findViewById(R.id.etEditKata2)
        etDeskripsi = findViewById(R.id.etEditDeskripsi)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun updateData(id: String?) {
        if (id == null) return

        val database = FirebaseDatabase.getInstance().getReference("SoalPengguna").child(id)

        val soalUpdate = mapOf(
            "id" to id,
            "username" to etUsername.text.toString(),
            // "email" to etEmail.text.toString(), // Dihapus
            "kata1" to etKata1.text.toString(),
            "kata2" to etKata2.text.toString(),
            "deskripsi" to etDeskripsi.text.toString()
        )

        database.updateChildren(soalUpdate).addOnSuccessListener {
            Toast.makeText(this, "Data Berhasil Di-UPDATE!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Update", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData(id: String?) {
        if (id == null) return

        val database = FirebaseDatabase.getInstance().getReference("SoalPengguna").child(id)

        database.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Data Berhasil Di-HAPUS (DELETE)!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
