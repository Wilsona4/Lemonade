package com.example.lemonade.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lemonade.data.remote.model.Student
import com.example.lemonade.domain.repository.StudentRepository
import com.example.lemonade.util.Resource
import com.example.lemonade.util.exhausted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class StudentsUiState(
    val students: List<Student> = emptyList(),
    val loading: Boolean = false,
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(StudentsUiState(loading = true))
    val uiState: StateFlow<StudentsUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getStudents()
    }

    /*Get Student List*/
    private fun getStudents() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            studentRepository.getStudent().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                students = result.data ?: emptyList(),
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                students = result.data ?: emptyList(),
                            )
                        }

                        _uiEvent.send(result.message ?: "Something went wrong")

                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(loading = true) }
                    }
                }.exhausted
            }
        }
    }
}