package com.example.reservas_salas_si.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservas_salas_si.R
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala

data class SalaComReserva(
    val sala: Sala,
    val reserva: Reserva?
)

class SalaAdapterHome(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SalaAdapterHome.SalaHomeViewHolder>() {

    private var salas: List<SalaComReserva> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sala_status, parent, false)
        return SalaHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalaHomeViewHolder, position: Int) {
        holder.bind(salas[position])
    }

    override fun getItemCount(): Int {
        return salas.size
    }

    fun submitList(newSalas: List<SalaComReserva>) {
        salas = newSalas
        notifyDataSetChanged()
    }

    inner class SalaHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val salaNameTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)

        fun bind(salaComReserva: SalaComReserva) {
            salaNameTextView.text = salaComReserva.sala.nome
            if (salaComReserva.reserva != null) {
                statusTextView.text = "Ocupada"
                statusTextView.setTextColor(Color.RED)
            } else {
                statusTextView.text = "Livre"
                statusTextView.setTextColor(Color.GREEN)
            }

            itemView.setOnClickListener {
                listener.onItemClick(salaComReserva)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(salaComReserva: SalaComReserva)
    }
}
