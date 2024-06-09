package com.example.reservas_salas_si.models

data class Reserva(
    val id: Int? = null,
    val professor_id: Int,
    val sala_id: Int,
    val data_reserva: String,
    val nome_professor: String? = null,
    val nome_sala: String? = null
)