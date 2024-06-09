package com.example.reservas_salas_si.services

import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/recurso1/{id}")
    fun getProfessor(@Path("id") id: Int): Call<Professor>

    @POST("api/recurso1/")
    fun createProfessor(@Body professor: Professor): Call<Professor>

    @GET("api/recurso2/")
    fun getProfessores(): Call<List<Professor>>

    @PUT("api/recurso3/{id}")
    fun updateProfessor(@Path("id") id: Int, @Body professor: Professor): Call<Professor>

    @DELETE("api/recurso3/{id}")
    fun deleteProfessor(@Path("id") id: Int): Call<Void>

    @GET("api/recurso4/{id}")
    fun getSala(@Path("id") id: Int): Call<Sala>

    @POST("api/recurso4/")
    fun createSala(@Body sala: Sala): Call<Sala>

    @GET("api/recurso4/")
    fun getSalas(): Call<List<Sala>>

    @PUT("api/recurso4/{id}")
    fun updateSala(@Path("id") id: Int, @Body sala: Sala): Call<Sala>

    @DELETE("api/recurso4/{id}")
    fun deleteSala(@Path("id") id: Int): Call<Void>

    @GET("api/recurso5/{id}")
    fun getReserva(@Path("id") id: Int): Call<Reserva>

    @POST("api/recurso5/")
    fun createReserva(@Body reserva: Reserva): Call<Reserva>

    @PUT("api/recurso5/{id}")
    fun updateReserva(@Path("id") id: Int, @Body reserva: Reserva): Call<Reserva>

    @DELETE("api/recurso5/{id}")
    fun deleteReserva(@Path("id") id: Int): Call<Void>

    @GET("api/recurso5/")
    fun getReservas(): Call<List<Reserva>>

    @GET("api/recurso5")
    fun getReservasByDate(@Query("data_reserva") data_reserva: String): Call<List<Reserva>>
}
