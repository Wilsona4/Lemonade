package com.example.lemonade.data.local

import androidx.room.*
import com.example.lemonade.data.remote.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    /*Add User to Database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(student: Student)

    /*Get User in the Database*/
    @Transaction
    @Query("SELECT * FROM student_table")
    fun readStudents(): Flow<List<Student>>

    /*Delete user in the Database*/
    @Query("DELETE FROM student_table")
    suspend fun deleteStudents()
}