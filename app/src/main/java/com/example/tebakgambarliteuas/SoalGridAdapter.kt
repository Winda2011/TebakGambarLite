package com.example.tebakgambarliteuas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoalGridAdapter(
    private val totalSoal: Int,
    private var soalTerbuka: Int,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SoalGridAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomor: TextView = view.findViewById(R.id.tvSoalNumber)
        val ivGembok: ImageView = view.findViewById(R.id.ivLock)
        val container: View = view.findViewById(R.id.container_grid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soal_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomorSoal = position + 1
        holder.tvNomor.text = nomorSoal.toString()

        if (nomorSoal <= soalTerbuka) {
            // Soal Terbuka
            holder.tvNomor.visibility = View.VISIBLE
            holder.ivGembok.visibility = View.GONE
            holder.container.alpha = 1.0f // Tampilan normal
            holder.itemView.setOnClickListener {
                onItemClick(nomorSoal)
            }
        } else {
            // Soal Terkunci
            holder.tvNomor.visibility = View.GONE
            holder.ivGembok.visibility = View.VISIBLE
            holder.container.alpha = 0.5f // Tampilan redup
            holder.itemView.setOnClickListener(null) // Tidak bisa diklik
        }
    }

    override fun getItemCount(): Int = totalSoal

    // Fungsi untuk memperbarui jumlah soal yang terbuka
    fun updateSoalTerbuka(jumlah: Int) {
        soalTerbuka = jumlah
        notifyDataSetChanged()
    }
}
