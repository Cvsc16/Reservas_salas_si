package com.example.reservas_salas_si

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservas_salas_si.adapters.ProfessorAdapter
import com.example.reservas_salas_si.adapters.SalaAdapter
import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Sala

class ConsultaFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var consultaIdEditText: EditText
    private lateinit var consultaProfessorButton: View
    private lateinit var consultaSalaButton: View
    private lateinit var consultaGeralProfessoresButton: View
    private lateinit var consultaGeralSalasButton: View
    private lateinit var resultTitleTextView: TextView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var professorAdapter: ProfessorAdapter
    private lateinit var salaAdapter: SalaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_consulta, container, false)

        consultaIdEditText = root.findViewById(R.id.consultaIdEditText)
        consultaProfessorButton = root.findViewById(R.id.consultaProfessorButton)
        consultaSalaButton = root.findViewById(R.id.consultaSalaButton)
        consultaGeralProfessoresButton = root.findViewById(R.id.consultaGeralProfessoresButton)
        consultaGeralSalasButton = root.findViewById(R.id.consultaGeralSalasButton)
        resultTitleTextView = root.findViewById(R.id.resultTitleTextView)
        resultRecyclerView = root.findViewById(R.id.resultRecyclerView)
        errorTextView = root.findViewById(R.id.errorTextView)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        professorAdapter = ProfessorAdapter(emptyList(), ::onEditProfessor, ::onDeleteProfessor)
        salaAdapter = SalaAdapter(emptyList(), ::onEditSala, ::onDeleteSala)

        resultRecyclerView.layoutManager = LinearLayoutManager(context)

        consultaProfessorButton.setOnClickListener {
            val idText = consultaIdEditText.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toIntOrNull()
                if (id != null) {
                    viewModel.fetchProfessorById(id)
                } else {
                    showToast("Por favor, insira um ID válido")
                }
            } else {
                showToast("Por favor, insira um ID")
            }
        }

        consultaSalaButton.setOnClickListener {
            val idText = consultaIdEditText.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toIntOrNull()
                if (id != null) {
                    viewModel.fetchSalaById(id)
                } else {
                    showToast("Por favor, insira um ID válido")
                }
            } else {
                showToast("Por favor, insira um ID")
            }
        }

        consultaGeralProfessoresButton.setOnClickListener {
            viewModel.fetchAllProfessores()
        }

        consultaGeralSalasButton.setOnClickListener {
            viewModel.fetchAllSalas()
        }

        viewModel.professor.observe(viewLifecycleOwner, Observer { professor ->
            resultTitleTextView.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            professorAdapter = ProfessorAdapter(listOf(professor), ::onEditProfessor, ::onDeleteProfessor)
            resultRecyclerView.adapter = professorAdapter
        })

        viewModel.sala.observe(viewLifecycleOwner, Observer { sala ->
            resultTitleTextView.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            salaAdapter = SalaAdapter(listOf(sala), ::onEditSala, ::onDeleteSala)
            resultRecyclerView.adapter = salaAdapter
        })

        viewModel.professores.observe(viewLifecycleOwner, Observer { professores ->
            resultTitleTextView.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            professorAdapter = ProfessorAdapter(professores, ::onEditProfessor, ::onDeleteProfessor)
            resultRecyclerView.adapter = professorAdapter
        })

        viewModel.salas.observe(viewLifecycleOwner, Observer { salas ->
            resultTitleTextView.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            salaAdapter = SalaAdapter(salas, ::onEditSala, ::onDeleteSala)
            resultRecyclerView.adapter = salaAdapter
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                resultTitleTextView.visibility = View.GONE
                resultRecyclerView.visibility = View.GONE
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = errorMessage
            }
        })

        return root
    }

    private fun onEditProfessor(professor: Professor) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_professor, null)
        val editProfessorNameEditText: EditText = dialogView.findViewById(R.id.editProfessorNameEditText)
        val editProfessorEmailEditText: EditText = dialogView.findViewById(R.id.editProfessorEmailEditText)

        editProfessorNameEditText.setText(professor.nome)
        editProfessorEmailEditText.setText(professor.email)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Professor")
            .setView(dialogView)
            .setPositiveButton("Confirmar") { dialog, _ ->
                val nome = editProfessorNameEditText.text.toString()
                val email = editProfessorEmailEditText.text.toString()

                if (nome.isNotEmpty() && email.isNotEmpty()) {
                    val updatedProfessor = professor.copy(nome = nome, email = email)
                    viewModel.updateProfessor(professor.id!!, updatedProfessor)
                    showToast("Professor atualizado com sucesso")
                } else {
                    showToast("Por favor, preencha todos os campos")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun onDeleteProfessor(professor: Professor) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Professor")
            .setMessage("Tem certeza de que deseja excluir o professor ${professor.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.deleteProfessor(professor.id!!)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun onEditSala(sala: Sala) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_sala, null)
        val editSalaNameEditText: EditText = dialogView.findViewById(R.id.editSalaNameEditText)
        val editSalaCapacidadeEditText: EditText = dialogView.findViewById(R.id.editSalaCapacidadeEditText)

        editSalaNameEditText.setText(sala.nome)
        editSalaCapacidadeEditText.setText(sala.capacidade.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Sala")
            .setView(dialogView)
            .setPositiveButton("Confirmar") { dialog, _ ->
                val nome = editSalaNameEditText.text.toString()
                val capacidade = editSalaCapacidadeEditText.text.toString()

                if (nome.isNotEmpty() && capacidade.isNotEmpty()) {
                    val updatedSala = sala.copy(nome = nome, capacidade = capacidade.toInt())
                    viewModel.updateSala(sala.id!!, updatedSala)
                    showToast("Sala atualizada com sucesso")
                } else {
                    showToast("Por favor, preencha todos os campos")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun onDeleteSala(sala: Sala) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Sala")
            .setMessage("Tem certeza de que deseja excluir a sala ${sala.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.deleteSala(sala.id!!)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}


