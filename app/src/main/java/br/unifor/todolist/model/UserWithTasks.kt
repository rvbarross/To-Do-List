package br.unifor.todolist.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTasks (
    @Embedded
    val user :User,
    @Relation(parentColumn = "id", entityColumn = "user_id")
    val tasks :List<Task>
)