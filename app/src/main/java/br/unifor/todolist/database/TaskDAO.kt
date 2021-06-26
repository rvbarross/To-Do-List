package br.unifor.todolist.database

import androidx.room.*
import br.unifor.todolist.model.Task

@Dao
interface TaskDAO {
    @Insert
    fun insert(task : Task)
    @Delete
    fun delete(task :Task)
    @Update
    fun update(task :Task)
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun find(id :Int) :Task
    @Query("SELECT * FROM tasks")
    fun findAll() :List<Task>
}
