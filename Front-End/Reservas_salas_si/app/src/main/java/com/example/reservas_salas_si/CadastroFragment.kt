package com.example.reservas_salas_si

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Sala

class CadastroFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var nomeProfessorEditText: EditText
    private lateinit var emailProfessorEditText: EditText
    private lateinit var nomeSalaEditText: EditText
    private lateinit var capacidadeSalaEditText: EditText
    private lateinit var cadastrarProfessorButton: Button
    private lateinit var cadastrarSalaButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cadastro, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        nomeProfessorEditText = root.findViewById(R.id.nomeProfessorEditText)
        emailProfessorEditText = root.findViewById(R.id.emailProfessorEditText)
        nomeSalaEditText = root.findViewById(R.id.nomeSalaEditText)
        capacidadeSalaEditText = root.findViewById(R.id.capacidadeSalaEditText)
        cadastrarProfessorButton = root.findViewById(R.id.cadastrarProfessorButton)
        cadastrarSalaButton = root.findViewById(R.id.cadastrarSalaButton)

        cadastrarProfessorButton.setOnClickListener {
            val nome = nomeProfessorEditText.text.toString()
            val email = emailProfessorEditText.text.toString()

            if (nome.isEmpty() || email.isEmpty()) {
                showToast("Por favor, preencha todos os campos")
            } else {
                val professor = Professor(nome = nome, email = email)
                viewModel.createProfessor(professor)
            }
        }

        cadastrarSalaButton.setOnClickListener {
            val nome = nomeSalaEditText.text.toString()
            val capacidadeText = capacidadeSalaEditText.text.toString()

            if (nome.isEmpty() || capacidadeText.isEmpty()) {
                showToast("Por favor, preencha todos os campos")
            } else {
                val capacidade = capacidadeText.toIntOrNull()
                if (capacidade == null) {
                    showToast("Capacidade inválida")
                } else {
                    val sala = Sala(nome = nome, capacidade = capacidade)
                    viewModel.createSala(sala)
                }
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
            }
        })

        return root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
