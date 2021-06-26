package br.unifor.todolist.database
import androidx.room.Database
import androidx.room.RoomDatabase
import br.unifor.todolist.model.Task
import br.unifor.todolist.model.User


@Database(entities = [User::class, Task::class], version = 2)
abstract class ToDoDatabase :RoomDatabase() {
    abstract fun getUserDAO() :UserDAO
    abstract fun getTaskDAO() :TaskDAO
}