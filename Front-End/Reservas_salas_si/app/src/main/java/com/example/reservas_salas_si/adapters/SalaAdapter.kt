package com.example.reservas_salas_si.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservas_salas_si.R
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala

class SalaAdapter(
    private val salas: List<Sala>,
    private val onEditClick: (Sala) -> Unit,
    private val onDeleteClick: (Sala) -> Unit
) : RecyclerView.Adapter<SalaAdapter.SalaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sala, parent, false)
        return SalaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalaViewHolder, position: Int) {
        val sala = salas[position]
        holder.bind(sala)
    }

    override fun getItemCount() = salas.size

    inner class SalaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.salaNameTextView)
        private val idSalaTextView: TextView = itemView.findViewById(R.id.salaIdTextView)
        private val editImageView: ImageView = itemView.findViewById(R.id.editSalaImageView)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.deleteSalaImageView)

        fun bind(sala: Sala) {
            nameTextView.text = sala.nome
            idSalaTextView.text = sala.id.toString()

            editImageView.setOnClickListener {
                onEditClick(sala)
            }

            deleteImageView.setOnClickListener {
                onDeleteClick(sala)
            }
        }
    }
}

