package com.example.tebakgambarliteuas

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoalAdapter(private val listSoal: ArrayList<ModelSoal>) :
    RecyclerView.Adapter<SoalAdapter.SoalViewHolder>() {

    class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvIsiSoal: TextView = itemView.findViewById(R.id.tvIsiSoal)
        // TextView untuk email tidak lagi kita gunakan, tapi biarkan saja di ViewHolder
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_soal, parent, false)
        return SoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = listSoal[position]

        holder.tvUsername.text = "Pengirim: ${soal.username}"
        holder.tvIsiSoal.text = "${soal.kata1} + ${soal.kata2}"
        // --- FIX: Hapus baris yang menyebabkan error ---
        // holder.tvEmail.text = soal.email
        holder.tvEmail.visibility = View.GONE // Sembunyikan TextView email

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailSoalActivity::class.java)

            intent.putExtra("id", soal.id)
            intent.putExtra("username", soal.username)
            // --- FIX: Hapus baris yang menyebabkan error ---
            // intent.putExtra("email", soal.email)
            intent.putExtra("kata1", soal.kata1)
            intent.putExtra("kata2", soal.kata2)
            intent.putExtra("deskripsi", soal.deskripsi)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listSoal.size
    }
}
