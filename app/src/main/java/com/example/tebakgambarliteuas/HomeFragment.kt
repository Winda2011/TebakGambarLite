package com.example.tebakgambarliteuas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // --- TOMBOL "AYO MAIN" ---
        val btnAyoMain = view.findViewById<Button>(R.id.btnAyoMain)
        btnAyoMain.setOnClickListener {
            // DIUBAH: Sekarang membuka halaman daftar level
            val intent = Intent(requireActivity(), LevelListActivity::class.java)
            startActivity(intent)
        }

        // --- TOMBOL "CARA MAIN" ---
        try {
            val btnCaraMain = view.findViewById<Button>(R.id.btnCaraMain)
            btnCaraMain.setOnClickListener {
                val intent = Intent(requireActivity(), CaraMainActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {}

        // --- TOMBOL "KIRIM SOAL" ---
        try {
            val btnKirimSoal = view.findViewById<Button>(R.id.btnKirimSoal)
            btnKirimSoal.setOnClickListener {
                val intent = Intent(requireActivity(), KirimSoalActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {}

        // --- TOMBOL PROFIL ---
        val btnProfile = view.findViewById<ImageButton>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // --- TOMBOL ADMIN PANEL ---
        val btnAdminPanel = view.findViewById<ImageButton>(R.id.btnLihatData)
        val currentUser = auth.currentUser
        val emailAdmin = "izmulmuflih@gmail.com"

        if (currentUser?.email == emailAdmin) {
            btnAdminPanel.visibility = View.VISIBLE
            btnAdminPanel.setOnClickListener {
                val intent = Intent(requireActivity(), ListSoalActivity::class.java)
                startActivity(intent)
                Toast.makeText(context, "Mode Admin Aktif 🛠️", Toast.LENGTH_SHORT).show()
            }
        } else {
            btnAdminPanel.visibility = View.GONE
        }
    }
}