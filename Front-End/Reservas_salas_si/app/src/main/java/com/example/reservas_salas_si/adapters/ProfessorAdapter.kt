package com.example.reservas_salas_si.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservas_salas_si.R
import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Reserva

class ProfessorAdapter(
    private val professors: List<Professor>,
    private val onEditClick: (Professor) -> Unit,
    private val onDeleteClick: (Professor) -> Unit
) : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_professor, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]
        holder.bind(professor)
    }

    override fun getItemCount() = professors.size

    inner class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.professorNameTextView)
        private val idProfessorTextView: TextView = itemView.findViewById(R.id.professorIdTextView)
        private val editImageView: ImageView = itemView.findViewById(R.id.editProfessorImageView)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.deleteProfessorImageView)

        fun bind(professor: Professor) {
            nameTextView.text = professor.nome
            idProfessorTextView.text = professor.id.toString()

            editImageView.setOnClickListener {
                onEditClick(professor)
            }

            deleteImageView.setOnClickListener {
                onDeleteClick(professor)
            }
        }
    }
}
