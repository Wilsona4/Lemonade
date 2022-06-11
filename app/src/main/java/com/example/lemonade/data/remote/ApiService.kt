package com.example.lemonade.data.remote

import com.example.lemonade.data.remote.model.Student
import retrofit2.http.GET

interface ApiService {
    @GET("students")
    suspend fun getStudentData(): List<Student>
}