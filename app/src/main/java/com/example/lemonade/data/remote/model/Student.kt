package com.example.lemonade.data.remote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    val age: Int,
    val avatar: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    val department: String,
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "profile_id")
    val profileId: String
)