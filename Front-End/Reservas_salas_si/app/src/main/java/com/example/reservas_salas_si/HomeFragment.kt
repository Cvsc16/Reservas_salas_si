package com.example.reservas_salas_si

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservas_salas_si.adapters.SalaAdapterHome
import com.example.reservas_salas_si.adapters.SalaComReserva
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), SalaAdapterHome.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalaAdapterHome
    private lateinit var calendarView: CalendarView
    private var selectedDate: String? = null

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)
        calendarView = root.findViewById(R.id.calendarView)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = SalaAdapterHome(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.salas.observe(viewLifecycleOwner, Observer { salas ->
            updateSalasComReservas(salas, viewModel.reservas.value ?: emptyList())
        })

        viewModel.reservas.observe(viewLifecycleOwner, Observer { reservas ->
            updateSalasComReservas(viewModel.salas.value ?: emptyList(), reservas)
        })

        viewModel.successMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
                viewModel.clearSuccessMessage()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
                viewModel.clearErrorMessage()
            }
        })

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            selectedDate?.let {
                viewModel.getSalasComReservas(it)
            }
        }

        return root
    }

    private fun updateSalasComReservas(salas: List<Sala>, reservas: List<Reserva>) {
        val salasComReservas = salas.map { sala ->
            val reserva = reservas.find { it.sala_id == sala.id }
            SalaComReserva(sala, reserva)
        }
        adapter.submitList(salasComReservas)
    }

    private fun showReserveSalaDialog(date: String, salaId: Int) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_reserve_sala, null)
        val professorIdEditText = dialogView.findViewById<EditText>(R.id.professorIdEditText)

        AlertDialog.Builder(activity)
            .setTitle("Reservar Sala")
            .setView(dialogView)
            .setPositiveButton("Reservar") { _, _ ->
                val professorIdText = professorIdEditText.text.toString()
                if (professorIdText.isEmpty()) {
                    showToast("Por favor, insira o ID do professor")
                    return@setPositiveButton
                }
                val professorId = professorIdText.toIntOrNull()
                if (professorId == null) {
                    showToast("ID do professor inválido")
                    return@setPositiveButton
                }
                viewModel.setCreatingReserva(true)
                viewModel.fetchProfessorById(professorId)
                viewModel.professor.observe(viewLifecycleOwner, Observer { professor ->
                    professor?.let {
                        if (viewModel.isCreatingReserva.value == true) {
                            val novaReserva = Reserva(
                                id = 0,
                                sala_id = salaId,
                                professor_id = professorId,
                                data_reserva = date,
                                nome_professor = professor.nome,
                                nome_sala = "",
                            )
                            viewModel.createReserva(novaReserva)
                            viewModel.setCreatingReserva(false)
                        }
                    }
                })
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(salaComReserva: SalaComReserva) {
        salaComReserva.reserva?.let {
            showProfessorInfoDialog(it)
        } ?: run {
            selectedDate?.let {
                showReserveSalaDialog(it, salaComReserva.sala.id!!)
            }
        }
    }

    private fun showProfessorInfoDialog(reserva: Reserva) {
        AlertDialog.Builder(activity)
            .setTitle("Informação do Professor")
            .setMessage("Professor: ${reserva.nome_professor}")
            .setPositiveButton("OK", null)
            .setNegativeButton("Editar") { _, _ ->
                showEditReservaDialog(reserva)
            }
            .setNeutralButton("Excluir") { _, _ ->
                confirmDeleteReserva(reserva)
            }
            .create()
            .show()
    }

    private fun showEditReservaDialog(reserva: Reserva) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_reserve_sala, null)
        val professorIdEditText = dialogView.findViewById<EditText>(R.id.professorIdEditText)

        professorIdEditText.setText(reserva.professor_id.toString())

        AlertDialog.Builder(activity)
            .setTitle("Editar Reserva")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                val professorIdText = professorIdEditText.text.toString()
                if (professorIdText.isEmpty()) {
                    showToast("Por favor, insira o ID do professor")
                    return@setPositiveButton
                }
                val professorId = professorIdText.toIntOrNull()
                if (professorId == null) {
                    showToast("ID do professor inválido")
                    return@setPositiveButton
                }
                viewModel.setUpdatingReserva(true)
                viewModel.fetchProfessorById(professorId)
                viewModel.professor.observe(viewLifecycleOwner, Observer { professor ->
                    professor?.let {
                        if (viewModel.isUpdatingReserva.value == true) {
                            val reservaEditada = reserva.copy(
                                professor_id = professorId,
                                nome_professor = professor.nome,
                            )
                            viewModel.updateReserva(reserva.id!!, reservaEditada)
                            viewModel.setUpdatingReserva(false)
                            selectedDate?.let { viewModel.fetchReservasByDate(it) }
                        }
                    }
                })
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun confirmDeleteReserva(reserva: Reserva) {
        AlertDialog.Builder(activity)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja excluir esta reserva?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteReserva(reserva.id!!)
                selectedDate?.let { viewModel.fetchReservasByDate(it) }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }
}

