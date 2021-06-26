package br.unifor.todolist.database

import androidx.room.*
import br.unifor.todolist.model.User
import br.unifor.todolist.model.UserWithTasks

@Dao
interface UserDAO {
    @Insert
    fun insert(user :User)
    @Update
    fun update(user :User)
    @Delete
    fun delete(user :User)
    @Query("SELECT * FROM users WHERE id = :id")
    fun find(id :Int) :User
    @Query("SELECT * FROM users WHERE email = :email")
    fun findByEmail(email :String) :User
    @Query("SELECT * FROM users")
    fun findAll() :List<User>
    @Query("SELECT * FROM users WHERE id = :id")
    fun findUserWithTasks(id :Int) :UserWithTasks
}