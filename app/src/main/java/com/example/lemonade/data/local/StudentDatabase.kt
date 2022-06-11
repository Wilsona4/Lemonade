package com.example.lemonade.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lemonade.data.remote.model.Student

@Database(
    entities = [Student::class],
    version = 1,
    exportSchema = false
)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        var DATABASE_NAME: String = "student_db"
    }
}