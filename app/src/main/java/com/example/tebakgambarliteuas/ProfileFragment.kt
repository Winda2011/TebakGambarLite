package com.example.tebakgambarliteuas // Pastikan package ini sesuai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val tvEmail = view.findViewById<TextView>(R.id.tvUserEmailProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogoutProfile)

        // Tampilkan Email
        if (currentUser != null) {
            tvEmail.text = "Login sebagai:\n${currentUser.email}"
        }

        // LOGIKA LOGOUT (Pindahan dari MainActivity)
        btnLogout.setOnClickListener {
            // 1. Logout dari Firebase
            auth.signOut()

            // 2. Logout dari Google
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1078379996714-h2j2tuf5j293bihl7j82f2ot4llcpmlj.apps.googleusercontent.com")
                .requestEmail()
                .build()

            // Perhatikan penggunaan 'requireActivity()' karena kita di dalam Fragment
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

            googleSignInClient.signOut().addOnCompleteListener {
                // 3. Pindah ke LoginActivity
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                // Hapus history activity agar tidak bisa di-back
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}