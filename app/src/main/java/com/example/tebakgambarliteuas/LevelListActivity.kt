package com.example.tebakgambarliteuas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelListActivity : AppCompatActivity() {

    private lateinit var rvLevel: RecyclerView
    private lateinit var levelAdapter: LevelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_list)

        rvLevel = findViewById(R.id.rvLevel)
        rvLevel.layoutManager = LinearLayoutManager(this)

        val levels = createDummyLevels()
        levelAdapter = LevelAdapter(levels) { level ->
            // Pindah ke halaman Grid Soal, kirim nomor level
            // (Halaman ini akan kita buat selanjutnya)
            val intent = Intent(this, SoalGridActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", level.levelNumber)
            startActivity(intent)
        }
        rvLevel.adapter = levelAdapter
    }

    private fun createDummyLevels(): List<Level> {
        val levels = mutableListOf<Level>()
        // Asumsi ada 10 Level, dan hanya level 1 yang terbuka
        // Nantinya, status unlocked ini bisa disimpan di SharedPreferences atau Firebase
        for (i in 1..10) {
            levels.add(Level(levelNumber = i, isUnlocked = (i == 1)))
        }
        return levels
    }
}
