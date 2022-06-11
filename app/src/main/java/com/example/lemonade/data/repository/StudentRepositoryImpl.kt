package com.example.lemonade.data.repository

import androidx.room.withTransaction
import com.example.lemonade.data.local.StudentDatabase
import com.example.lemonade.data.remote.ApiService
import com.example.lemonade.data.remote.model.Student
import com.example.lemonade.di.Dispatcher
import com.example.lemonade.di.LemonadeDispatchers
import com.example.lemonade.domain.repository.StudentRepository
import com.example.lemonade.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StudentRepositoryImpl(
    private val lemonadeApiService: ApiService,
    private val database: StudentDatabase,
    @Dispatcher(LemonadeDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : StudentRepository {
    override fun getStudent(): Flow<Resource<List<Student>>> = flow {

        val studentDao = database.studentDao()

        emit(Resource.Loading())

        val cachedData = studentDao.readStudents().first()

        if (cachedData.isNotEmpty()) {
            emit(Resource.Success(cachedData))
        }

        val remoteData = try {
            lemonadeApiService.getStudentData()
        } catch (e: Exception) {
            studentDao.readStudents().collect { studentList ->
                emit(Resource.Error(e.message ?: "Something went wrong", studentList))
            }
            null
        }

        remoteData?.let { studentList ->
            database.withTransaction {
                studentDao.deleteStudents()
                studentList.forEach { student ->
                    studentDao.addStudent(student)
                }
            }
            studentDao.readStudents().collect {
                emit(Resource.Success(it))
            }
        }

    }.flowOn(ioDispatcher)

}