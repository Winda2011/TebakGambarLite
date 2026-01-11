package com.example.tebakgambarliteuas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoalAdminAdapter(
    private val soalList: MutableList<ModelSoal>,
    private val onEditClick: (ModelSoal) -> Unit,
    private val onDeleteClick: (ModelSoal) -> Unit
) : RecyclerView.Adapter<SoalAdminAdapter.SoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soal_admin, parent, false)
        return SoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = soalList[position]
        holder.bind(soal)
    }

    override fun getItemCount(): Int = soalList.size

    inner class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvKata1: TextView = itemView.findViewById(R.id.tvKata1)
        private val tvKata2: TextView = itemView.findViewById(R.id.tvKata2)
        private val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnHapus: Button = itemView.findViewById(R.id.btnHapus)

        fun bind(soal: ModelSoal) {
            tvKata1.text = soal.kata1
            tvKata2.text = soal.kata2
            tvDeskripsi.text = soal.deskripsi
            tvUsername.text = "Pengirim: ${soal.username}"

            btnEdit.setOnClickListener { onEditClick(soal) }
            btnHapus.setOnClickListener { onDeleteClick(soal) }
        }
    }
}
