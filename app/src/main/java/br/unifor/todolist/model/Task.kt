package br.unifor.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey
    val id :Int? = null,
    val name :String,
    val description :String,
    @ColumnInfo(name = "is_done")
    val isDone :Boolean,
    @ColumnInfo(name = "user_id")
    val userID :Int
)