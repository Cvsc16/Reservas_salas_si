package com.example.reservas_salas_si

import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala
import com.example.reservas_salas_si.services.ApiService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class Repository {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://127.0.0.1:8000/")
            .baseUrl("http://192.168.3.19:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getProfessor(id: Int) = apiService.getProfessor(id)

    fun createProfessor(professor: Professor) = apiService.createProfessor(professor)

    fun getProfessores() = apiService.getProfessores()

    fun updateProfessor(id: Int, professor: Professor) = apiService.updateProfessor(id, professor)

    fun deleteProfessor(id: Int) = apiService.deleteProfessor(id)

    fun getSala(id: Int) = apiService.getSala(id)

    fun createSala(sala: Sala) = apiService.createSala(sala)

    fun getSalas() = apiService.getSalas()

    fun updateSala(id: Int, sala: Sala) = apiService.updateSala(id, sala)

    fun deleteSala(id: Int) = apiService.deleteSala(id)

    fun getReserva(id: Int) = apiService.getReserva(id)

    fun createReserva(reserva: Reserva) = apiService.createReserva(reserva)

    fun updateReserva(id: Int, reserva: Reserva) = apiService.updateReserva(id, reserva)

    fun deleteReserva(id: Int) = apiService.deleteReserva(id)

    fun getReservas() = apiService.getReservas()

    fun getReservasByDate(date: String): Call<List<Reserva>> {
        return apiService.getReservasByDate(date)
    }
}