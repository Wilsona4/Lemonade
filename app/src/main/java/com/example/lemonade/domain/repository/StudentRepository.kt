package com.example.lemonade.domain.repository

import com.example.lemonade.data.remote.model.Student
import com.example.lemonade.util.Resource
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getStudent(): Flow<Resource<List<Student>>>
}