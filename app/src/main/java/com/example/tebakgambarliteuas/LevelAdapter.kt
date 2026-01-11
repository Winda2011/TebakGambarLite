package com.example.tebakgambarliteuas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LevelAdapter(
    private val levelList: List<Level>,
    private val onLevelClick: (Level) -> Unit
) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levelList[position]
        holder.bind(level)
    }

    override fun getItemCount(): Int = levelList.size

    inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLevelNumber: TextView = itemView.findViewById(R.id.tvLevelNumber)
        private val ivLockStatus: ImageView = itemView.findViewById(R.id.ivLockStatus)

        fun bind(level: Level) {
            tvLevelNumber.text = "Level ${level.levelNumber}"

            if (level.isUnlocked) {
                ivLockStatus.visibility = View.GONE
                itemView.setOnClickListener { onLevelClick(level) }
            } else {
                ivLockStatus.visibility = View.VISIBLE
                itemView.setOnClickListener(null) // Non-aktifkan klik jika terkunci
                itemView.alpha = 0.6f // Buat tampak redup
            }
        }
    }
}
