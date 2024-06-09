package com.example.reservas_salas_si

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reservas_salas_si.models.Professor
import com.example.reservas_salas_si.models.Reserva
import com.example.reservas_salas_si.models.Sala
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private val repository = Repository()

    private val _professor = MutableLiveData<Professor>()
    val professor: LiveData<Professor> get() = _professor

    private val _sala = MutableLiveData<Sala>()
    val sala: LiveData<Sala> get() = _sala

    private val _professores = MutableLiveData<List<Professor>>()
    val professores: LiveData<List<Professor>> get() = _professores

    private val _salas = MutableLiveData<List<Sala>>()
    val salas: LiveData<List<Sala>> get() = _salas

    private val _reservas = MutableLiveData<List<Reserva>>()
    val reservas: LiveData<List<Reserva>> get() = _reservas

    private val _reservaResult = MutableLiveData<Reserva>()
    val reservaResult: LiveData<Reserva> get() = _reservaResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _isCreatingReserva = MutableLiveData<Boolean>(false)
    val isCreatingReserva: LiveData<Boolean> get() = _isCreatingReserva

    private val _isUpdatingReserva = MutableLiveData<Boolean>(false)
    val isUpdatingReserva: LiveData<Boolean> get() = _isUpdatingReserva

    fun setCreatingReserva(value: Boolean) {
        _isCreatingReserva.value = value
    }

    fun setUpdatingReserva(value: Boolean) {
        _isUpdatingReserva.value = value
    }

    fun fetchProfessorById(id: Int) {
        repository.getProfessor(id).enqueue(object : Callback<Professor> {
            override fun onResponse(call: Call<Professor>, response: Response<Professor>) {
                if (response.isSuccessful && response.body() != null) {
                    _professor.value = response.body()
                } else {
                    _errorMessage.value = "Professor não encontrado"
                }
            }

            override fun onFailure(call: Call<Professor>, t: Throwable) {
                _errorMessage.value = "Erro ao buscar professor"
            }
        })
    }

    fun fetchSalaById(id: Int) {
        repository.getSala(id).enqueue(object : Callback<Sala> {
            override fun onResponse(call: Call<Sala>, response: Response<Sala>) {
                if (response.isSuccessful && response.body() != null) {
                    _sala.value = response.body()
                } else {
                    _errorMessage.value = "Sala não encontrada"
                }
            }

            override fun onFailure(call: Call<Sala>, t: Throwable) {
                _errorMessage.value = "Erro ao buscar sala"
            }
        })
    }

    fun fetchAllProfessores() {
        Log.d("MainViewModel", "fetchAllProfessores chamado")
        try {
            repository.getProfessores().enqueue(object : Callback<List<Professor>> {
                override fun onResponse(call: Call<List<Professor>>, response: Response<List<Professor>>) {
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        Log.d("MainViewModel", "Professores recebidos: ${response.body()}")
                        _professores.value = response.body()
                    } else {
                        _errorMessage.value = "Nenhum professor encontrado"
                    }
                }

                override fun onFailure(call: Call<List<Professor>>, t: Throwable) {
                    Log.d("MainViewModel", "Falha na requisição: ${t.message}")
                    _errorMessage.value = "Erro ao buscar professores"
                }
            })
        } catch (e: Exception) {
            Log.e("MainViewModel", "Erro ao tentar buscar professores", e)
            _errorMessage.value = "Erro ao buscar professores"
        }
    }

    fun createProfessor(professor: Professor) {
        repository.createProfessor(professor).enqueue(object : Callback<Professor> {
            override fun onResponse(call: Call<Professor>, response: Response<Professor>) {
                if (response.isSuccessful) {
                    _successMessage.value = "Professor criado com sucesso"
                } else {
                    _errorMessage.value = "Erro ao criar professor"
                }
            }

            override fun onFailure(call: Call<Professor>, t: Throwable) {
                _errorMessage.value = "Erro ao criar professor"
            }
        })
    }

    fun updateProfessor(professorId: Int, professor: Professor) {
        repository.updateProfessor(professorId, professor).enqueue(object : Callback<Professor> {
            override fun onResponse(call: Call<Professor>, response: Response<Professor>) {
                if (response.isSuccessful) {
                    fetchAllProfessores()
                } else {
                    _errorMessage.value = "Erro ao atualizar professor"
                }
            }

            override fun onFailure(call: Call<Professor>, t: Throwable) {
                _errorMessage.value = "Erro ao atualizar professor"
            }
        })
    }

    fun deleteProfessor(professorId: Int) {
        repository.deleteProfessor(professorId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchAllProfessores()
                } else {
                    _errorMessage.value = "Erro ao excluir professor"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _errorMessage.value = "Erro ao excluir professor"
            }
        })
    }

    fun fetchAllSalas() {
        repository.getSalas().enqueue(object : Callback<List<Sala>> {
            override fun onResponse(call: Call<List<Sala>>, response: Response<List<Sala>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    _salas.value = response.body()
                } else {
                    _errorMessage.value = "Nenhuma sala encontrada"
                }
            }

            override fun onFailure(call: Call<List<Sala>>, t: Throwable) {
                _errorMessage.value = "Erro ao buscar salas"
            }
        })
    }

    fun createSala(sala: Sala) {
        repository.createSala(sala).enqueue(object : Callback<Sala> {
            override fun onResponse(call: Call<Sala>, response: Response<Sala>) {
                if (response.isSuccessful) {
                    _successMessage.value = "Sala criada com sucesso"
                } else {
                    _errorMessage.value = "Erro ao criar sala"
                }
            }

            override fun onFailure(call: Call<Sala>, t: Throwable) {
                _errorMessage.value = "Erro ao atualizar sala"
            }
        })
    }

    fun updateSala(salaId: Int, sala: Sala) {
        repository.updateSala(salaId, sala).enqueue(object : Callback<Sala> {
            override fun onResponse(call: Call<Sala>, response: Response<Sala>) {
                if (response.isSuccessful) {
                    fetchAllSalas()
                } else {
                    _errorMessage.value = "Erro ao atualizar sala"
                }
            }

            override fun onFailure(call: Call<Sala>, t: Throwable) {
                _errorMessage.value = "Erro ao atualizar sala"
            }
        })
    }

    fun deleteSala(salaId: Int) {
        repository.deleteSala(salaId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchAllSalas()
                } else {
                    _errorMessage.value = "Erro ao excluir sala"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _errorMessage.value = "Erro ao excluir sala"
            }
        })
    }

    private fun fetchAllSalasHome() {
        repository.getSalas().enqueue(object : Callback<List<Sala>> {
            override fun onResponse(call: Call<List<Sala>>, response: Response<List<Sala>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    _salas.value = response.body()
                } else {
                    _errorMessage.value = "Nenhuma sala encontrada"
                }
            }

            override fun onFailure(call: Call<List<Sala>>, t: Throwable) {
                _errorMessage.value = "Erro ao buscar salas"
            }
        })
    }

    fun fetchReservasByDate(date: String) {
        repository.getReservasByDate(date).enqueue(object : Callback<List<Reserva>> {
            override fun onResponse(call: Call<List<Reserva>>, response: Response<List<Reserva>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    _reservas.value = response.body()
                } else {
                    _reservas.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<Reserva>>, t: Throwable) {
                _errorMessage.value = "Erro ao buscar reservas"
            }
        })
    }

    fun createReserva(reserva: Reserva) {
        if (_isUpdatingReserva.value == true) return

        _isCreatingReserva.value = true

        repository.createReserva(reserva).enqueue(object : Callback<Reserva> {
            override fun onResponse(call: Call<Reserva>, response: Response<Reserva>) {
                _isCreatingReserva.value = false
                if (response.isSuccessful) {
                    fetchReservasByDate(reserva.data_reserva)
                    _successMessage.value = "Reserva criada com sucesso"
                } else {
                    _errorMessage.value = "Erro ao criar reserva"
                }
            }

            override fun onFailure(call: Call<Reserva>, t: Throwable) {
                _isCreatingReserva.value = false
                _errorMessage.value = "Erro ao criar reserva"
            }
        })
    }

    fun updateReserva(reservaId: Int, reserva: Reserva) {
        if (_isCreatingReserva.value == true) return

        _isUpdatingReserva.value = true
        repository.updateReserva(reservaId, reserva).enqueue(object : Callback<Reserva> {
            override fun onResponse(call: Call<Reserva>, response: Response<Reserva>) {
                _isUpdatingReserva.value = false
                if (response.isSuccessful) {
                    fetchReservasByDate(reserva.data_reserva)
                    _successMessage.value = "Reserva editada com sucesso"
                } else {
                    _errorMessage.value = "Erro ao atualizar reserva"
                }
            }

            override fun onFailure(call: Call<Reserva>, t: Throwable) {
                _isUpdatingReserva.value = false
                _errorMessage.value = "Erro ao atualizar reserva"
            }
        })
    }

    fun deleteReserva(reservaId: Int) {
        repository.deleteReserva(reservaId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _successMessage.value = "Reserva excluída com sucesso"
                } else {
                    _errorMessage.value = "Erro ao excluir reserva"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _errorMessage.value = "Erro ao excluir reserva"
            }
        })
    }

    fun getSalasComReservas(date: String) {
        fetchAllSalasHome()
        fetchReservasByDate(date)
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

