package com.example.tebakgambarliteuas // <-- Pastikan ini sesuai dengan package-mu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CaraMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PENTING: Pastikan ini memanggil "activity_cara_main"
        // Bukan "activity_main"
        setContentView(R.layout.activity_cara_main)
    }
}